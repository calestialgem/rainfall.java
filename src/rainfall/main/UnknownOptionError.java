package rainfall.main;

/**
 * An option name or shortcut was not recognized.
 */
final class UnknownOptionError extends ArgumentError {
  /**
   * Constructs an unknown option error.
   *
   * @param unknown Argument that was not recognized as an option.
   */
  UnknownOptionError(String unknown) {
    super("Could not recognize `%s` as an option!".formatted(unknown));
  }
}
