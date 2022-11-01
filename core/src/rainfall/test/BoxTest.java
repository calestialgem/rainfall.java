package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.Box;

final class BoxTest {
  @Test void emptyIsEmpty() { assertTrue(Box.empty().isEmpty()); }
  @Test void emptyIsNotFull() { assertFalse(Box.empty().isFull()); }
  @Test void emptyDoesNotHaveValue() {
    assertThrows(UnsupportedOperationException.class, Box.empty()::value);
  }

  @Test void fullIsNotEmpty() { assertFalse(Box.full(1).isEmpty()); }
  @Test void fullIsFull() { assertTrue(Box.full(1).isFull()); }
  @Test void fullHasValue() {
    var value = 1;
    assertEquals(value, Box.full(value).value());
  }
}
