package rainfall.qa.physical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.physical.Name;

final class NameQA {
  @Test void cannotCreateANameThatIsEmpty() {
    assertEquals("error: Name cannot be empty!",
      Name.of("").error().toString());
  }

  @Test void canCreateANameThatStartsWithAnUppercaseLetter() {
    assertTrue(Name.of("A").isSuccess());
  }
  @Test void cannotCreateANameThatStartsWithALowercaseLetter() {
    assertEquals("error: Name must start with an uppercase English letter!",
      Name.of("a").error().toString());
  }
  @Test void cannotCreateANameThatStartsWithADecimalDigit() {
    assertEquals("error: Name must start with an uppercase English letter!",
      Name.of("0").error().toString());
  }
  @Test void cannotCreateANameThatStartsWithAnInvalidCharacter() {
    assertEquals("""
      error: Name must start with an uppercase English letter!
      error: Name must solely consist of English letters and decimal digits!""",
      Name.of("_").error().toString());
  }

  @Test void canCreateANameThatContinuesWithAnUppercaseLetter() {
    assertTrue(Name.of("AA").isSuccess());
  }
  @Test void canCreateANameThatContinuesWithALowercaseLetter() {
    assertTrue(Name.of("Aa").isSuccess());
  }
  @Test void canCreateANameThatContinuesWithADecimalDigit() {
    assertTrue(Name.of("A0").isSuccess());
  }
  @Test void cannotCreateANameThatContinuesWithAnInvalidCharacter() {
    assertEquals(
      "error: Name must solely consist of English letters and decimal digits!",
      Name.of("A_").error().toString());
  }

  @Test void aNameHasTheGivenValue() {
    var value = "A";
    assertEquals(value, Name.of(value).value().value());
  }

  @Test void aNameStringifiedAsItsValue() {
    var value = "A";
    assertEquals(value, Name.of(value).value().toString());
  }
}
