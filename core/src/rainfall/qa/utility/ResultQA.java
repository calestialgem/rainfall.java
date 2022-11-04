package rainfall.qa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.utility.Result;

final class ResultQA {
  @Test void failureIsFailure() { assertTrue(Result.failure(1).isFailure()); }
  @Test void failureIsNotSuccess() {
    assertFalse(Result.failure(1).isSuccess());
  }
  @Test void failureHasError() {
    var error = 1;
    assertEquals(error, Result.failure(error).error());
  }
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
  @Test void successHasValue() {
    var value = 1;
    assertEquals(value, Result.success(value).value());
  }
}
