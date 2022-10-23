package rainfall.main;

/**
 * Arguments did not include a command.
 */
final class MissingCommandError extends ArgumentError {
  /**
   * Constructs a missing command error.
   */
  MissingCommandError() {
    super("Provide a command!");
  }
}
