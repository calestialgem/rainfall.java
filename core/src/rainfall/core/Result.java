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

  static <Value, Error> Result<Value, Error> success(Value value) {
    return new Success<>(value);
  }
  static <Value, Error> Result<Value, Error> failure(Error error) {
    return new Failure<>(error);
  }

  boolean isSuccess();
  boolean isFailure();
  Value value();
  Error error();

  public static void test(final Tester tester) {
    class SuccessIsSuccess implements Test {
      @Override public boolean outcome() {
        return new Success<>(1).isSuccess();
      }
    }
    class SuccessIsNotFailure implements Test {
      @Override public boolean outcome() {
        return !new Success<>(1).isFailure();
      }
    }
    class FailureIsNotSuccess implements Test {
      @Override public boolean outcome() {
        return !new Failure<>(1).isSuccess();
      }
    }
    class FailureIsFailure implements Test {
      @Override public boolean outcome() {
        return new Failure<>(1).isFailure();
      }
    }
    class CreatedSuccessHasTheGivenValue implements Test {
      @Override public boolean outcome() {
        final var value = 1;
        return success(value).value().equals(value);
      }
    }
    class CreatedFailureHasTheGivenError implements Test {
      @Override public boolean outcome() {
        final var error = 1;
        return failure(error).error().equals(error);
      }
    }

    tester.run(new SuccessIsSuccess());
    tester.run(new SuccessIsNotFailure());
    tester.run(new FailureIsNotSuccess());
    tester.run(new FailureIsFailure());
    tester.run(new CreatedSuccessHasTheGivenValue());
    tester.run(new CreatedFailureHasTheGivenError());
  }
}
