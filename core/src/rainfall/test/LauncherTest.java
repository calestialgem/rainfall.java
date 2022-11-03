package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import rainfall.launcher.Command;
import rainfall.launcher.Launcher;
import rainfall.launcher.Option;

final class LauncherTest {
  @Test void hasCommand() {
    var command  = Command.check(List.of());
    var options  = Map.<Class<? extends Option>, Option>of();
    var launcher = Launcher.of(command, options);
    assertEquals(command, launcher.command());
  }
  @Test void hasOptions() {
    var command  = Command.check(List.of());
    var options  = Map.<Class<? extends Option>, Option>of();
    var launcher = Launcher.of(command, options);
    assertEquals(options, launcher.options());
  }
}
