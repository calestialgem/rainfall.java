package rainfall.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import rainfall.Box;

final class BoxTest {
  @Test void emptyBoxIsEmpty() { assertTrue(Box.empty().isEmpty()); }
  @Test void emptyBoxIsNotFull() { assertFalse(Box.empty().isFull()); }
  @Test void emptyBoxDoesNotHaveAValue() {
    assertThrows(UnsupportedOperationException.class, Box.empty()::value);
  }
  @Test void fullBoxIsNotEmpty() { assertFalse(Box.full(1).isEmpty()); }
  @Test void fullBoxIsFull() { assertTrue(Box.full(1).isFull()); }
  @Test void fullBoxHasTheGivenValue() {
    final var value = 1;
    assertEquals(value, Box.full(value).value());
  }
}
