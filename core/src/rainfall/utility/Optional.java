package rainfall.utility;

public sealed abstract class Optional<Value> {
  public static <Value> Optional<Value> present(Value value) {
    return new Present<>(value);
  }
  public static <Value> Optional<Value> absent() { return Absent.INSTANCE; }

  public abstract boolean isAbsent();
  public abstract boolean isPresent();
  public abstract Value value();

  private static final class Present<Value> extends Optional<Value> {
    private final Value value;
    private Present(Value value) { this.value = value; }

    @Override public boolean isAbsent() { return false; }
    @Override public boolean isPresent() { return true; }
    @Override public Value value() { return value; }

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
      return hash;
    }
    @Override public boolean equals(Object other) {
      if (this == other) { return true; }
      if (!(other instanceof Present asPresent)) { return false; }
      return value.equals(asPresent.value);
    }
  }
  private static final class Absent<Value> extends Optional<Value> {
    private static final Absent INSTANCE = new Absent<>();

    @Override public boolean isAbsent() { return true; }
    @Override public boolean isPresent() { return false; }
    @Override public Value value() { return null; }

    @Override public String toString() { return "[]"; }
    @Override public int hashCode() { return 31; }
    @Override public boolean equals(Object other) { return this == other; }
  }
}
