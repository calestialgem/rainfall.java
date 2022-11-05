package rainfall.qa.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import rainfall.launcher.Command;
import rainfall.launcher.Launcher;
import rainfall.launcher.Option;
import rainfall.utility.List;
import rainfall.utility.Map;

final class LauncherQA {
  @Test void aLauncherHasTheGivenCommand() {
    var command  = Command.check(List.of());
    var options  = Map.<Class<? extends Option>, Option>of();
    var launcher = Launcher.of(command, options);
    assertEquals(command, launcher.command());
  }
  @Test void aLauncherHasTheGivenOptions() {
    var command  = Command.check(List.of());
    var options  = Map.<Class<? extends Option>, Option>of();
    var launcher = Launcher.of(command, options);
    assertEquals(options, launcher.options());
  }
}
