package rainfall;

public sealed abstract class Box<Value> {
  public static <Value> Box<Value> empty() { return new Empty<>(); }
  public static <Value> Box<Value> full(Value value) {
    return new Full<>(value);
  }

  public abstract boolean isEmpty();
  public abstract boolean isFull();
  public abstract Value value();

  private static final class Empty<Value> extends Box<Value> {
    @Override public boolean isEmpty() { return true; }
    @Override public boolean isFull() { return false; }
    @Override public Value value() {
      throw new UnsupportedOperationException("Box is empty!");
    }
  }

  private static final class Full<Value> extends Box<Value> {
    private final Value value;
    private Full(Value value) { this.value = value; }

    @Override public boolean isEmpty() { return false; }
    @Override public boolean isFull() { return true; }
    @Override public Value value() { return value; }
  }
}
