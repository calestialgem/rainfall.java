package rainfall.utility;

public sealed abstract class Result<Value, Error> {
  public static <Value, Error> Result<Value, Error> failure(Error error) {
    return new Failure<>(error);
  }
  public static <Value, Error> Result<Value, Error> success(Value value) {
    return new Success<>(value);
  }

  public abstract boolean isFailure();
  public abstract boolean isSuccess();
  public abstract Error error();
  public abstract Value value();

  private static final class Failure<Value, Error>
    extends Result<Value, Error> {
    private final Error error;
    private Failure(Error error) { this.error = error; }

    @Override public boolean isFailure() { return true; }
    @Override public boolean isSuccess() { return false; }
    @Override public Error error() { return error; }
    @Override public Value value() {
      throw new UnsupportedOperationException("Result is failure!");
    }
  }

  private static final class Success<Value, Error>
    extends Result<Value, Error> {
    private final Value value;
    private Success(Value value) { this.value = value; }

    @Override public boolean isFailure() { return false; }
    @Override public boolean isSuccess() { return true; }
    @Override public Error error() {
      throw new UnsupportedOperationException("Result is success!");
    }
    @Override public Value value() { return value; }
  }
}
