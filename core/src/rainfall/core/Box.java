package rainfall.core;

public sealed interface Box<Value> {
  record Full<Value>(Value value) implements Box<Value> {
    @Override public boolean isFull() { return true; }
    @Override public boolean isEmpty() { return false; }
  }
  record Empty<Value>() implements Box<Value> {
    @Override public boolean isFull() { return false; }
    @Override public boolean isEmpty() { return true; }
    @Override public Value value() {
      throw new UnsupportedOperationException("Box is empty!");
    }
  }

  static <Value> Box<Value> full(final Value value) {
    return new Full<>(value);
  }
  static <Value> Box<Value> empty() { return new Empty<>(); }

  boolean isFull();
  boolean isEmpty();
  Value value();

  final class TestSuite {
    public static boolean fullIsFull() { return new Full<>(1).isFull(); }
    public static boolean fullIsNotEmpty() { return !new Full<>(1).isEmpty(); }
    public static boolean emptyIsNotFull() { return !new Empty<>().isFull(); }
    public static boolean emptyIsEmpty() { return new Empty<>().isEmpty(); }
    public static boolean createdFullHasTheGivenValue() {
      final var value = 1;
      return full(value).value().equals(value);
    }
    public static boolean createdEmptyIsEmpty() { return empty().isEmpty(); }
  }
}
