package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import rainfall.Command;
import rainfall.PhysicalName;

final class CommandTest {
  @Test void newHasName() {
    var created = PhysicalName.of("A").value();
    assertEquals(created, Command.new_(created).created());
  }
}
