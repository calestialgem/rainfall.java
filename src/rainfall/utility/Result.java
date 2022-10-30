package rainfall.utility;

public sealed interface Result<Value, Error> {
  record Success<Value, Error>(Value value) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return true; }
    @Override public boolean isFailure() { return false; }
    @Override public Error error() {
      throw new RuntimeException("Result is success!");
    }
  }
  record Failure<Value, Error>(Error error) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return false; }
    @Override public boolean isFailure() { return true; }
    @Override public Value value() {
      throw new RuntimeException("Result is failure!");
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

  static void test(UnitTestRunner runner) {
    class SuccessIsSuccess implements UnitTest {
      @Override public boolean outcome() {
        return new Success<>(1).isSuccess();
      }
    }

    class SuccessIsNotFailure implements UnitTest {
      @Override public boolean outcome() {
        return !new Success<>(1).isFailure();
      }
    }

    class FailureIsNotSuccess implements UnitTest {
      @Override public boolean outcome() {
        return !new Failure<>(1).isSuccess();
      }
    }

    class FailureIsFailure implements UnitTest {
      @Override public boolean outcome() {
        return new Failure<>(1).isFailure();
      }
    }

    class CreatedSuccessHasTheGivenValue implements UnitTest {
      @Override public boolean outcome() {
        var value = 1;
        return success(value).value().equals(value);
      }
    }

    class CreatedFailureHasTheGivenError implements UnitTest {
      @Override public boolean outcome() {
        var error = 1;
        return failure(error).error().equals(error);
      }
    }

    runner.run(new SuccessIsSuccess());
    runner.run(new SuccessIsNotFailure());
    runner.run(new FailureIsNotSuccess());
    runner.run(new FailureIsFailure());
    runner.run(new CreatedSuccessHasTheGivenValue());
    runner.run(new CreatedFailureHasTheGivenError());
  }
}
