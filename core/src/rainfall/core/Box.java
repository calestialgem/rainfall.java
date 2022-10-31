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

  static <Value> Box<Value> full(Value value) { return new Full<>(value); }
  static <Value> Box<Value> empty() { return new Empty<>(); }

  boolean isFull();
  boolean isEmpty();
  Value value();

  public static void test(final Tester tester) {
    class FullIsFull implements Test {
      @Override public boolean outcome() { return new Full<>(1).isFull(); }
    }
    class FullIsNotEmpty implements Test {
      @Override public boolean outcome() { return !new Full<>(1).isEmpty(); }
    }
    class EmptyIsNotFull implements Test {
      @Override public boolean outcome() { return !new Empty<>().isFull(); }
    }
    class EmptyIsEmpty implements Test {
      @Override public boolean outcome() { return new Empty<>().isEmpty(); }
    }
    class CreatedFullHasTheGivenValue implements Test {
      @Override public boolean outcome() {
        final var value = 1;
        return full(value).value().equals(value);
      }
    }
    class CreatedEmptyIsEmpty implements Test {
      @Override public boolean outcome() { return empty().isEmpty(); }
    }

    tester.run(new FullIsFull());
    tester.run(new FullIsNotEmpty());
    tester.run(new EmptyIsNotFull());
    tester.run(new EmptyIsEmpty());
    tester.run(new CreatedFullHasTheGivenValue());
    tester.run(new CreatedEmptyIsEmpty());
  }
}
