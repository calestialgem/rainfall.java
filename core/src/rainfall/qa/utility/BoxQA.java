package rainfall.qa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.utility.Box;

final class BoxQA {
  @Test void anEmptyBoxIsEmpty() { assertTrue(Box.empty().isEmpty()); }
  @Test void anEmptyBoxIsNotFull() { assertFalse(Box.empty().isFull()); }
  @Test void anEmptyBoxThrowsWhenItsValueIsAccessed() {
    assertThrows(UnsupportedOperationException.class, Box.empty()::value);
  }

  @Test void aFullBoxIsNotEmpty() { assertFalse(Box.full(1).isEmpty()); }
  @Test void aFullBoxIsFull() { assertTrue(Box.full(1).isFull()); }
  @Test void aFullBoxHasTheGivenValue() {
    var value = 1;
    assertEquals(value, Box.full(value).value());
  }
}
