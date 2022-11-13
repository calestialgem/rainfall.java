package rainfall.utility;

public final class Optional<Value> {
  private static final Optional EMPTY_INSTANCE = new Optional<>(null);

  private final Value value;
  private Optional(Value value) { this.value = value; }

  public static <Value> Optional<Value> empty() { return EMPTY_INSTANCE; }
  public static <Value> Optional<Value> full(Value value) {
    return new Optional<>(value);
  }

  public boolean isEmpty() { return value == null; }
  public boolean isFull() { return value != null; }
  public Value value() { return value; }

  @Override public String toString() {
    var buffer = new StringBuilder();
    buffer.append('[');
    if (isFull()) { buffer.append(value); }
    buffer.append(']');
    return buffer.toString();
  }
  @Override public int hashCode() {
    var hash = 1;
    if (isFull()) {
      hash *= 31;
      hash += value.hashCode();
    }
    return hash;
  }
  @Override public boolean equals(Object other) {
    if (this == other) { return true; }
    if (!(other instanceof Optional asOptional)) { return false; }
    if (isEmpty()) { return asOptional.isEmpty(); }
    if (asOptional.isEmpty()) { return false; }
    return value.equals(asOptional.value);
  }
}
