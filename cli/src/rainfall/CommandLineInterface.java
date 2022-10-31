package rainfall;

import java.lang.reflect.InvocationTargetException;

import rainfall.core.Box;
import rainfall.core.Buffer;
import rainfall.core.Result;
import rainfall.core.Tester;

class CommandLineInterface {
  public static void main(String[] arguments) {
    if (!testEverythingAndReport()) {
      System.err.println("Exiting the compiler due to the failed tests.");
      return;
    }
    System.out.println("Hello, world!");
  }

  private static boolean testEverythingAndReport() {
    final var tester = new Tester();
    try {
      tester.run(Box.TestSuite.class);
      tester.run(Result.TestSuite.class);
      tester.run(Buffer.TestSuite.class);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return false;
    }
    return tester.report();
  }
}
