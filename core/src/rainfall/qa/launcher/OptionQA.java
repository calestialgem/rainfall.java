package rainfall.qa.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import rainfall.launcher.Option;
import rainfall.utility.Map;

final class OptionQA {
  @Test void aDirectoryHasTheGivenPath() {
    var path = Path.of(".");
    assertEquals(path, Option.directory(path).path());
  }

  @Test void anEmptyOptionMapAcceptsRegisteringAnOption() {
    var option  = Option.directory(Path.of("."));
    var options = Map.<Class<? extends Option>, Option>mutable();
    assertTrue(Option.register(options, option).isSuccess());
  }
  @Test void anEmptyOptionMapAfterRegisteringContainsRegisteredOption() {
    var option  = Option.directory(Path.of("."));
    var options = Map.<Class<? extends Option>, Option>mutable();
    Option.register(options, option);
    assertTrue(options.contains(option.getClass()));
    assertEquals(option, options.get(option.getClass()));
  }
  @Test void anOptionMapWithAnOptionDeclinesRegisteringAnOptionOfTheSameType() {
    var first   = Option.directory(Path.of("."));
    var second  = Option.directory(Path.of(".."));
    var options = Map.<Class<? extends Option>, Option>mutable();
    Option.register(options, first);
    assertEquals("error: Option is already provided!",
      Option.register(options, second).error().toString());
  }
  @Test void
    anOptionMapWithAnOptionAfterRegisteringAnOptionOfTheSameTypeKeepsThePreviousOption() {
    var first   = Option.directory(Path.of("."));
    var second  = Option.directory(Path.of(".."));
    var options = Map.<Class<? extends Option>, Option>mutable();
    Option.register(options, first);
    Option.register(options, second);
    assertTrue(options.contains(first.getClass()));
    assertEquals(first, options.get(first.getClass()));
    assertNotEquals(second, options.get(second.getClass()));
  }
}
