package rainfall.qa.physical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.physical.Name;

final class NameQA {
  @Test void deniesEmptyValue() {
    assertEquals("error: Name cannot be empty!",
      Name.of("").error().toString());
  }

  @Test void deniesLowercaseInitial() {
    assertEquals("error: Name must start with an uppercase English letter!",
      Name.of("a").error().toString());
  }
  @Test void deniesDigitInitial() {
    assertEquals("error: Name must start with an uppercase English letter!",
      Name.of("0").error().toString());
  }
  @Test void acceptsUppercaseInitial() { assertTrue(Name.of("A").isSuccess()); }
  @Test void deniesInvalidInitial() {
    assertEquals("""
      error: Name must start with an uppercase English letter!
      error: Name must solely consist of English letters and decimal digits!""",
      Name.of("_").error().toString());
  }

  @Test void acceptsLowercaseBody() { assertTrue(Name.of("Aa").isSuccess()); }
  @Test void acceptsDigitBody() { assertTrue(Name.of("A0").isSuccess()); }
  @Test void acceptsUppercaseBody() { assertTrue(Name.of("AA").isSuccess()); }
  @Test void deniesInvalidBody() {
    assertEquals(
      "error: Name must solely consist of English letters and decimal digits!",
      Name.of("A_").error().toString());
  }

  @Test void acceptedHasValue() {
    var value = "A";
    assertEquals(value, Name.of(value).value().value());
  }
}
