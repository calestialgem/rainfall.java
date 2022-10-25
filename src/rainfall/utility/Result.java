package rainfall.utility;

/**
 * Result of a process that can fail.
 *
 * @param <T> Type of the value that is the successful result.
 * @param <E> Type of the error that led to the failed result.
 */
public sealed abstract class Result<T, E> permits Success<T, E>, Failure<T, E> {
  /**
   * Converts the given value to a successful result.
   *
   * @param <T>   Type of the value that is the successful result.
   * @param <E>   Type of the error that led to the failed result.
   * @param <U>   Type of the value that will be converted to a successful
   *              result.
   * @param value Value that will be converted.
   *
   * @return Converted success result.
   */
  public static <T, E, U extends T> Result<T, E> ofSuccess(U value) {
    return new Success<>(value);
  }

  /**
   * Converts the given error to a failed result.
   *
   * @param <T>   Type of the value that is the successful result.
   * @param <E>   Type of the error that led to the failed result.
   * @param <U>   Type of the error that will be converted to a failed result.
   * @param error Error that will be converted.
   *
   * @return Converted failure result.
   */
  public static <T, E, U extends E> Result<T, E> ofFailure(U error) {
    return new Failure<>(error);
  }

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
