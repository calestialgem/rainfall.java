package rainfall.utility;

public final class UnitTestRunner {
  private static final double MAX_ACCEPTABLE_UNIT_ELAPSED_MILLISECONDS = 5;

  private int    runTestCount;
  private int    failedTestCount;
  private double startMilliseconds;

  public UnitTestRunner() {
    runTestCount      = 0;
    failedTestCount   = 0;
    startMilliseconds = Timer.currentMilliseconds();
  }

  public void run(UnitTest runTest) {
    runTestCount++;

    var unitStartMilliseconds   = Timer.currentMilliseconds();
    var unitTestOutcome         = runTest.outcome();
    var unitElapsedMilliseconds =
      Timer.elapsedMilliseconds(unitStartMilliseconds);

    if (unitTestOutcome
      && unitElapsedMilliseconds < MAX_ACCEPTABLE_UNIT_ELAPSED_MILLISECONDS) {
      return;
    }

    if (!unitTestOutcome) { failedTestCount++; }
    System.out.printf("[%s] %s (%.3f ms)%n",
      unitTestOutcome ? "TOO LONG" : "FAILED", runTest.name(),
      unitElapsedMilliseconds);
  }

  public boolean report() {
    var totalElapsedMilliseconds = Timer.elapsedMilliseconds(startMilliseconds);

    if (runTestCount == 0) {
      System.out.printf("No tests were run.");
    } else if (failedTestCount == 0) {
      System.out.printf("%d tests passed.", runTestCount);
    } else {
      System.out.printf("%d/%d tests failed!", failedTestCount, runTestCount);
    }

    System.out.printf(" (%.3f ms)%n", totalElapsedMilliseconds);

    return failedTestCount == 0;
  }
}
