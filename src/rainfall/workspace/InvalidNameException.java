package rainfall.workspace;

/**
 * Name of a Thrice package, module, or source was wrong.
 */
public final class InvalidNameException extends Exception {
  /**
   * Constructs an invalid name exception.
   *
   * @param message Explanation of why the name was invalid.
   */
  public InvalidNameException(String message) {
    super(message);
  }
}
