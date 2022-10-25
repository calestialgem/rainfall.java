package rainfall.launcher;

import java.nio.file.Path;
import java.util.Map;

import rainfall.loader.Loader;

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
   * Path to the workspace directory.
   */
  private Path directory;

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

  /**
   * Launch the Rainfall Thrice compiler.
   */
  public void launch() {
    resolveOptions();
    var loader = new Loader(directory);
    var linear = loader.load();
  }

  /**
   * Resolve the provided options.
   */
  private void resolveOptions() {
    directory = Path.of(".");
    if (options.containsKey(Directory.class)) {
      directory = ((Directory) options.get(Directory.class)).workspace;
    }
  }
}
