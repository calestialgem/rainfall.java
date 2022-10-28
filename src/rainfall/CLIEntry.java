package rainfall;

/**
 * Command-line interface of Rainfall.
 *
 * @author calestialgem
 */
record CLIEntry() {
  /**
   * Entry point to the command-line interface.
   *
   * @param arguments Command-line arguments that were passed to the compiler.
   */
  public static void main(final String[] arguments) {
    if (!test()) {
      System.err.println("Stopping the compiler because of the failed tests!");
      return;
    }
  }

  /**
   * Tests the compiler.
   *
   * @return Whether all the tests passed.
   */
  private static boolean test() {
    final var tester = new Tester();
    tester.run(() -> true, "TesterInfrastructure");
    return tester.report();
  }
}
