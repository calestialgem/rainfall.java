package rainfall.main;

import java.util.*;

import rainfall.launcher.*;

/**
 * Parsed form of command-line arguments.
 */
final class Arguments {
  /**
   * Seen options.
   */
  final Map<Class<? extends Option>, Option> options;

  /**
   * Command if it was seen.
   */
  final Optional<Command> executed;

  /**
   * Constructs an arguments.
   *
   * @param options  Options seen in the constructed arguments.
   * @param executed Command if it was seen in the constructed arguments.
   */
  Arguments(Map<Class<? extends Option>, Option> options,
    Optional<Command> executed) {
    this.options  = options;
    this.executed = executed;
  }
}
