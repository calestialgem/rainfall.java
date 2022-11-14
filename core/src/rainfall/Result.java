package rainfall;

public sealed abstract class Result<Value, Error> {
  public static <Value, Error> Result<Value, Error> success(final Value value) {
    return new Success<>(value);
  }
  public static <Value, Error> Result<Value, Error> failure(final Error error) {
    return new Failure<>(error);
  }

  public abstract boolean isSuccess();
  public abstract boolean isFailure();
  public abstract Value value();
  public abstract Error error();

  private static final class Success<Value, Error>
    extends Result<Value, Error> {
    private final Value value;
    private Success(final Value value) { this.value = value; }

    @Override public boolean isSuccess() { return true; }
    @Override public boolean isFailure() { return false; }
    @Override public Value value() { return value; }
    @Override public Error error() {
      throw new UnsupportedOperationException("Result is success!");
    }

    @Override public String toString() {
      final var builder = new StringBuilder();
      builder.append('[');
      builder.append(value);
      builder.append(']');
      return builder.toString();
    }
    @Override public int hashCode() {
      var hash = 1;
      hash *= 31;
      hash += value.hashCode();
      hash *= 31;
      hash += 0;
      return hash;
    }
    @Override public boolean equals(final Object obj) {
      if (this == obj) { return true; }
      if (!(obj instanceof final Success asSuccess)) { return false; }
      return value.equals(asSuccess.value);
    }
  }
  private static final class Failure<Value, Error>
    extends Result<Value, Error> {
    private final Error error;
    private Failure(final Error error) { this.error = error; }

    @Override public boolean isSuccess() { return false; }
    @Override public boolean isFailure() { return true; }
    @Override public Value value() {
      throw new UnsupportedOperationException("Result is failure!");
    }
    @Override public Error error() { return error; }

    @Override public String toString() {
      final var builder = new StringBuilder();
      builder.append("[");
      builder.append(error);
      builder.append(']');
      return builder.toString();
    }
    @Override public int hashCode() {
      var hash = 1;
      hash *= 31;
      hash += 0;
      hash *= 31;
      hash += error.hashCode();
      return hash;
    }
    @Override public boolean equals(final Object obj) {
      if (this == obj) { return true; }
      if (!(obj instanceof final Failure asFailure)) { return false; }
      return error.equals(asFailure.error);
    }
  }
}
