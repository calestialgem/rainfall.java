package rainfall.qa.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import rainfall.launcher.Option;

final class OptionQA {
  @Test void aDirectoryHasTheGivenPath() {
    var path = Path.of(".");
    assertEquals(path, Option.directory(path).path());
  }

  @Test void anEmptyOptionMapAcceptsRegisteringAnOption() {
    var option  = Option.directory(Path.of("."));
    var options = new HashMap<Class<? extends Option>, Option>();
    assertTrue(Option.register(options, option).isSuccess());
  }
  @Test void anEmptyOptionMapAfterRegisteringContainsRegisteredOption() {
    var option  = Option.directory(Path.of("."));
    var options = new HashMap<Class<? extends Option>, Option>();
    Option.register(options, option);
    assertTrue(options.containsKey(option.getClass()));
    assertEquals(option, options.get(option.getClass()));
  }
  @Test void anOptionMapWithAnOptionDeclinesRegisteringAnOptionOfTheSameType() {
    var first   = Option.directory(Path.of("."));
    var second  = Option.directory(Path.of(".."));
    var options = new HashMap<Class<? extends Option>, Option>();
    Option.register(options, first);
    assertEquals("error: Option is already provided!",
      Option.register(options, second).error().toString());
  }
  @Test void
    anOptionMapWithAnOptionAfterRegisteringAnOptionOfTheSameTypeKeepsThePreviousOption() {
    var first   = Option.directory(Path.of("."));
    var second  = Option.directory(Path.of(".."));
    var options = new HashMap<Class<? extends Option>, Option>();
    Option.register(options, first);
    Option.register(options, second);
    assertTrue(options.containsKey(first.getClass()));
    assertEquals(first, options.get(first.getClass()));
    assertNotEquals(second, options.get(second.getClass()));
  }
}
