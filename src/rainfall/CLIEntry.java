package rainfall;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import rainfall.launcher.Command;
import rainfall.launcher.Launcher;
import rainfall.launcher.Option;
import rainfall.utility.Box;
import rainfall.utility.Message;
import rainfall.utility.Result;
import rainfall.utility.Tester;
import rainfall.workspace.PhysicalName;

/**
 * Command-line interface of the compiler.
 *
 * @author calestialgem
 */
class CLIEntry {
  /**
   * Command-line arguments that are parsed.
   */
  private final List<String> parsed;

  /**
   * Index of the currently parsed command-line argument.
   */
  private int current;

  /**
   * Entry to the compiler from the command-line interface.
   *
   * @param arguments Arguments provided from the command-line.
   */
  public static void main(final String[] arguments) {
    // Stop if there are any unit tests that fail.
    if (!runAllTests()) {
      System.err.println("Stopping the compiler because of the failed tests!");
      return;
    }

    // Stop if it fails to parse the arguments.
    final var launcher = new CLIEntry(List.of(arguments)).parse();
    if (launcher.isFailure()) {
      Message.error(
        "Not launching the compiler because failed to parse the command-line arguments!",
        launcher.error()).report();
      return;
    }
  }

  /**
   * Runs all the unit tests for all the components of the compiler.
   *
   * @return Whether all unit tests passed.
   */
  private static boolean runAllTests() {
    final var tester = new Tester();
    Box.test(tester);
    Result.test(tester);
    PhysicalName.test(tester);
    Option.test(tester);
    CLIEntry.test(tester);
    return tester.report();
  }

  /**
   * Constructs a command line interface parser.
   *
   * @param parsed Arguments that are parsed by the constructed parser.
   */
  private CLIEntry(List<String> parsed) { this.parsed = parsed; }

  /**
   * Parses all the arguments.
   *
   * @return Parsed launcher, or error message.
   */
  private Result<Launcher, Message> parse() {
    // Initialize the mutable state.
    current = 0;

    // Parse an option until cannot.
    final var options = new HashMap<Class<? extends Option>, Option>();
    while (true) {
      // Parse the option and break if its not an option.
      final var option = parseOption();
      if (option.isFailure()) return option.propagate();
      if (option.value().isEmpty()) break;

      // Register the parsed option.
      final var registration = Option.register(options, option.value().get());
      if (registration.isFailure()) return registration.propagate();
    }

    // Parse the command.
    final var command = parseCommand();
    if (command.isFailure()) return command.propagate();

    return Result.success(new Launcher(command.value(), options));
  }

  /**
   * Parses an option at the current position.
   *
   * @return Parsed option, nothing, or the error message that was generated
   *           while parsing an option.
   */
  private Result<Box<Option>, Message> parseOption() {
    // If there is nothing, pass.
    if (!has()) return Result.success(Box.empty());
    final var option = advance();

    // Check whether the argument is an option shortcut.
    if (option.length() == 2 && option.charAt(0) == '-') {
      final var shortcut = option.charAt(1);
      return switch (shortcut) {
      case 'd' -> parseDirectory(option).map(Box::full);
      default -> Message.failure("""
        Unknown option shortcut `%s`! Use:
         - directory (d): sets workspace directory""".formatted(shortcut));
      };
    }

    // Check whether the argument is an option name.
    if (option.length() > 2 && option.startsWith("--")) {
      final var name = option.substring(2);
      return switch (name) {
      case "directory" -> parseDirectory(option).map(Box::full);
      default -> Message.failure("""
        Unknown option name `%s`! Use:
         - directory (d): sets workspace directory""".formatted(name));
      };
    }

    // If none matched, the argument was not an option; retreat.
    current--;
    return Result.success(Box.empty());
  }

  /**
   * Parses a directory option.
   *
   * @param  option Argument that indicated the parsed option. Used for
   *                  reporting in the error message.
   * @return        Parsed directory option, or error message.
   */
  private Result<Option, Message> parseDirectory(final String option) {
    // Check whether the path to the workspace directory exists.
    if (!has()) return Message
      .failure("Expected workspace directory argument after the `%s` option!"
        .formatted(option));
    final var argument = advance();

    // Try converting the argument to a path.
    try {
      return Result.success(new Option.Directory(Path.of(argument)));
    } catch (InvalidPathException exception) {
      return Message.failure("Workspace directory argument is not a path!",
        Message.error(exception));
    }
  }

  /**
   * Parses a command.
   *
   * @return Parsed command, or error message.
   */
  private Result<Command, Message> parseCommand() {
    // Check whether a command is given.
    if (!has()) return Message.failure("""
      There is no command given! Use:
       - new   (n): creates a package
       - check (c): checks packages
       - test  (t): tests packages
       - build (b): builds a package
       - run   (r): runs a package""");
    final var command = advance();

    // Dispatch over the command name or shortcut.
    final var parsedCommand = switch (command) {
    case "new", "n" -> parseNew(command);
    case "check", "c" -> parseCheck(command);
    case "test", "t" -> parseTest(command);
    case "build", "b" -> parseBuild(command);
    case "run", "r" -> parseRun(command);
    default -> Message.<Command>failure("""
      Could not recognize the given command `%s`! Use:
       - new   (n): creates a package
       - check (c): checks packages
       - test  (t): tests packages
       - build (b): builds a package
       - run   (r): runs a package""".formatted(command));
    };

    // Check whether all the arguments are consumed.
    if (parsedCommand.isSuccess() && has()) return Message.failure(
      "Unexpected arguments after `%s` command!".formatted(command),
      advanceToEnd().stream().map(Message::error).toList());

    return parsedCommand;
  }

  /**
   * Parses a new command.
   *
   * @param  command Argument that indicated the parsed command. Used for
   *                   reporting in the error message.
   * @return         Parsed new command, or error message.
   */
  private Result<Command, Message> parseNew(final String command) {
    // Check whether a name for the created package is given.
    if (!has()) return Message.failure(
      "Expected a name for the package that will be created for `%s` command!"
        .formatted(command));
    return PhysicalName.of(advance()).map(Command.New::new);
  }

  /**
   * Parses a check command.
   *
   * @param  command Argument that indicated the parsed command. Used for
   *                   reporting in the error message.
   * @return         Parsed check command, or error message.
   */
  private Result<Command, Message> parseCheck(final String command) {
    return parseNames(command).map(Command.Check::new);
  }

  /**
   * Parses a test command.
   *
   * @param  command Argument that indicated the parsed command. Used for
   *                   reporting in the error message.
   * @return         Parsed test command, or error message.
   */
  private Result<Command, Message> parseTest(final String command) {
    return parseNames(command).map(Command.Test::new);
  }

  /**
   * Parses a build command.
   *
   * @param  command Argument that indicated the parsed command. Used for
   *                   reporting in the error message.
   * @return         Parsed build command, or error message.
   */
  private Result<Command, Message> parseBuild(final String command) {
    // Check whether a package is given to be built.
    if (!has()) return Message.failure(
      "Expected a name for the package that will be build for `%s` command!"
        .formatted(command));
    return PhysicalName.of(advance()).map(Command.Build::new);
  }

  /**
   * Parses a run command.
   *
   * @param  command Argument that indicated the parsed command. Used for
   *                   reporting in the error message.
   * @return         Parsed run command, or error message.
   */
  private Result<Command, Message> parseRun(final String command) {
    // Check whether a package is given to be run.
    if (!has()) return Message.failure(
      "Expected a name for the package that will be run for `%s` command!"
        .formatted(command));
    // Pass all the remaining arguments to the command.
    return PhysicalName.of(advance())
      .map(name -> new Command.Run(name, advanceToEnd()));
  }

  /**
   * Parses all the remaining arguments as package names.
   *
   * @param  command Argument that indicated the parsed command, which takes
   *                   package names as arguments. Used for reporting in the
   *                   error message.
   * @return         Parsed package names, or error message.
   */
  private Result<List<PhysicalName>, Message> parseNames(final String command) {
    // Check whether all the remaining arguments are unique.
    final var arguments       = advanceToEnd();
    final var uniqueArguments = arguments.stream().collect(Collectors.toSet());
    if (uniqueArguments.size() < arguments.size()) return Message
      .failure("Repeated names are given to `%s` command!".formatted(command));

    // Try to parse names, and combine them to a list if all are valid.
    // Concatenate error messages if there are any invalid ones.
    return Result
      .combine(uniqueArguments.stream().map(PhysicalName::of).toList(),
        Function.identity(),
        errors -> Message.error(
          "Invalid names are given to `%s` command!".formatted(command),
          errors));
  }

  /**
   * Skips over all the arguments left.
   *
   * @return Remaining arguments.
   */
  private List<String> advanceToEnd() {
    final var remaining = parsed.subList(current, parsed.size());
    current = parsed.size();
    return remaining;
  }

  /**
   * Skips over the current argument.
   *
   * @return Current argument.
   */
  private String advance() { return parsed.get(current++); }

  /**
   * Checks current index.
   *
   * @return Whether there is a current argument.
   */
  private boolean has() { return current < parsed.size(); }

  /**
   * Runs all the unit tests for {@link CLIEntry}.
   *
   * @param tester Used unit test runner.
   */
  private static void test(Tester tester) {
    // Test whether parser runs over the bounds of the arguments.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("a"));
      parser.current = 0;
      return parser.has();
    });

    // Test whether parser advances correctly.
    tester.run(() -> {
      final var value  = "a";
      final var parser = new CLIEntry(List.of(value));
      parser.current = 0;
      return value.equals(parser.advance()) && parser.current == 1;
    });

    // Test whether parses advances over all the remaining arguments.
    tester.run(() -> {
      final var value  = List.of("a", "b");
      final var parser = new CLIEntry(value);
      parser.current = 0;
      return value.equals(parser.advanceToEnd())
        && parser.current == value.size();
    });

    // Test whether name uniqueness is checked.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "A"));
      parser.current = 0;
      return parser.parseNames("c").isFailure();
    });

    // Test whether names are parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "B"));
      parser.current = 0;
      return parser.parseNames("c").isSuccess();
    });

    // Test whether new command parser expects a package name.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseNew("n").isFailure();
    });

    // Test whether a new command is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A"));
      parser.current = 0;
      return parser.parseNew("n").isSuccess();
    });

    // Test whether build command parser expects a package name.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseBuild("b").isFailure();
    });

    // Test whether a build command is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A"));
      parser.current = 0;
      return parser.parseBuild("b").isSuccess();
    });

    // Test whether run command parser expects a package name.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseRun("r").isFailure();
    });

    // Test whether a run command is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A"));
      parser.current = 0;
      return parser.parseRun("r").isSuccess();
    });

    // Test whether a check command is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "B"));
      parser.current = 0;
      return parser.parseCheck("c").isSuccess();
    });

    // Test whether a test command is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "B"));
      parser.current = 0;
      return parser.parseTest("t").isSuccess();
    });

    // Test whether parser expects a command.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseCommand().isFailure();
    });

    // Test whether a new command shortcut is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("n", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.New;
    });

    // Test whether a new command name is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("new", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.New;
    });

    // Test whether a check command shortcut is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("c"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Check;
    });

    // Test whether a check command name is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("check"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Check;
    });

    // Test whether a test command shortcut is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("t"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Test;
    });

    // Test whether a test command name is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("test"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Test;
    });

    // Test whether a build command shortcut is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("b", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Build;
    });

    // Test whether a build command name is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("build", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Build;
    });

    // Test whether a run command shortcut is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("r", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Run;
    });

    // Test whether a run command name is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("run", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Run;
    });

    // Test whether an unknown command is checked.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("0"));
      parser.current = 0;
      return parser.parseCommand().isFailure();
    });

    // Test whether unused arguments are checked.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("new", "A", "0", "1"));
      parser.current = 0;
      return parser.parseCommand().isFailure();
    });

    // Test whether directory option parser checks invalid paths.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("\""));
      parser.current = 0;
      return parser.parseDirectory("-d").isFailure();
    });

    // Test whether a directory option is parsed correctly.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("."));
      parser.current = 0;
      return parser.parseDirectory("-d").isSuccess();
    });

    // Test whether option parser handles no arguments.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    // Test whether option parser differentiates single dash from a shortcut.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-"));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    // Test whether option parser differentiates two characters without a dash
    // from a shortcut.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("dd"));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    // Test whether option parser understands directory option shortcut.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-d", "."));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isFull()
        && parsed.value().get() instanceof Option.Directory;
    });

    // Test whether option parser checks unknown shortcuts.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-0"));
      parser.current = 0;
      return parser.parseOption().isFailure();
    });

    // Test whether option parser checks whether an option name starts with
    // double dashes.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("directory"));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    // Test whether option parser understands directory option name.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("--directory", "."));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isFull()
        && parsed.value().get() instanceof Option.Directory;
    });

    // Test whether option parser checks unknown names.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("--0"));
      parser.current = 0;
      return parser.parseOption().isFailure();
    });

    // Test whether parser checks option parsing failure.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("--0"));
      return parser.parse().isFailure();
    });

    // Test whether parser works without an option.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("c"));
      final var parsed = parser.parse();
      return parsed.isSuccess()
        && parsed.value().command() instanceof Command.Check;
    });

    // Test whether parser checks same option being given again.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-d", ".", "-d", "."));
      return parser.parse().isFailure();
    });

    // Test whether parser checks command parsing failure.
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("n"));
      return parser.parse().isFailure();
    });
  }
}
