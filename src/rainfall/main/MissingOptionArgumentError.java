package rainfall.main;

/**
 * An argument to an option was missing.
 */
final class MissingOptionArgumentError extends ArgumentError {
  /**
   * Constructs a missing option argument error.
   *
   * @param option   Option that had a missing argument.
   * @param expected Description of the argument that was missing.
   */
  MissingOptionArgumentError(String option, String expected) {
    super(
      "Argument `%s` to option `%s` was missing!".formatted(option, expected));
  }
}
