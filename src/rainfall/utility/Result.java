package rainfall.utility;

import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public sealed interface Result<Value, Error> {
  record Success<Value, Error>(Value value) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return true; }
    @Override public boolean isFailure() { return false; }

    @Override public Error error() {
      throw new RuntimeException("Result is success!");
    }

    private static void test(final Tester tester) {
      final var value  = 1;
      final var result = new Success<>(value);
      tester.run(result::isSuccess);
      tester.run(() -> !result.isFailure());
      tester.run(() -> result.value() == value);
    }
  }

  record Failure<Value, Error>(Error error) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return false; }
    @Override public boolean isFailure() { return true; }

    @Override public Value value() {
      throw new RuntimeException("Result is failure!");
    }

    private static void test(final Tester tester) {
      final var error  = 1;
      final var result = new Failure<>(error);
      tester.run(() -> !result.isSuccess());
      tester.run(result::isFailure);
      tester.run(() -> result.error() == error);
    }
  }

  static <Value, Error> Result<Value, Error> success(final Value value) {
    return new Success<>(value);
  }

  static <Value, Error> Result<Value, Error> failure(final Error error) {
    return new Failure<>(error);
  }

  static <CombinedValue, CombinedError, Value, Error>
    Result<CombinedValue, CombinedError>
    combine(final Collection<Result<Value, Error>> results,
      final Function<List<Value>, CombinedValue> valueCombiner,
      final Function<List<Error>, CombinedError> errorCombiner) {
    final var errors =
      results.stream().filter(Result::isFailure).map(Result::error).toList();
    if (!errors.isEmpty()) return Result.failure(errorCombiner.apply(errors));
    return Result.success(
      valueCombiner.apply(results.stream().map(Result::value).toList()));
  }

  default <TargetValue> Result<TargetValue, Error> propagate() {
    return Result.failure(error());
  }

  default <TargetValue> Result<TargetValue, Error>
    map(Function<Value, TargetValue> mapper) {
    if (isFailure()) return propagate();
    return success(mapper.apply(value()));
  }

  boolean isSuccess();
  boolean isFailure();
  Value value();
  Error error();

  static void test(final Tester tester) {
    Success.test(tester);
    Failure.test(tester);
    final var success = success(1);
    final var failure = failure(1);
    tester.run(success::isSuccess);
    tester.run(failure::isFailure);

    tester.run(() -> {
      final BinaryOperator<Integer>          reducer  =
        (left, right) -> left + right;
      final Function<List<Integer>, Integer> combiner =
        list -> list.stream().reduce(0, reducer);
      final var                              results  =
        List.<Result<Integer, Integer>>of(success(1), failure(1), success(1),
          success(1));
      final var                              combined =
        combine(results, combiner, combiner);
      return combined.isFailure() && combined.error() == 1;
    });

    tester.run(() -> {
      final BinaryOperator<Integer>          reducer  =
        (left, right) -> left + right;
      final Function<List<Integer>, Integer> combiner =
        list -> list.stream().reduce(0, reducer);
      final var                              results  =
        List.<Result<Integer, Integer>>of(success(1), success(1), success(1),
          success(1));
      final var                              combined =
        combine(results, combiner, combiner);
      return combined.isSuccess() && combined.value() == 4;
    });

    tester.run(() -> {
      final Result<Integer, Integer> first  = failure(1);
      final Result<String, Integer>  second = first.propagate();
      return second.isFailure() && second.error().equals(first.error());
    });

    tester.run(() -> {
      final Result<Integer, Integer> first  = failure(1);
      final Result<String, Integer>  second = first.map(String::valueOf);
      return second.isFailure() && second.error().equals(first.error());
    });

    tester.run(() -> {
      final Result<Integer, Integer> first  = success(1);
      final Result<String, Integer>  second = first.map(String::valueOf);
      return second.isSuccess()
        && second.value().equals(String.valueOf(first.value()));
    });
  }
}
