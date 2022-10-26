package rainfall;

import java.util.function.Function;

public sealed interface Result<Value, Error> {
  record Success<Value, Error> (Value value) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return true; }

    @Override public boolean isFailure() { return false; }

    @Override public Value get() { return value; }

    @Override public Error getError() {
      throw new RuntimeException("Result is success!");
    }

    @Override public <Target> Result<Target, Error>
        map(Function<Value, Target> mapper) {
      return success(mapper.apply(value));
    }
  }

  record Failure<Value, Error> (Error error) implements Result<Value, Error> {
    @Override public boolean isSuccess() { return false; }

    @Override public boolean isFailure() { return true; }

    @Override public Value get() {
      throw new RuntimeException("Result is failure!");
    }

    @Override public Error getError() { return error; }

    @Override public <Target> Result<Target, Error>
        map(Function<Value, Target> mapper) {
      return failure(error);
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
  Value get();
  Error getError();
  <Target> Result<Target, Error> map(Function<Value, Target> mapper);

  default <Target> Result<Target, Error> propagate() {
    return failure(getError());
  }
}
