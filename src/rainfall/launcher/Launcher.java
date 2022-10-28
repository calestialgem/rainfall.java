package rainfall.launcher;

import java.util.Map;

/**
 * Launches the compiler.
 *
 * @param  command Command that is asked from this launch.
 * @param  options Options that are given to this launch.
 * @author         calestialgem
 */
public record Launcher(Command command,
  Map<Class<? extends Option>, Option> options) {}
