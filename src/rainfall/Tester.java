package rainfall;

import java.util.function.BooleanSupplier;

/**
 * Runs and reports the results of the unit tests.
 *
 * @author calestialgem
 */
class Tester {
  /**
   * Point in time the tester is created. Used for measuring the total time
   * taken by all the run tests.
   */
  private final double startTime;

  /**
   * Number of run unit tests.
   */
  private int runCount;

  /**
   * Number of failed unit tests.
   */
  private int failedCount;

  /**
   * Constructs a clean tester, which immediately starts measuring the time.
   */
  Tester() {
    runCount    = 0;
    failedCount = 0;
    startTime   = currentMilliseconds();
  }

  /**
   * Runs the given unit test with the given identifier. If the test fails or
   * takes too much time, reports it.
   *
   * @param test       Callable to the unit test that will be run.
   * @param identifier String that will be reported to identify the test.
   */
  void run(final BooleanSupplier test, final String identifier) {
    runCount++;

    final var unitStartTime = currentMilliseconds();
    final var result        = test.getAsBoolean();
    final var duration      = elapsedMilliseconds(unitStartTime);

    final var maxAcceptableDuration = 5.0;
    if (result && duration <= maxAcceptableDuration) return;

    if (!result) failedCount++;
    System.out.printf("[%s] %s (%.3f ms)%n", result ? "TOO LONG" : "FAILED",
      identifier, duration);
  }

  /**
   * Reports the results of all the unit tests.
   *
   * @return Whether all the unit tests passed.
   */
  boolean report() {
    final var totalDuration = elapsedMilliseconds(startTime);

    if (runCount == 0) System.out.printf("There are no tests!");
    else if (failedCount == 0) System.out.printf("All tests passed.");
    else System.out.printf("%i/%i of the tests failed!", failedCount, runCount);
    System.out.printf(" (%.3f ms)%n", totalDuration);

    return failedCount == 0;
  }

  /**
   * @return Current time of a relative clock in milliseconds.
   */
  private static double currentMilliseconds() {
    final var milli = 1e3;
    final var nano  = 1e9;
    return System.nanoTime() / nano * milli;
  }

  /**
   * @param  startTime Point in time that the measurement starts at. Must be a
   *                     time point received from
   *                     {@link Tester#currentMilliseconds}.
   * @return           Elapsed time in milliseconds.
   */
  private static double elapsedMilliseconds(final double startTime) {
    final var endTime = currentMilliseconds();
    return endTime - startTime;
  }
}
