package rainfall.core;

public sealed interface Result<Value, Error> {
  record Success<Value, Error>(Value value) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return true; }
    @Override public boolean isFailure() { return false; }
    @Override public Error error() {
      throw new UnsupportedOperationException("Result is success!");
    }
  }
  record Failure<Value, Error>(Error error) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return false; }
    @Override public boolean isFailure() { return true; }
    @Override public Value value() {
      throw new UnsupportedOperationException("Result is failure!");
    }
  }

  static <Value, Error> Result<Value, Error> success(final Value value) {
    return new Success<>(value);
  }
  static <Value, Error> Result<Value, Error> failure(final Error error) {
    return new Failure<>(error);
  }

  boolean isSuccess();
  boolean isFailure();
  Value value();
  Error error();

  final class TestSuite {
    public static boolean successIsSuccess() {
      return new Success<>(1).isSuccess();
    }
    public static boolean successIsNotFailure() {
      return !new Success<>(1).isFailure();
    }
    public static boolean failureIsNotSuccess() {
      return !new Failure<>(1).isSuccess();
    }
    public static boolean failureIsFailure() {
      return new Failure<>(1).isFailure();
    }
    public static boolean createdSuccessHasTheGivenValue() {
      final var value = 1;
      return success(value).value().equals(value);
    }
    public static boolean createdFailureHasTheGivenError() {
      final var error = 1;
      return failure(error).error().equals(error);
    }
  }
}
