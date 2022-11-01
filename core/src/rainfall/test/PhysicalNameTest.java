package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.PhysicalName;

import static org.junit.jupiter.api.Assertions.*;

final class PhysicalNameTest {
  @Test void deniesEmptyValue() {
    assertEquals("error: Name cannot be empty!",
      PhysicalName.of("").error().toString());
  }

  @Test void deniesLowercaseInitial() {
    assertEquals("error: Name must start with an uppercase English letter!",
      PhysicalName.of("a").error().toString());
  }
  @Test void deniesDigitInitial() {
    assertEquals("error: Name must start with an uppercase English letter!",
      PhysicalName.of("0").error().toString());
  }
  @Test void acceptsUppercaseInitial() {
    assertTrue(PhysicalName.of("A").isSuccess());
  }
  @Test void deniesInvalidInitial() {
    assertEquals("""
      error: Name must start with an uppercase English letter!
      error: Name must solely consist of English letters and decimal digits!""",
      PhysicalName.of("_").error().toString());
  }

  @Test void acceptsLowercaseBody() {
    assertTrue(PhysicalName.of("Aa").isSuccess());
  }
  @Test void acceptsDigitBody() {
    assertTrue(PhysicalName.of("A0").isSuccess());
  }
  @Test void acceptsUppercaseBody() {
    assertTrue(PhysicalName.of("AA").isSuccess());
  }
  @Test void deniesInvalidBody() {
    assertEquals(
      "error: Name must solely consist of English letters and decimal digits!",
      PhysicalName.of("A_").error().toString());
  }

  @Test void acceptedHasValue() {
    assertEquals("A", PhysicalName.of("A").value().value());
  }
}
