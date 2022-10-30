package rainfall;

import rainfall.utility.Box;
import rainfall.utility.Result;
import rainfall.utility.UnitTestRunner;

class CLIEntry {
  public static void main(String[] arguments) {
    if (!testAndReport()) {
      System.err.println("Stopping the compiler due to failed tests!");
      return;
    }

    System.out.println("Hello, world!");
  }

  private static boolean testAndReport() {
    var runner = new UnitTestRunner();
    Box.test(runner);
    Result.test(runner);
    return runner.report();
  }
}
