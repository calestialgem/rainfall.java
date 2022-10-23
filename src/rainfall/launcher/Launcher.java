package rainfall.launcher;

import java.util.*;

/**
 * Launches the compiler after parsing the configuration
 * file.
 */
public final class Launcher {
  /**
   * Map from options to values for the command.
   */
  public final Map<Option, String> options;

  /**
   * Command to be launched.
   */
  public final Command command;

  /**
   * Arguments given for the launched command.
   */
  public final List<String> arguments;

  /**
   * Constructs a launcher.
   *
   * @param options   Map from the options to values for the command launched by
   *                  the constructed launcher.
   * @param command   Command to be launched by the constructed launcher.
   * @param arguments Arguments given for the command launched by the
   *                  constructed launcher.
   */
  public Launcher(Map<Option, String> options, Command command,
    List<String> arguments) {
    this.options   = options;
    this.command   = command;
    this.arguments = arguments;
  }

  @Override
  public int hashCode() {
    return Objects.hash(options, command, arguments);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Launcher)) {
      return false;
    }
    Launcher other = (Launcher) obj;
    return Objects.equals(options, other.options) && command == other.command
      && Objects.equals(arguments, other.arguments);
  }
}
