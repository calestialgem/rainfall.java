package rainfall.main;

/**
 * A command name or shortcut was not recognized.
 */
final class UnknownCommandError extends ArgumentError {
  /**
   * Constructs an unknown command error.
   *
   * @param unknown Argument that was not recognized as a command.
   */
  UnknownCommandError(String unknown) {
    super("Could not recognize `%s` as a command!".formatted(unknown));
  }
}
