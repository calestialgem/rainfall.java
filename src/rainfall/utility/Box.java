package rainfall.utility;

/**
 * Container that can at most have one element in it.
 *
 * @param  <Held> Type of the contained element.
 * @author        calestialgem
 */
public sealed interface Box<Held> {
  /**
   * {@link Box} that contains an element.
   *
   * @param  <Held> Type of the contained element.
   * @param  value  Contained element.
   * @author        calestialgem
   */
  record Full<Held>(Held value) implements Box<Held> {
    @Override public boolean isFull() { return true; }
    @Override public boolean isEmpty() { return false; }
    @Override public Held get() { return value; }

    /**
     * Runs all the unit tests for {@link Full}.
     *
     * @param tester Used unit test runner.
     */
    private static void test(final Tester tester) {
      final var boxed = 1;
      final var box   = new Full<>(boxed);
      tester.run(box::isFull);
      tester.run(() -> !box.isEmpty());
      tester.run(() -> box.get() == boxed);
    }
  }

  /**
   * {@link Box} that is empty.
   *
   * @param  <Held> Type of the value that the box would have contained if there
   *                  were a value.
   * @author        calestialgem
   */
  record Empty<Held>() implements Box<Held> {
    @Override public boolean isFull() { return false; }
    @Override public boolean isEmpty() { return true; }
    @Override public Held get() { throw new RuntimeException("Box is empty!"); }

    /**
     * Runs all the unit tests for {@link Empty}.
     *
     * @param tester Used unit test runner.
     */
    private static void test(final Tester tester) {
      final var box = new Empty<>();
      tester.run(() -> !box.isFull());
      tester.run(box::isEmpty);
    }
  }

  /**
   * Creates a full box.
   *
   * @param  <Held> Type of the value contained in the created box.
   * @param  value  Value contained in the created box.
   * @return        New full box.
   */
  static <Held> Box<Held> full(final Held value) {
    return new Full<>(value);
  }

  /**
   * Creates an empty box.
   *
   * @param  <Held> Type of the value that the box would have contained if there
   *                  were a value.
   * @return        New empty box.
   */
  static <Held> Box<Held> empty() { return new Empty<>(); }

  /**
   * @return Whether there is a value.
   */
  boolean isFull();

  /**
   * @return Whether there is not a value.
   */
  boolean isEmpty();

  /**
   * @return Contained value.
   */
  Held get();

  /**
   * runs all the unit tests for {@link Box}.
   *
   * @param tester Used unit test runner.
   */
  static void test(final Tester tester) {
    Full.test(tester);
    Empty.test(tester);
    final var full  = Box.full(1);
    final var empty = Box.empty();
    tester.run(full::isFull);
    tester.run(empty::isEmpty);
  }
}
