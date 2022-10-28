package rainfall.utility;

public sealed interface Box<Held> {
  record Full<Held>(Held value) implements Box<Held> {
    @Override public boolean isFull() { return true; }
    @Override public boolean isEmpty() { return false; }
    @Override public Held get() { return value; }

    private static void test(Tester tester) {
      final var boxed = 1;
      final var box   = new Full<>(boxed);
      tester.run(box::isFull);
      tester.run(() -> !box.isEmpty());
      tester.run(() -> box.get() == boxed);
    }
  }

  record Empty<Held>() implements Box<Held> {
    @Override public boolean isFull() { return false; }
    @Override public boolean isEmpty() { return true; }
    @Override public Held get() { throw new RuntimeException("Box is empty!"); }

    private static void test(Tester tester) {
      final var box = new Empty<>();
      tester.run(() -> !box.isFull());
      tester.run(box::isEmpty);
    }
  }

  static <Held> Box<Held> full(Held value) { return new Full<>(value); }
  static <Held> Box<Held> empty() { return new Empty<>(); }

  boolean isFull();
  boolean isEmpty();
  Held get();

  static void test(Tester tester) {
    Full.test(tester);
    Empty.test(tester);
    final var full  = Box.full(1);
    final var empty = Box.empty();
    tester.run(full::isFull);
    tester.run(empty::isEmpty);
  }
}
