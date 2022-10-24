package rainfall.main;

import java.util.*;

import rainfall.launcher.*;

/**
 * Parsed form of command-line arguments.
 */
final class Arguments {
  /**
   * Given options.
   */
  final Map<Class<? extends Option>, Option> options;

  /**
   * Given command.
   */
  final Command command;

  /**
   * Constructs an arguments.
   *
   * @param options  Options given in the constructed arguments.
   * @param executed Command given in the constructed arguments.
   */
  Arguments(Map<Class<? extends Option>, Option> options,
    Command command) {
    this.options = options;
    this.command = command;
  }
}
