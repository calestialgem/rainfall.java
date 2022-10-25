package rainfall.utility;

/**
 * Result of a process that can fail.
 */
public sealed abstract class Result<T, E> permits Success<T, E>, Failure<T, E> {
  /**
   * Returns true if the result has a value.
   *
   * @return Whether the result is a success.
   */
  public abstract boolean isSuccessful();

  /**
   * Returns true if the result does not have a value.
   *
   * @return Whether the result is a failure.
   */
  public final boolean isFailed() {
    return !isSuccessful();
  }

  /**
   * Returns the value if it is a success. Throws if it is a failure.
   *
   * @return Successfully computed value.
   */
  public abstract T getValue();

  /**
   * Returns the error if it is a failure. Throws if it is a success.
   *
   * @return Error that led to the failure.
   */
  public abstract E getError();
}
