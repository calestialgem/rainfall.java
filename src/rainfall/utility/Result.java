package rainfall.utility;

import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Outcome of an algorithm that can fail.
 *
 * @param  <Value> Type of the result of a succeeded completion.
 * @param  <Error> Type of the result of a failed completion.
 * @author         calestialgem
 */
public sealed interface Result<Value, Error> {
  /**
   * Successful {@link Result}.
   *
   * @param  <Value> Type of the resulted value.
   * @param  <Error> Type of the error that would have resulted on failure.
   * @param  value   Resulted value.
   * @author         calestialgem
   */
  record Success<Value, Error>(Value value) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return true; }
    @Override public boolean isFailure() { return false; }

    @Override public Error error() {
      throw new RuntimeException("Result is success!");
    }

    /**
     * Runs all the unit tests for {@link Success}.
     *
     * @param tester Used unit test runner.
     */
    private static void test(final Tester tester) {
      final var value  = 1;
      final var result = new Success<>(value);
      tester.run(result::isSuccess);
      tester.run(() -> !result.isFailure());
      tester.run(() -> result.value() == value);
    }
  }

  /**
   * Failed {@link Result}.
   *
   * @param  <Value> Type of the value that would have resulted on success.
   * @param  <Error> Type of the resulted error.
   * @param  error   Resulted error.
   * @author         calestialgem
   */
  record Failure<Value, Error>(Error error) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return false; }
    @Override public boolean isFailure() { return true; }

    @Override public Value value() {
      throw new RuntimeException("Result is failure!");
    }

    /**
     * Runs all the unit tests for {@link Failure}.
     *
     * @param tester Used unit test runner.
     */
    private static void test(final Tester tester) {
      final var error  = 1;
      final var result = new Failure<>(error);
      tester.run(() -> !result.isSuccess());
      tester.run(result::isFailure);
      tester.run(() -> result.error() == error);
    }
  }

  /**
   * Creates a successful result.
   *
   * @param  <Value> Type of the resulted value.
   * @param  <Error> Type of the error that would have resulted on failure.
   * @param  value   Resulted value.
   * @return         New success that contains the given value.
   */
  static <Value, Error> Result<Value, Error> success(final Value value) {
    return new Success<>(value);
  }

  /**
   * Creates a failed result.
   *
   * @param  <Value> Type of the value that would have resulted on success.
   * @param  <Error> Type of the resulted error.
   * @param  error   Resulted error.
   * @return         New failure that contains the given error.
   */
  static <Value, Error> Result<Value, Error> failure(final Error error) {
    return new Failure<>(error);
  }

  /**
   * Combines multiple results to a single one.
   *
   * @param  <CombinedValue> Type that is the result of combining the values.
   * @param  <CombinedError> Type that is the result of combining the errors.
   * @param  <Value>         Type of the successful results.
   * @param  <Error>         Type of the failed results.
   * @param  results         Combined results.
   * @param  valueCombiner   Function that combines values.
   * @param  errorCombiner   Function that combines errors.
   * @return                 Combined values, or combined errors.
   */
  static <CombinedValue, CombinedError, Value, Error>
    Result<CombinedValue, CombinedError>
    combine(final Collection<Result<Value, Error>> results,
      final Function<List<Value>, CombinedValue> valueCombiner,
      final Function<List<Error>, CombinedError> errorCombiner) {
    // List all the errors and combine them if there are any.
    final var errors =
      results.stream().filter(Result::isFailure).map(Result::error).toList();
    if (!errors.isEmpty()) return Result.failure(errorCombiner.apply(errors));

    // If there are no errors, combine the values.
    return Result.success(
      valueCombiner.apply(results.stream().map(Result::value).toList()));
  }

  /**
   * Propagates the error to a new result.
   *
   * @param  <TargetValue> Type of the value that would have resulted on success
   *                         for the converted failed result.
   * @return               Failure with the same error as this one.
   */
  default <TargetValue> Result<TargetValue, Error> propagate() {
    return Result.failure(error());
  }

  /**
   * Maps the value if it exists.
   *
   * @param  <TargetValue> Type of the value that will be resulted on success
   *                         for the converted result.
   * @param  mapper        Function that converts the value.
   * @return               Converted value, or the same error.
   */
  default <TargetValue> Result<TargetValue, Error>
    map(Function<Value, TargetValue> mapper) {
    // If there is an error, propagate; otherwise, map the value.
    if (isFailure()) return propagate();
    return success(mapper.apply(value()));
  }

  /**
   * @return Whether the result is successful.
   */
  boolean isSuccess();

  /**
   * @return Whether the result is failed.
   */
  boolean isFailure();

  /**
   * @return Resulted value.
   */
  Value value();

  /**
   * @return Resulted error.
   */
  Error error();

  /**
   * Runs all the unit tests for {@link Result}.
   *
   * @param tester Used unit test runner.
   */
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
