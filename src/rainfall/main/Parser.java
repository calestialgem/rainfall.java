package rainfall.main;

import java.nio.file.*;
import java.util.*;

import rainfall.launcher.*;

/**
 * Parses the command line arguments. Should not be mistaken for the Thrice
 * parser in Rainfall.
 */
final class Parser {
  /**
   * Parse command-line arguments.
   *
   * @param parsed List of command-line arguments, excluding the path to the
   *               executable, that will be parsed.
   *
   * @return Parsed command-line arguments.
   */
  static Arguments parse(List<String> parsed) {
    return new Parser(parsed).run();
  }

  /**
   * Seen options.
   */
  private Map<Class<? extends Option>, Option> options;

  /**
   * Command if it was seen.
   */
  private Optional<Command> executed;

  /**
   * List of command-line arguments, excluding the path to the executable, that
   * will be parsed.
   */
  private final List<String> parsed;

  /**
   * Index to the currently parsed argument.
   */
  private int current;

  /**
   * Constructs an empty parser that is ready to parse.
   *
   * @param parsed List of command-line arguments, excluding the path to the
   *               executable, that will be parsed.
   */
  private Parser(List<String> parsed) {
    options  = new HashMap<>();
    executed = Optional.empty();

    this.parsed = parsed;
    current     = 0;
  }

  /**
   * Run the parser.
   *
   * @return Parsed arguments.
   */
  private Arguments run() {
    parseOptions();
    parseCommand();
    return new Arguments(options, executed);
  }

  /**
   * Parse the options.
   */
  private void parseOptions() {
    while (parseOption()) {
    }
  }

  /**
   * Tries to parse an option.
   *
   * @return Whether an option was parsed.
   */
  private boolean parseOption() {
    var option = get();
    if (optionShortcut()) {
      var shortcut = option.charAt(1);
      current++;
      switch (shortcut) {
        case 'd' -> parseDirectory(option);
        default -> throw new UnknownOptionError(get());
      }
      return true;
    }
    if (optionName()) {
      var name = option.substring(2);
      current++;
      switch (name) {
        case "directory" -> parseDirectory(option);
        default -> throw new UnknownOptionError(get());
      }
      return true;
    }
    return false;
  }

  /**
   * Parse a directory option.
   */
  private void parseDirectory(String option) {
    var argument  = get();
    var workspace = Path.of(argument);
    var file      = workspace.toFile();
    if (!file.isDirectory())
      throw new InvalidOptionArgumentError(option, argument);
    options.put(Directory.class, new Directory(workspace));
  }

  /**
   * Checks whether there is an argument and its an option shortcut.
   *
   * @return Whether the current argument is an option shortcut.
   */
  private boolean optionShortcut() {
    return has() && get().length() == 2 && get().charAt(0) == '-';
  }

  /**
   * Checks whether there is an argument and its an option name.
   *
   * @return Whether the current argument is an option name.
   */
  private boolean optionName() {
    return has() && get().length() > 2 && get().startsWith("--");
  }

  /**
   * Parse the commands.
   */
  private void parseCommand() {
  }

  /**
   * Checks the validity of the current index.
   *
   * @return Whether the current argument exists.
   */
  private boolean has() {
    return current < parsed.size();
  }

  /**
   * Returns the current argument.
   *
   * @return Currently parsed argument.
   */
  private String get() {
    return parsed.get(current);
  }
}
