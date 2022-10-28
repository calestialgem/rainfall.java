package rainfall.utility;

import java.util.function.BooleanSupplier;

/**
 * Runs and reports unit tests. Measures time taken by the tests.
 *
 * @author calestialgem
 */
public class Tester {
  /**
   * Time of creation in milliseconds. User for measuring the total elapsed
   * time.
   */
  private final double startTime;

  /**
   * Number of unit tests that were run.
   */
  private int runCount;

  /**
   * Number of unit tests that failed.
   */
  private int failedCount;

  /**
   * Constructs a clean tester, which immediately starts measuring the time.
   * Thus, it should be used to run all the tests that would be run just after
   * creation.
   */
  public Tester() {
    runCount    = 0;
    failedCount = 0;
    startTime   = currentMilliseconds();
  }

  /**
   * Runs the given unit test and report it if it fails or takes too much time.
   *
   * @param test Run unit test.
   */
  public void run(final BooleanSupplier test) {
    // Count the run.
    runCount++;

    // Run the test, while measuring the time.
    final var unitStartTime = currentMilliseconds();
    final var result        = test.getAsBoolean();
    final var duration      = elapsedMilliseconds(unitStartTime);

    // Return if it passed in an acceptable time.
    final var maxAcceptableDuration = 5.0;
    if (result && duration <= maxAcceptableDuration) return;

    // Count the failed test and report.
    if (!result) failedCount++;
    final var caller = Thread.currentThread().getStackTrace()[2];
    System.out.printf("[%s] %s:%d (%.3f ms)%n", result ? "TOO LONG" : "FAILED",
      caller.getFileName(), caller.getLineNumber(), duration);
  }

  /**
   * Reports the test results and the total time taken for all the tests.
   *
   * @return Whether all tests passed.
   */
  public boolean report() {
    final var totalDuration = elapsedMilliseconds(startTime);

    if (runCount == 0) System.out.printf("There are no tests!");
    else
      if (failedCount == 0) System.out.printf("All %d tests passed.", runCount);
      else
        System.out.printf("%d/%d of the tests failed!", failedCount, runCount);
    System.out.printf(" (%.3f ms)%n", totalDuration);

    return failedCount == 0;
  }

  /**
   * @return Current relative time in milliseconds.
   */
  private static double currentMilliseconds() {
    final var milli = 1e3;
    final var nano  = 1e9;
    return System.nanoTime() / nano * milli;
  }

  /**
   * Measures the elapsed time.
   *
   * @param  startTime Relative time at the start of the measurement.
   * @return           Elapsed time from the start up to now.
   */
  private static double elapsedMilliseconds(final double startTime) {
    final var endTime = currentMilliseconds();
    return endTime - startTime;
  }
}
