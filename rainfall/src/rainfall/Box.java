package rainfall;

public sealed interface Box<Value> {
  record Full<Value> (Value value) implements Box<Value> {
    @Override public boolean isFull() { return true; }

    @Override public boolean isEmpty() { return false; }

    @Override public Value get() { return value; }
  }

  record Empty<Value> () implements Box<Value> {
    @Override public boolean isFull() { return false; }

    @Override public boolean isEmpty() { return true; }

    @Override public Value get() {
      throw new RuntimeException("Box is empty!");
    }
  }

  static <Value> Box<Value> full(Value value) { return new Full<>(value); }

  static <Value> Box<Value> empty() { return new Empty<>(); }

  boolean isFull();
  boolean isEmpty();
  Value get();
}
