package rainfall.utility;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
   * @param value Value that will be converted.
   *
   * @return Converted success result.
   */
  public static <T, E> Result<T, E> ofSuccess(T value) {
    return new Success<>(value);
  }

  /**
   * Converts the given error to a failed result.
   *
   * @param <T>   Type of the value that is the successful result.
   * @param <E>   Type of the error that led to the failed result.
   * @param error Error that will be converted.
   *
   * @return Converted failure result.
   */
  public static <T, E> Result<T, E> ofFailure(E error) {
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

  /**
   * Propagates the error with a result that expects a different value if it is
   * an error. Throws if it is a success.
   *
   * @param <U> Type of the expected value.
   *
   * @return Failure with the error that led to this failure.
   */
  public <U> Result<U, E> propagate() {
    return Result.ofFailure(getError());
  }

  /**
   * Maps the value if it exits using the given function.
   *
   * @param <U>    Type of the mapped value.
   * @param mapper Function that maps the value.
   *
   * @return Success with the mapped value if this is success, otherwise failure
   *         with this error.
   */
  public abstract <U> Result<U, E> map(Function<T, U> mapper);

  /**
   * Maps the value if it exits using the given function that can fail.
   *
   * @param <U>    Type of the mapped value.
   * @param binder Function that binds the value.
   *
   * @return Success with the mapped value if this is success and the binder was
   *         successful, otherwise failure with this error or the error returned
   *         by the binder.
   */
  public abstract <U> Result<U, E> bind(Function<T, Result<U, E>> binder);

  /**
   * Checks the value if it exits using the given predicate.
   *
   * @param predicate Predicate that checks the value.
   * @param supplier  Error supplier used if the predicate fails.
   *
   * @return Success with this value if this is success and predicate passes,
   *         otherwise failure with this error or the one supplied.
   */
  public abstract Result<T, E> check(Predicate<T> predicate,
    Supplier<E> supplier);

  /**
   * Combines multiple results to a single one.
   *
   * @param <T>           Type of the original values.
   * @param <E>           Type of the original errors.
   * @param <U>           Type of the combined value.
   * @param <F>           Type of the combined error.
   * @param results       Results that are combined.
   * @param valueCombiner Function that combines values.
   * @param errorCombiner Function that combines errors.
   *
   * @return Combination of the results.
   */
  public static <T, E, U, F> Result<U, F>
    combine(Collection<Result<T, E>> results,
      Function<List<T>, U> valueCombiner,
      Function<List<E>, F> errorCombiner) {
    var errors = results.stream().filter(Result::isFailed).map(Result::getError)
      .toList();

    if (errors.size() != 0)
      return Result.ofFailure(errorCombiner.apply(errors));

    return Result.ofSuccess(
      valueCombiner.apply(results.stream().map(Result::getValue).toList()));
  }
}
