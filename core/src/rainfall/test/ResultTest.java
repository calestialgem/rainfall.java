package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.Result;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
  @Test void failureResultIsFailure() {
    assertTrue(Result.failure(1).isFailure());
  }
  @Test void failureResultIsNotSuccess() {
    assertFalse(Result.failure(1).isSuccess());
  }
  @Test void failureResultHasTheGivenError() {
    final var error = 1;
    assertEquals(error, Result.failure(error).error());
  }
  @Test void failureResultDoesNotHaveAValue() {
    assertThrows(UnsupportedOperationException.class, Result.failure(1)::value);
  }
  @Test void successResultIsNotFailure() {
    assertFalse(Result.success(1).isFailure());
  }
  @Test void successResultIsSuccess() {
    assertTrue(Result.success(1).isSuccess());
  }
  @Test void successResultDoesNotHaveAnError() {
    assertThrows(UnsupportedOperationException.class, Result.success(1)::error);
  }
  @Test void successResultHasTheGivenValue() {
    final var value = 1;
    assertEquals(value, Result.success(value).value());
  }
}
