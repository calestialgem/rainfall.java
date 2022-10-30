package rainfall.utility;

import java.util.HashSet;
import java.util.Set;

public final class UnitTestRunner {
  private static final double MAX_ACCEPTABLE_UNIT_ELAPSED_MILLISECONDS = 5;

  private Set<Class<? extends UnitTest>> runTestClasses;
  private int                            failedTestCount;
  private double                         startMilliseconds;

  public UnitTestRunner() {
    runTestClasses    = new HashSet<>();
    failedTestCount   = 0;
    startMilliseconds = currentMilliseconds();
  }

  public void run(UnitTest runTest) {
    if (runTestClasses.contains(runTest.getClass())) {
      var caller = Thread.currentThread().getStackTrace()[2];
      System.out.printf("[REPEATED] %s:%d: %s%n", caller.getFileName(),
        caller.getLineNumber(), runTest.getClass().getSimpleName());
      return;
    }
    runTestClasses.add(runTest.getClass());

    var unitStartMilliseconds   = currentMilliseconds();
    var unitTestOutcome         = runTest.outcome();
    var unitElapsedMilliseconds = elapsedMilliseconds(unitStartMilliseconds);

    if (unitTestOutcome
      && unitElapsedMilliseconds < MAX_ACCEPTABLE_UNIT_ELAPSED_MILLISECONDS) {
      return;
    }

    var caller = Thread.currentThread().getStackTrace()[2];
    if (!unitTestOutcome) { failedTestCount++; }
    System.out.printf("[%s] %s:%d: %s (%.3f ms)%n",
      unitTestOutcome ? "TOO LONG" : "FAILED", caller.getFileName(),
      caller.getLineNumber(), runTest.getClass().getSimpleName(),
      unitElapsedMilliseconds);
  }

  public boolean report() {
    var totalElapsedMilliseconds = elapsedMilliseconds(startMilliseconds);

    if (runTestClasses.size() == 0) {
      System.out.printf("No tests were run.");
    } else if (failedTestCount == 0) {
      System.out.printf("%d tests passed.", runTestClasses.size());
    } else {
      System.out.printf("%d/%d tests failed!", failedTestCount,
        runTestClasses.size());
    }

    System.out.printf(" (%.3f ms)%n", totalElapsedMilliseconds);

    return failedTestCount == 0;
  }

  private static double elapsedMilliseconds(double startMilliseconds) {
    return currentMilliseconds() - startMilliseconds;
  }

  private static double currentMilliseconds() {
    return System.nanoTime() * 1e-6;
  }
}
