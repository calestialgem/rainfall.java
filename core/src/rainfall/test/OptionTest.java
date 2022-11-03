package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import rainfall.Option;

final class OptionTest {
  @Test void directoryHasPath() {
    var path = Path.of(".");
    assertEquals(path, Option.directory(path).path());
  }

  @Test void registrationAcceptsNonexistingOption() {
    var option  = Option.directory(Path.of("."));
    var options = new HashMap<Class<? extends Option>, Option>();
    assertTrue(Option.register(options, option).isSuccess());
  }
  @Test void registersNonexistingOption() {
    var option  = Option.directory(Path.of("."));
    var options = new HashMap<Class<? extends Option>, Option>();
    Option.register(options, option);
    assertTrue(options.containsKey(option.getClass()));
    assertEquals(option, options.get(option.getClass()));
  }
  @Test void registrationDeclinesExistingOption() {
    var first   = Option.directory(Path.of("."));
    var second  = Option.directory(Path.of(".."));
    var options = new HashMap<Class<? extends Option>, Option>();
    Option.register(options, first);
    assertEquals("error: Option is already provided!",
      Option.register(options, second).error().toString());
  }
  @Test void doesNotRegisterExistingOption() {
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
