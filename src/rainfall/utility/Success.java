package rainfall.utility;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Succeeded result.
 *
 * @param <T> Type of the value that is the successful result.
 * @param <E> Type of the error that led to the failed result.
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

  @Override
  public <U> Result<U, E> map(Function<T, U> mapper) {
    return Result.ofSuccess(mapper.apply(value));
  }

  @Override
  public <U> Result<U, E> bind(Function<T, Result<U, E>> binder) {
    return binder.apply(value);
  }

  @Override
  public Result<T, E> check(Predicate<T> predicate, Supplier<E> supplier) {
    return predicate.test(value) ? this : Result.ofFailure(supplier.get());
  }
}
