package rainfall.utility;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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
   * Combines multiple boxed values to a single boxed value.
   *
   * @param  <Combined> Type that is the result of combining the values.
   * @param  <Held>     Type that is held in the full boxes.
   * @param  boxes      Combined boxes.
   * @param  combiner   Function that combines values.
   * @return            Combined values, or nothing.
   */
  static <Combined, Held> Box<Combined> combine(
    final Collection<Box<Held>> boxes,
    final Function<List<Held>, Combined> combiner) {
    final var held = boxes.stream().filter(Box::isFull).map(Box::get).toList();
    if (held.isEmpty()) return Box.empty();
    return Box.full(combiner.apply(held));
  }

  /**
   * Creates a box, which is full depending on a condition.
   *
   * @param  <Held>     The type that could be held in the created box.
   * @param  shouldFill Whether the box should be filled.
   * @param  filler     Function that supplies an object that will fill the box.
   * @return            Box filled with a value from the filler if the condition
   *                      is true, or nothing.
   */
  static <Held> Box<Held> fillIf(boolean shouldFill, Supplier<Held> filler) {
    return shouldFill ? Box.full(filler.get()) : Box.empty();
  }

  /**
   * Inserts a box, which is full depending on a condition, in to the list.
   *
   * @param  <Held>     The type that could be held in the created box.
   * @param  shouldFill Whether the box should be filled.
   * @param  boxes      List that the box is inserted to.
   * @param  filler     Function that supplies an object that will fill the box.
   * @return            Whether the box was filled.
   */
  static <Held> boolean fillIf(boolean shouldFill, List<Box<Held>> boxes,
    Supplier<Held> filler) {
    boxes.add(fillIf(shouldFill, filler));
    return shouldFill;
  }

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
