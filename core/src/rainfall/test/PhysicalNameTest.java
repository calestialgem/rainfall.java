package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.PhysicalName;

import static org.junit.jupiter.api.Assertions.*;

class PhysicalNameTest {
  @Test void deniesEmptyValue() { assertTrue(PhysicalName.of("").isFailure()); }

  @Test void deniesLowercaseInitial() {
    assertTrue(PhysicalName.of("a").isFailure());
  }
  @Test void deniesDigitInitial() {
    assertTrue(PhysicalName.of("0").isFailure());
  }
  @Test void acceptsUppercaseInitial() {
    assertTrue(PhysicalName.of("A").isSuccess());
  }
  @Test void deniesInvalidInitial() {
    assertTrue(PhysicalName.of("_").isFailure());
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
    assertTrue(PhysicalName.of("A_").isFailure());
  }

  @Test void acceptedHasValue() {
    assertEquals("A", PhysicalName.of("A").value().value());
  }
}
