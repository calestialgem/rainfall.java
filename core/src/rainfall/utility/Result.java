package rainfall.utility;

public sealed abstract class Result<Value, Error> {
  public static <Value, Error> Result<Value, Error> success(Value value) {
    return new Success<>(value);
  }
  public static <Value, Error> Result<Value, Error> failure(Error error) {
    return new Failure<>(error);
  }

  public abstract boolean isFailure();
  public abstract boolean isSuccess();
  public abstract Value value();
  public abstract Error error();

  private static final class Success<Value, Error>
    extends Result<Value, Error> {
    private final Value value;
    private Success(Value value) { this.value = value; }

    @Override public boolean isFailure() { return false; }
    @Override public boolean isSuccess() { return true; }
    @Override public Value value() { return value; }
    @Override public Error error() { return null; }

    @Override public String toString() {
      var buffer = new StringBuilder();
      buffer.append('[');
      buffer.append(value);
      buffer.append(']');
      return buffer.toString();
    }
    @Override public int hashCode() {
      var hash = 1;
      hash *= 31;
      hash += value.hashCode();
      hash *= 31;
      return hash;
    }
    @Override public boolean equals(Object other) {
      if (this == other) { return true; }
      if (!(other instanceof Success asSuccess)) { return false; }
      return value.equals(asSuccess.value);
    }
  }
  private static final class Failure<Value, Error>
    extends Result<Value, Error> {
    private final Error error;
    private Failure(Error error) { this.error = error; }

    @Override public boolean isFailure() { return true; }
    @Override public boolean isSuccess() { return false; }
    @Override public Value value() { return null; }
    @Override public Error error() { return error; }

    @Override public String toString() {
      var buffer = new StringBuilder();
      buffer.append('[');
      buffer.append(error);
      buffer.append(']');
      return buffer.toString();
    }
    @Override public int hashCode() {
      var hash = 1;
      hash *= 31;
      hash *= 31;
      hash += error.hashCode();
      return hash;
    }
    @Override public boolean equals(Object other) {
      if (this == other) { return true; }
      if (!(other instanceof Failure asFailure)) { return false; }
      return error.equals(asFailure.error);
    }
  }
}
