package rainfall.utility;

/**
 * Failed result.
 *
 * @param <T> Type of the value that is the successful result.
 * @param <E> Type of the error that led to the failed result.
 */
public final class Failure<T, E> extends Result<T, E> {
  /**
   * Error that led to the failure.
   */
  public final E error;

  /**
   * Constructs a failure result.
   *
   * @param error Error that led to the constructed failure.
   */
  public Failure(E error) {
    this.error = error;
  }

  @Override
  public boolean isSuccessful() {
    return false;
  }

  @Override
  public T getValue() {
    throw new RuntimeException("There is no value!");
  }

  @Override
  public E getError() {
    return error;
  }
}
