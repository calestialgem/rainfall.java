package rainfall.utility;

/**
 * Succeeded result.
 */
public final class Success<T, E> extends Result<T, E> {
  /**
   * Successfully computed value.
   */
  public final T value;

  /**
   * Constructs a success result.
   *
   * @param value Value of the constructed success result.
   */
  public Success(T value) {
    this.value = value;
  }

  @Override
  public boolean isSuccessful() {
    return true;
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public E getError() {
    throw new RuntimeException("There is no error!");
  }
}
