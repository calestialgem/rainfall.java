package rainfall.utility;

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

  static void test(final Tester tester) {
    Success.test(tester);
    Failure.test(tester);
    final var success = success(1);
    final var failure = failure(1);
    tester.run(success::isSuccess);
    tester.run(failure::isFailure);
  }

  boolean isSuccess();
  boolean isFailure();
  Value value();
  Error error();
}
