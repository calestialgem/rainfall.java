package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.Result;

import static org.junit.jupiter.api.Assertions.*;

final class ResultTest {
  @Test void failureIsFailure() { assertTrue(Result.failure(1).isFailure()); }
  @Test void failureIsNotSuccess() {
    assertFalse(Result.failure(1).isSuccess());
  }
  @Test void failureHasError() { assertEquals(1, Result.failure(1).error()); }
  @Test void failureDoesNotHaveValue() {
    assertThrows(UnsupportedOperationException.class, Result.failure(1)::value);
  }

  @Test void successIsNotFailure() {
    assertFalse(Result.success(1).isFailure());
  }
  @Test void successIsSuccess() { assertTrue(Result.success(1).isSuccess()); }
  @Test void successDoesNotHaveError() {
    assertThrows(UnsupportedOperationException.class, Result.success(1)::error);
  }
  @Test void successHasValue() { assertEquals(1, Result.success(1).value()); }
}
