package rainfall.main;

/**
 * Parses the arguments that were read from the command line and passes them to
 * the launcher.
 */
final class Main {
  /**
   * Entry point of the compiler.
   *
   * @param arguments command-line arguments
   */
  public static void main(String[] arguments) {
    try {
      var parsedArguments = Parser.parse(arguments);
    } catch (ArgumentError error) {
      System.err.println(error.getMessage());
    }
  }
}
