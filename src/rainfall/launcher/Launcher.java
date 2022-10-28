package rainfall.launcher;

import java.util.Map;

public record Launcher(Command command,
  Map<Class<? extends Option>, Option> options) {}
