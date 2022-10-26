package rainfall.launcher;

import java.util.Map;

public record Launcher(Map<Class<? extends Option>, Option> options,
    Command command) {}
