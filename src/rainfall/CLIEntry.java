package rainfall;

import rainfall.utility.Box;
import rainfall.utility.Tester;

record CLIEntry() {
  public static void main(final String[] arguments) {
    if (!test()) {
      System.err.println("Stopping the compiler because of the failed tests!");
      return;
    }
  }

  private static boolean test() {
    final var tester = new Tester();
    Box.test(tester);
    return tester.report();
  }
}
