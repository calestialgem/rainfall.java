package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.Command;
import rainfall.PhysicalName;

import static org.junit.jupiter.api.Assertions.*;

final class CommandTest {
  @Test void newHasName() {
    var created = PhysicalName.of("A").value();
    assertEquals(created, Command.new_(created).created());
  }
}
