package rainfall.qa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.utility.Result;

final class ResultQA {
  @Test void aFailureResultIsFailure() {
    assertTrue(Result.failure(1).isFailure());
  }
  @Test void aFailureResultIsNotSuccess() {
    assertFalse(Result.failure(1).isSuccess());
  }
  @Test void aFailureResultHasTheGivenError() {
    var error = 1;
    assertEquals(error, Result.failure(error).error());
  }
  @Test void aFailureResultThrowsWhenItsValueIsAccessed() {
    assertThrows(UnsupportedOperationException.class, Result.failure(1)::value);
  }

  @Test void aSuccessResultIsNotFailure() {
    assertFalse(Result.success(1).isFailure());
  }
  @Test void aSuccessResultIsSuccess() {
    assertTrue(Result.success(1).isSuccess());
  }
  @Test void aSuccessResultThrowsWhenItsErrorIsAccessed() {
    assertThrows(UnsupportedOperationException.class, Result.success(1)::error);
  }
  @Test void aSuccessHasTheGivenValue() {
    var value = 1;
    assertEquals(value, Result.success(value).value());
  }
}
