package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.Box;

import static org.junit.jupiter.api.Assertions.*;

final class BoxTest {
  @Test void emptyIsEmpty() { assertTrue(Box.empty().isEmpty()); }
  @Test void emptyIsNotFull() { assertFalse(Box.empty().isFull()); }
  @Test void emptyDoesNotHaveValue() {
    assertThrows(UnsupportedOperationException.class, Box.empty()::value);
  }

  @Test void fullIsNotEmpty() { assertFalse(Box.full(1).isEmpty()); }
  @Test void fullIsFull() { assertTrue(Box.full(1).isFull()); }
  @Test void fullHasValue() { assertEquals(1, Box.full(1).value()); }
}
