package rainfall.core;

import java.util.HashSet;
import java.util.Set;

public final class Tester {
  private final Set<Class<? extends Test>> runTestClasses;
  private final double                     startMilliseconds;
  private int                              failedTestCount;

  public Tester() {
    runTestClasses    = new HashSet<>();
    startMilliseconds = currentMilliseconds();
    failedTestCount   = 0;
  }

  public void run(final Test runTest) {
    if (runTestClasses.contains(runTest.getClass())) {
      System.out.printf("[REPEATED] %s%n", testInformation(runTest));
      return;
    }

    final var runTestStartMilliseconds = currentMilliseconds();
    final var runTestOutcome           = runTest.outcome();
    final var runTestDuration          =
      elapsedMilliseconds(runTestStartMilliseconds);

    final var maxAcceptableTestDuration = 5;
    if (runTestOutcome && runTestDuration < maxAcceptableTestDuration) {
      return;
    }

    if (!runTestOutcome) { failedTestCount++; }
    System.out.printf("[%s] %s (%.3f ms)%n",
      runTestOutcome ? "TOO LONG" : "FAILED", testInformation(runTest),
      runTestDuration);
  }

  public boolean report() {
    if (runTestClasses.isEmpty()) {
      System.out.printf("No tests were run.");
    } else if (failedTestCount == 0) {
      System.out.printf("%d tests passed.", runTestClasses.size());
    } else {
      System.out.printf("%d/%d of the tests failed!", failedTestCount,
        runTestClasses.size());
    }
    System.out.printf(" (%.3f ms)%n", elapsedMilliseconds(startMilliseconds));
    return failedTestCount == 0;
  }

  private static String testInformation(Test test) {
    final var callerTrace = Thread.currentThread().getStackTrace()[3];
    return "%s:%d: %s".formatted(callerTrace.getFileName(),
      callerTrace.getLineNumber(), test.getClass().getSimpleName());
  }

  private static double elapsedMilliseconds(final double previousMilliseconds) {
    return currentMilliseconds() - previousMilliseconds;
  }

  private static double currentMilliseconds() {
    final var milliScale        = 1e3;
    final var nanoScale         = 1e9;
    final var nanoToMilliFactor = milliScale / nanoScale;
    return System.nanoTime() * nanoToMilliFactor;
  }
}
