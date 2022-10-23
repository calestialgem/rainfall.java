package rainfall.main;

/**
 * An argument of a command was invalid.
 */
final class InvalidCommandArgumentError extends ArgumentError {
  /**
   * Constructs an invalid command argument error.
   *
   * @param command Command that had an invalid argument.
   * @param invalid Argument that was invalid.
   */
  InvalidCommandArgumentError(String command, String invalid) {
    super(
      "Argument `%s` to command `%s` was invalid!".formatted(command, invalid));
  }

  /**
   * Constructs an invalid command argument error.
   *
   * @param command Command that had an invalid argument.
   * @param invalid Argument that was invalid.
   * @param cause   Throwable that caused the error.
   */
  InvalidCommandArgumentError(String command, String invalid, Throwable cause) {
    super(
      "Argument `%s` to command `%s` was invalid!".formatted(command, invalid),
      cause);
  }
}
