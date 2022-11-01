package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import rainfall.Option;

final class OptionTest {
  @Test void directoryHasPath() {
    var path = Path.of(".");
    assertEquals(path, Option.directory(path).path());
  }
}
