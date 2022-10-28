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
      // Test whether a full box is actually full.
      tester.run(() -> new Full<>(1).isFull());

      // Test whether a full box is actually not empty.
      tester.run(() -> !new Full<>(1).isEmpty());

      // Test accessing the contained value.
      tester.run(() -> {
        final var boxed = 1;
        return new Full<>(boxed).get() == boxed;
      });
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
      // Test whether an empty box is actually not full.
      tester.run(() -> !new Empty<>().isFull());

      // Test whether an empty box is actually empty.
      tester.run(() -> new Empty<>().isEmpty());
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
    // Run sub-test suites.
    Full.test(tester);
    Empty.test(tester);

    // Test whether a created full box is actually full.
    tester.run(() -> full(1).isFull());

    // Test whether a created empty box is actually empty.
    tester.run(() -> empty().isEmpty());
  }
}
