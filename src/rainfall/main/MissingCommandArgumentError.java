package rainfall.main;

/**
 * An argument to a command was missing.
 */
final class MissingCommandArgumentError extends ArgumentError {
  /**
   * Constructs a missing command argument error.
   *
   * @param command  Command that had a missing argument.
   * @param expected Description of the argument that was missing.
   */
  MissingCommandArgumentError(String command, String expected) {
    super(
      "Argument `%s` to command `%s` was missing!".formatted(command,
        expected));
  }
}
