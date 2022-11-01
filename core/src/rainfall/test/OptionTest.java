package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.Option;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

final class OptionTest {
  @Test void directoryHasPath() {
    assertEquals(Path.of("."), Option.directory(Path.of(".")).path());
  }
}
