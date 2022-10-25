package rainfall.main;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import rainfall.launcher.Build;
import rainfall.launcher.Check;
import rainfall.launcher.Command;
import rainfall.launcher.Directory;
import rainfall.launcher.Launcher;
import rainfall.launcher.New;
import rainfall.launcher.Option;
import rainfall.launcher.Run;
import rainfall.launcher.Test;
import rainfall.utility.Result;
import rainfall.utility.Success;
import rainfall.workspace.PhysicalName;

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
    var result = new Main(List.of(arguments)).parse();
    if (!(result instanceof Success<Launcher, String> launcher)) {
      System.err.println(result.getError());
      return;
    }
  }

  /**
   * Command-line arguments, excluding the path to the executable, that will be
   * parsed.
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
  private Main(List<String> parsed) {
    this.parsed = parsed;
  }

  /**
   * Parses the command line arguments to a launcher.
   *
   * @return Launcher with the parsed options and command.
   */
  private Result<Launcher, String> parse() {
    // Initialize mutable state.
    current = 0;
    return parseOptions().bind(
      options -> parseCommand().map(command -> new Launcher(options, command)));
  }

  /**
   * Parse the options.
   *
   * @return All parsed options.
   */
  private Result<Map<Class<? extends Option>, Option>, String> parseOptions() {
    // Parse an option until cannot.
    var options = new HashMap<Class<? extends Option>, Option>();
    while (true) {
      var optionResult = parseOption();
      if (optionResult.isFailed())
        return optionResult.propagate();
      if (optionResult.getValue().isEmpty())
        break;
      var option = optionResult.getValue().get();
      options.put(option.getClass(), option);
    }
    return Result.ofSuccess(options);
  }

  /**
   * Tries to parse an option.
   *
   * @return Whether an option was parsed.
   */
  private Result<Optional<Option>, String> parseOption() {
    // Check whether there is an argument.
    if (!has())
      return Result.ofSuccess(Optional.empty());
    var option = consume();

    // Check whether the argument can be an option shortcut.
    if (option.length() == 2 && option.charAt(0) == '-') {
      var shortcut = option.charAt(1);
      return switch (shortcut) {
        case 'd' -> parseDirectory(option).map(Optional::of);
        default -> Result.ofFailure(
          """
          Could not recognize option shortcut `%s`!
          Use:
           - directory (d): set workspace directory
          """.formatted(shortcut));
      };

    }

    // Check whether the argument can be an option name.
    if (option.length() > 2 && option.startsWith("--")) {
      var name = option.substring(2);
      return switch (name) {
        case "directory" -> parseDirectory(option).map(Optional::of);
        default -> Result.ofFailure(
          """
          Could not recognize option name `%s`!
          Use:
           - directory (d): set workspace directory
          """.formatted(name));
      };
    }

    // Not an option if none matched. Need to roll back the consumed argument.
    current--;
    return Result.ofSuccess(Optional.empty());
  }

  /**
   * Parse a directory option.
   *
   * @param option Argument that indicated the option. Used for reporting to
   *               user on error.
   */
  private Result<Option, String> parseDirectory(String option) {
    // Check whether the path to workspace directory argument exists.
    if (!has())
      return Result.ofFailure(
        "Expected workspace directory argument for directory option after `%s`!"
          .formatted(option));
    var argument = consume();

    // Try converting argument to a path.
    try {
      var workspace = Path.of(argument);
      var file      = workspace.toFile();

      // Check the given workspace path.
      if (!file.exists())
        return Result.ofFailure(
          "Given workspace path `%s` for the option `%s` does not exists!"
            .formatted(argument, option));
      if (!file.isDirectory())
        return Result.ofFailure(
          "Given workspace path `%s` for the option `%s` is not a directory!"
            .formatted(argument, option));
      if (!file.canRead())
        return Result.ofFailure(
          "Given workspace path `%s` for the option `%s` is not readable!"
            .formatted(argument, option));
      if (!file.canWrite())
        return Result.ofFailure(
          "Given workspace path `%s` for the option `%s` is not writable!"
            .formatted(argument, option));

      return Result.ofSuccess(new Directory(workspace));
    } catch (InvalidPathException exception) {
      return Result.ofFailure(
        "Given workspace argument `%s` for the option `%s` is not a path!%n%s"
          .formatted(argument, option, exception.getLocalizedMessage()));
    }
  }

  /**
   * Parse the commands.
   *
   * @return Parsed command.
   */
  private Result<Command, String> parseCommand() {
    // Check whether a command is given.
    if (!has())
      return Result.ofFailure(
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
      case "new", "n" -> parseNew();
      case "check", "c" -> parseCheck();
      case "test", "t" -> parseTest();
      case "build", "b" -> parseBuild();
      case "run", "r" -> parseRun();
      default -> Result.<Command, String>ofFailure(
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
    result = result.check(c -> !has(),
      () -> "Unexpected arguments `%s` after the command `%s`!"
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
  private Result<Command, String> parseNew() {
    // Check whether a name for the created package is given.
    if (!has())
      return Result.ofFailure(
        "Expected a name for the new package that will be created!");
    var argument = consume();

    return PhysicalName.of(argument).map(New::new);
  }

  /**
   * Parse a check command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Result<Command, String> parseCheck() {
    return uniqueRemaining().bind(
      arguments -> Result.combine(
        arguments.stream().map(PhysicalName::of).toList(),
        names -> new Check(names), errors -> String.join(System.lineSeparator(),
          errors.toArray(String[]::new))));
  }

  /**
   * Parse a test command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Result<Command, String> parseTest() {
    return uniqueRemaining().bind(
      arguments -> Result.combine(
        arguments.stream().map(PhysicalName::of).toList(),
        names -> new Test(names), errors -> String.join(System.lineSeparator(),
          errors.toArray(String[]::new))));
  }

  /**
   * Parse a build command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Result<Command, String> parseBuild() {
    // Check whether a name for the built package is given.
    if (!has())
      return Result.ofFailure(
        "Expected a name for the executable package that will be built!");
    var argument = consume();

    return PhysicalName.of(argument).map(Build::new);
  }

  /**
   * Parse a run command.
   *
   * @param command Argument that indicated the command. Used for reporting to
   *                user on error.
   *
   * @return Parsed command.
   */
  private Result<Command, String> parseRun() {
    // Check whether a name for the run package is given.
    if (!has())
      return Result.ofFailure(
        "Expected a name for the executable package that will be run!");
    var argument = consume();

    return PhysicalName.of(argument).map(name -> new Run(name, remaining()));
  }

  /**
   * Checks uniqueness of the remaining arguments.
   *
   * @return Set of remaining arguments.
   */
  private Result<Set<String>, String> uniqueRemaining() {
    var arguments       = remaining();
    var uniqueArguments = arguments.stream().collect(Collectors.toSet());
    if (uniqueArguments.size() < arguments.size())
      return Result.ofFailure("There are duplicate arguments!");
    return Result.ofSuccess(uniqueArguments);
  }

  /**
   * Returns and consumes all the remaining arguments.
   *
   * @return Arguments from the current one to the last one.
   */
  private List<String> remaining() {
    var result = parsed.subList(current, parsed.size());
    current = parsed.size();
    return result;
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
   * Returns the current argument and goes to the next one.
   *
   * @return Currently parsed argument.
   */
  private String consume() {
    return parsed.get(current++);
  }
}
