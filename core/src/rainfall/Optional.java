package rainfall;

public sealed abstract class Optional<Value> {
  public static <Value> Optional<Value> present(final Value value) {
    return new Present<>(value);
  }
  public static <Value> Optional<Value> absent() { return new Absent<>(); }

  public abstract boolean isPresent();
  public abstract boolean isAbsent();
  public abstract Value value();

  private static final class Present<Value> extends Optional<Value> {
    private final Value value;
    private Present(final Value value) { this.value = value; }

    @Override public boolean isPresent() { return true; }
    @Override public boolean isAbsent() { return false; }
    @Override public Value value() { return value; }

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
      return hash;
    }
    @Override public boolean equals(final Object obj) {
      if (this == obj) { return true; }
      if (!(obj instanceof final Present asPresent)) { return false; }
      return value.equals(asPresent.value);
    }
  }
  private static final class Absent<Value> extends Optional<Value> {
    @Override public boolean isPresent() { return false; }
    @Override public boolean isAbsent() { return true; }
    @Override public Value value() {
      throw new UnsupportedOperationException("Optional is absent!");
    }

    @Override public String toString() { return "[]"; }
    @Override public int hashCode() {
      var hash = 1;
      hash *= 31;
      hash += 0;
      return hash;
    }
    @Override public boolean equals(final Object obj) {
      if (this == obj) { return true; }
      if (!(obj instanceof Absent)) { return false; }
      return true;
    }
  }
}
