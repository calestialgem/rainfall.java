package rainfall.main;

/**
 * An argument of an option was invalid.
 */
final class InvalidOptionArgumentError extends ArgumentError {
  /**
   * Constructs an invalid option argument error.
   *
   * @param option  Option that had an invalid argument.
   * @param invalid Argument that was invalid.
   */
  InvalidOptionArgumentError(String option, String invalid) {
    super(
      "Argument `%s` to option `%s` was invalid!".formatted(option, invalid));
  }

  /**
   * Constructs an invalid option argument error.
   *
   * @param option  Option that had an invalid argument.
   * @param invalid Argument that was invalid.
   * @param cause   Throwable that caused the error.
   */
  InvalidOptionArgumentError(String option, String invalid, Throwable cause) {
    super(
      "Argument `%s` to option `%s` was invalid!".formatted(option, invalid),
      cause);
  }
}
