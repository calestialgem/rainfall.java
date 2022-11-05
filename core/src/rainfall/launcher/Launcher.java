package rainfall.launcher;

import rainfall.utility.Map;

public final class Launcher {
  private final Command                              command;
  private final Map<Class<? extends Option>, Option> options;
  private Launcher(Command command,
    Map<Class<? extends Option>, Option> options) {
    this.command = command;
    this.options = options;
  }

  public static Launcher of(Command command,
    Map<Class<? extends Option>, Option> options) {
    return new Launcher(command, options);
  }

  public Command command() { return command; }
  public Map<Class<? extends Option>, Option> options() { return options; }
}
