package rainfall.utility;

import java.util.function.BooleanSupplier;

public class Tester {
  private final double startTime;
  private int          runCount;
  private int          failedCount;

  public Tester() {
    runCount    = 0;
    failedCount = 0;
    startTime   = currentMilliseconds();
  }

  public void run(final BooleanSupplier test) {
    runCount++;

    final var unitStartTime = currentMilliseconds();
    final var result        = test.getAsBoolean();
    final var duration      = elapsedMilliseconds(unitStartTime);

    final var maxAcceptableDuration = 5.0;
    if (result && duration <= maxAcceptableDuration) return;

    if (!result) failedCount++;
    final var caller = Thread.currentThread().getStackTrace()[2];
    System.out.printf("[%s] %s:%d (%.3f ms)%n", result ? "TOO LONG" : "FAILED",
      caller.getFileName(), caller.getLineNumber(), duration);
  }

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

  private static double currentMilliseconds() {
    final var milli = 1e3;
    final var nano  = 1e9;
    return System.nanoTime() / nano * milli;
  }

  private static double elapsedMilliseconds(final double startTime) {
    final var endTime = currentMilliseconds();
    return endTime - startTime;
  }
}
