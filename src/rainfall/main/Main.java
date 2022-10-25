package rainfall.main;

import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import rainfall.launcher.*;
import rainfall.workspace.*;

/**
 * Parses the arguments that were read from the command line and passes them to
 * the launcher. Should not be mistaken for the Thrice parser in Rainfall.
 */
final class Main {
  /**
   * Entry point of the compiler as executable.
   *
   * @param arguments Given command-line arguments.
   */
  public static void main(String[] arguments) {
    var launcher = new Main(arguments).parse();
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
  private Main(String[] parsed) {
    this.parsed = parsed;
  }

  /**
   * Parses the command line arguments to a launcher.
   *
   * @return Launcher with the parsed options and command.
   */
  private Launcher parse() {
    // Initialize mutable state.
    current = 0;
    return new Launcher(parseOptions(), parseCommand());
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

    try {
      return new New(PhysicalName.of(argument));
    } catch (InvalidNameException exception) {
      throw new ArgumentError(
        "Argument `%s` to the new command `%s` is not a valid package name!"
          .formatted(argument, command),
        exception);
    }
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
    return new Check(Stream.of(remaining()).map(argument -> {
      try {
        return PhysicalName.of(argument);
      } catch (InvalidNameException exception) {
        throw new ArgumentError(
          "Argument `%s` to the check command `%s` is not a valid package name!"
            .formatted(argument, command),
          exception);
      }
    }).toList());
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
    return new Test(Stream.of(remaining()).map(argument -> {
      try {
        return PhysicalName.of(argument);
      } catch (InvalidNameException exception) {
        throw new ArgumentError(
          "Argument `%s` to the test command `%s` is not a valid package name!"
            .formatted(argument, command),
          exception);
      }
    }).toList());
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

    try {
      return new Build(PhysicalName.of(argument));
    } catch (InvalidNameException exception) {
      throw new ArgumentError(
        "Argument `%s` to the build command `%s` is not a valid package name!"
          .formatted(argument, command),
        exception);
    }
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

    try {
      return new Run(PhysicalName.of(argument), List.of(remaining()));
    } catch (InvalidNameException exception) {
      throw new ArgumentError(
        "Argument `%s` to the run command `%s` is not a valid package name!"
          .formatted(argument, command),
        exception);
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
   * Returns and consumes all the remaining arguments.
   *
   * @return Arguments from the current one to the last one.
   */
  private String[] remaining() {
    var result = Arrays.copyOfRange(parsed, current, parsed.length);
    current = parsed.length;
    return result;
  }
}
