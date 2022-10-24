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
   * @param parsed Command-line arguments, excluding the path to the executable,
   *               that will be parsed.
   *
   * @return Parsed command-line arguments.
   */
  static Arguments parse(String[] parsed) throws ArgumentError {
    return new Parser(parsed).run();
  }

  /**
   * Command-line arguments, excluding the path to the executable, that will be
   * parsed.
   */
  private final String[] parsed;

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
  private Parser(String[] parsed) {
    this.parsed = parsed;
    current     = 0;
  }

  /**
   * Run the parser.
   *
   * @return Parsed arguments.
   */
  private Arguments run() {
    return new Arguments(parseOptions(), parseCommand());
  }

  /**
   * Parse the options.
   *
   * @return All parsed options.
   */
  private Map<Class<? extends Option>, Option> parseOptions() {
    // Parse an option until cannot.
    var result = new HashMap<Class<? extends Option>, Option>();
    while (true) {
      var optionResult = parseOption();
      if (optionResult.isEmpty())
        break;
      var option = optionResult.get();
      result.put(option.getClass(), option);
    }
    return result;
  }

  /**
   * Tries to parse an option.
   *
   * @return Whether an option was parsed.
   */
  private Optional<Option> parseOption() {
    // Check whether there is an argument.
    if (!has())
      return Optional.empty();
    var option = consume();

    // Check whether the argument can be an option shortcut.
    if (option.length() == 2 && option.charAt(0) == '-') {
      var shortcut = option.charAt(1);
      return Optional.of(switch (shortcut) {
        case 'd' -> parseDirectory(option);
        default -> throw new ArgumentError(
          """
          Could not recognize option shortcut `%s`!
          Use:
           - directory (d): set workspace directory
          """.formatted(shortcut));
      });
    }

    // Check whether the argument can be an option name.
    if (option.length() > 2 && option.startsWith("--")) {
      var name = option.substring(2);
      return Optional.of(switch (name) {
        case "directory" -> parseDirectory(option);
        default -> throw new ArgumentError(
          """
          Could not recognize option name `%s`!
          Use:
           - directory (d): set workspace directory
          """.formatted(name));
      });
    }

    // Not an option if none matched. Need to roll back the consumed argument.
    current--;
    return Optional.empty();
  }

  /**
   * Parse a directory option.
   *
   * @param option Argument that indicated the option. Used for reporting to
   *               user on error.
   */
  private Option parseDirectory(String option) {
    // Check whether the path to workspace directory argument exists.
    if (!has())
      throw new ArgumentError(
        "Expected workspace directory argument for directory option after `%s`!"
          .formatted(option));
    var argument = consume();

    // Try converting argument to a path.
    try {
      var workspace = Path.of(argument);
      var file      = workspace.toFile();

      // Check the given workspace path.
      if (!file.exists())
        throw new ArgumentError(
          "Given workspace path `%s` for the option `%s` does not exists!"
            .formatted(argument, option));
      if (!file.isDirectory())
        throw new ArgumentError(
          "Given workspace path `%s` for the option `%s` is not a directory!"
            .formatted(argument, option));
      if (!file.canRead())
        throw new ArgumentError(
          "Given workspace path `%s` for the option `%s` is not readable!"
            .formatted(argument, option));
      if (!file.canWrite())
        throw new ArgumentError(
          "Given workspace path `%s` for the option `%s` is not writable!"
            .formatted(argument, option));

      return new Directory(workspace);
    } catch (InvalidPathException exception) {
      throw new ArgumentError(
        "Given workspace argument `%s` for the option `%s` is not a path!"
          .formatted(argument, option),
        exception);
    }
  }

  /**
   * Parse the commands.
   *
   * @return Parsed command.
   */
  private Command parseCommand() {
    // Check whether a command is given.
    if (!has())
      throw new ArgumentError(
        """
        There is no command given!
        Use:
         - new   (n): create a package
         - check (c): check packages
         - test  (t): test packages
         - build (b): build executable
         - run   (r): run executable
        """);
    var command = consume();

    // Dispatch to command name or shortcut.
    var result = switch (command) {
      case "new", "n" -> parseNew(command);
      case "check", "c" -> parseCheck(command);
      case "test", "t" -> parseTest(command);
      case "build", "b" -> parseBuild(command);
      case "run", "r" -> parseRun(command);
      default -> throw new ArgumentError(
        """
        Could not recognize command `%s`!
        Use:
         - new   (n): create a package
         - check (c): check packages
         - test  (t): test packages
         - build (b): build executable
         - run   (r): run executable
        """.formatted(command));
    };

    // Check whether all the arguments are consumed.
    if (has())
      throw new ArgumentError(
        "Unexpected arguments `%s` after the command `%s`!"
          .formatted(String.join(" ", remaining()), command));

    return result;
  }

  /**
   * Parse a new command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Command parseNew(String command) {
    // Check whether a name for the created package is given.
    if (!has())
      throw new ArgumentError(
        "Expected a name for the new package that will be created for the `%s` command!"
          .formatted(command));
    var argument = consume();

    checkName(command, argument);
    return new New(argument);
  }

  /**
   * Parse a check command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Command parseCheck(String command) {
    // Check all the arguments.
    var arguments = new ArrayList<String>();
    while (has()) {
      var argument = consume();
      checkName(command, argument);
      arguments.add(argument);
    }

    return new Check(arguments);
  }

  /**
   * Parse a test command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Command parseTest(String command) {
    // Check all the arguments.
    var arguments = new ArrayList<String>();
    while (has()) {
      var argument = consume();
      checkName(command, argument);
      arguments.add(argument);
    }

    return new Test(arguments);
  }

  /**
   * Parse a build command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Command parseBuild(String command) {
    // Check whether a name for the built package is given.
    if (!has())
      throw new ArgumentError(
        "Expected a name for the executable package that will be built for the `%s` command!"
          .formatted(command));
    var argument = consume();

    checkName(command, argument);
    return new Build(argument);
  }

  /**
   * Parse a run command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Command parseRun(String command) {
    // Check whether a name for the run package is given.
    if (!has())
      throw new ArgumentError(
        "Expected a name for the executable package that will be run for the `%s` command!"
          .formatted(command));
    var argument = consume();

    checkName(command, argument);

    // All the arguments that will be passed to the run executable.
    var remaining = List.of(remaining());

    return new Run(argument, remaining);
  }

  /**
   * Check the given package name.
   *
   * @param command Reported command.
   * @param name    Checked package name.
   */
  private static void checkName(String command, String name) {
    // Check whether there is a first character.
    if (name.length() == 0)
      throw new ArgumentError(
        "Given package name `%s` for the `%s` command is empty!"
          .formatted(name, command));

    // Check first character.
    char initial = name.charAt(0);
    if (initial < 'A' || initial > 'Z')
      throw new ArgumentError(
        "Initial `%c` of the given package name `%s` for the `%s` command is not an upper case English letter!"
          .formatted(initial, name, command));

    // Check the rest.
    for (int index = 1; index < name.length(); index++) {
      char character = name.charAt(index);
      if ((character < 'A' || character > 'Z')
        && (character < 'a' || character > 'z')
        && (character < '0' || character > '9'))
        throw new ArgumentError(
          "Character `%c` in the given package name `%s` for the `%s` command is not an English letter or decimal digit!"
            .formatted(character, name, command));
    }
  }

  /**
   * Checks the validity of the current index.
   *
   * @return Whether the current argument exists.
   */
  private boolean has() {
    return current < parsed.length;
  }

  /**
   * Returns the current argument and goes to the next one.
   *
   * @return Currently parsed argument.
   */
  private String consume() {
    return parsed[current++];
  }

  /**
   * Returns all the remaining arguments.
   *
   * @return Arguments from the current one to the last one.
   */
  private String[] remaining() {
    return Arrays.copyOfRange(parsed, current, parsed.length);
  }
}
