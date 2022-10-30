package rainfall;

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
    return tester.report();
  }
}
