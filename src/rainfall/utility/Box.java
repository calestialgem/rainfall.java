package rainfall.utility;

public sealed interface Box<Value> {
  record Full<Value>(Value value) implements Box<Value> {
    @Override public boolean isFull() { return true; }
    @Override public boolean isEmpty() { return false; }
  }

  record Empty<Value>() implements Box<Value> {
    @Override public boolean isFull() { return false; }
    @Override public boolean isEmpty() { return true; }
    @Override public Value value() {
      throw new RuntimeException("Box is empty!");
    }
  }

  static <Value> Box<Value> full(Value value) { return new Full<>(value); }
  static <Value> Box<Value> empty() { return new Empty<>(); }

  boolean isFull();
  boolean isEmpty();
  Value value();

  static void test(UnitTestRunner runner) {
    class FullIsFull implements UnitTest {
      @Override public boolean outcome() { return new Full<>(1).isFull(); }
    }

    class FullIsNotEmpty implements UnitTest {
      @Override public boolean outcome() { return !new Full<>(1).isEmpty(); }
    }

    class EmptyIsNotFull implements UnitTest {
      @Override public boolean outcome() { return !new Empty<>().isFull(); }
    }

    class EmptyIsEmpty implements UnitTest {
      @Override public boolean outcome() { return new Empty<>().isEmpty(); }
    }

    class CreatedFullHasTheGivenValue implements UnitTest {
      @Override public boolean outcome() {
        var value = 1;
        return full(value).value().equals(value);
      }
    }

    class CreatedEmptyIsEmpty implements UnitTest {
      @Override public boolean outcome() { return empty().isEmpty(); }
    }

    runner.run(new FullIsFull());
    runner.run(new FullIsNotEmpty());
    runner.run(new EmptyIsNotFull());
    runner.run(new EmptyIsEmpty());
    runner.run(new CreatedFullHasTheGivenValue());
    runner.run(new CreatedEmptyIsEmpty());
  }
}
