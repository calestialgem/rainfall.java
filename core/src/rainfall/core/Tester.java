package rainfall.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public final class Tester {
  private final Set<Class<?>> runTestSuiteClasses;
  private final double        startMilliseconds;

  private int failedTestCount;
  private int runTestCount;

  public Tester() {
    runTestSuiteClasses = new HashSet<>();
    startMilliseconds   = currentMilliseconds();
    failedTestCount     = 0;
    runTestCount        = 0;
  }

  public void run(final Class<?> runSuiteClass)
    throws IllegalAccessException, InvocationTargetException {
    if (runTestSuiteClasses.contains(runSuiteClass)) {
      System.out.printf("Repeated Test Suite: %s(%s)%n",
        runSuiteClass.getName(), callerInformation());
      return;
    }
    runTestSuiteClasses.add(runSuiteClass);

    for (final var method : runSuiteClass.getMethods()) {
      if (!method.getReturnType().equals(boolean.class)
        || method.getParameterTypes().length != 0
        || !Modifier.isStatic(method.getModifiers())) {
        continue;
      }

      final var runTestStartMilliseconds = currentMilliseconds();
      final var runTestOutcome           = (boolean) method.invoke(null);
      final var runTestDuration          =
        elapsedMilliseconds(runTestStartMilliseconds);
      runTestCount++;

      final var maxAcceptableTestDuration = 10;
      if (runTestOutcome && runTestDuration < maxAcceptableTestDuration) {
        continue;
      }

      if (!runTestOutcome) { failedTestCount++; }
      System.out.printf("%s Unit Test: %s.%s(%s) (%.3f ms)%n",
        runTestOutcome ? "Too Long" : "Failed", runSuiteClass.getName(),
        method.getName(), callerInformation(), runTestDuration);
    }
  }

  public boolean report() {
    if (runTestCount == 0) {
      System.out.printf("No tests were run.");
    } else if (failedTestCount == 0) {
      System.out.printf("All of the %d tests passed.", runTestCount);
    } else {
      System.out.printf("%d/%d of the tests failed!", failedTestCount,
        runTestCount);
    }
    System.out.printf(" (%.3f ms)%n", elapsedMilliseconds(startMilliseconds));
    return failedTestCount == 0;
  }

  private static String callerInformation() {
    final var callerTrace = Thread.currentThread().getStackTrace()[3];
    return "%s:%d".formatted(callerTrace.getFileName(),
      callerTrace.getLineNumber());
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
