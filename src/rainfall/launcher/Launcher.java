package rainfall.launcher;

import java.util.Map;

/**
 * Launches the compiler after parsing the configuration
 * file.
 */
public final class Launcher {
  /**
   * Options for the launch.
   */
  public final Map<Class<? extends Option>, Option> options;

  /**
   * Command that will be executed by the launcher.
   */
  public final Command executed;

  /**
   * Constructs a launcher.
   *
   * @param options  Options for the constructed launch.
   * @param executed Command that will be executed by the constructed launch.
   */
  public Launcher(Map<Class<? extends Option>, Option> options,
    Command executed) {
    this.options  = options;
    this.executed = executed;
  }
}
