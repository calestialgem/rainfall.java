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
import rainfall.utility.Result;
import rainfall.utility.Tester;
import rainfall.workspace.PhysicalName;

class CLIEntry {
  private final List<String> parsed;
  private int                current;

  public static void main(final String[] arguments) {
    if (!runAllTests()) {
      System.err.println("Stopping the compiler because of the failed tests!");
      return;
    }

    final var launcher = new CLIEntry(List.of(arguments)).parse();
    if (launcher.isFailure()) {
      System.err.println(launcher.error());
      return;
    }
  }

  private static boolean runAllTests() {
    final var tester = new Tester();
    Box.test(tester);
    Result.test(tester);
    PhysicalName.test(tester);
    Option.test(tester);
    CLIEntry.test(tester);
    return tester.report();
  }

  private CLIEntry(List<String> parsed) { this.parsed = parsed; }

  private Result<Launcher, String> parse() {
    current = 0;

    final var options = new HashMap<Class<? extends Option>, Option>();
    while (true) {
      final var option = parseOption();
      if (option.isFailure()) return option.propagate();
      if (option.value().isEmpty()) break;
      final var registration = Option.register(options, option.value().get());
      if (registration.isFailure()) return registration.propagate();
    }

    final var command = parseCommand();
    if (command.isFailure()) return command.propagate();

    return Result.success(new Launcher(command.value(), options));
  }

  private Result<Box<Option>, String> parseOption() {
    if (!has()) return Result.success(Box.empty());
    final var option = advance();

    if (option.length() == 2 && option.charAt(0) == '-') {
      final var shortcut = option.charAt(1);
      return switch (shortcut) {
      case 'd' -> parseDirectory(option).map(Box::full);
      default -> Result.failure("""
        Unknown option shortcut `%s`! Use:
         - directory (d): sets workspace directory""".formatted(shortcut));
      };
    }

    if (option.length() > 2 && option.startsWith("--")) {
      final var name = option.substring(2);
      return switch (name) {
      case "directory" -> parseDirectory(option).map(Box::full);
      default -> Result.failure("""
        Unknown option name `%s`! Use:
         - directory (d): sets workspace directory""".formatted(name));
      };
    }

    current--;
    return Result.success(Box.empty());
  }

  private Result<Option, String> parseDirectory(final String option) {
    if (!has()) return Result
      .failure("Expected workspace directory argument after the `%s` option!"
        .formatted(option));
    final var argument = advance();

    try {
      return Result.success(new Option.Directory(Path.of(argument)));
    } catch (InvalidPathException exception) {
      return Result.failure(exception.getLocalizedMessage());
    }
  }

  private Result<Command, String> parseCommand() {
    if (!has()) return Result.failure("""
      There is no command given! Use:
       - new   (n): creates a package
       - check (c): checks packages
       - test  (t): tests packages
       - build (b): builds a package
       - run   (r): runs a package""");
    final var command = advance();

    final var parsedCommand = switch (command) {
    case "new", "n" -> parseNew(command);
    case "check", "c" -> parseCheck(command);
    case "test", "t" -> parseTest(command);
    case "build", "b" -> parseBuild(command);
    case "run", "r" -> parseRun(command);
    default -> Result.<Command, String>failure("""
      Could not recognize the given command `%s`! Use:
       - new   (n): creates a package
       - check (c): checks packages
       - test  (t): tests packages
       - build (b): builds a package
       - run   (r): runs a package""".formatted(command));
    };

    if (parsedCommand.isSuccess() && has()) return Result.failure(
      "Unexpected arguments after `%s` command!%n%s".formatted(command, String
        .join(System.lineSeparator(), advanceToEnd().toArray(String[]::new))));

    return parsedCommand;
  }

  private Result<Command, String> parseNew(final String command) {
    if (!has()) return Result.failure(
      "Expected a name for the package that will be created for `%s` command!"
        .formatted(command));
    return PhysicalName.of(advance()).map(Command.New::new);
  }

  private Result<Command, String> parseCheck(final String command) {
    return parseNames(command).map(Command.Check::new);
  }

  private Result<Command, String> parseTest(final String command) {
    return parseNames(command).map(Command.Test::new);
  }

  private Result<Command, String> parseBuild(final String command) {
    if (!has()) return Result.failure(
      "Expected a name for the package that will be build for `%s` command!"
        .formatted(command));
    return PhysicalName.of(advance()).map(Command.Build::new);
  }

  private Result<Command, String> parseRun(final String command) {
    if (!has()) return Result.failure(
      "Expected a name for the package that will be run for `%s` command!"
        .formatted(command));
    return PhysicalName.of(advance())
      .map(name -> new Command.Run(name, advanceToEnd()));
  }

  private Result<List<PhysicalName>, String> parseNames(final String command) {
    final var arguments       = advanceToEnd();
    final var uniqueArguments = arguments.stream().collect(Collectors.toSet());
    if (uniqueArguments.size() < arguments.size()) return Result
      .failure("Repeated names are given to `%s` command!".formatted(command));

    return Result.combine(
      uniqueArguments.stream().map(PhysicalName::of).toList(),
      Function.identity(),
      errors -> "Invalid names are given to `%s` command".formatted(command,
        String.join(System.lineSeparator(), errors.toArray(String[]::new))));
  }

  private List<String> advanceToEnd() {
    final var remaining = parsed.subList(current, parsed.size());
    current = parsed.size();
    return remaining;
  }

  private String advance() { return parsed.get(current++); }
  private boolean has() { return current < parsed.size(); }

  private static void test(Tester tester) {
    tester.run(() -> {
      final var parser = new CLIEntry(List.of("a"));
      parser.current = 0;
      return parser.has();
    });

    tester.run(() -> {
      final var value  = "a";
      final var parser = new CLIEntry(List.of(value));
      parser.current = 0;
      return value.equals(parser.advance()) && parser.current == 1;
    });

    tester.run(() -> {
      final var value  = List.of("a", "b");
      final var parser = new CLIEntry(value);
      parser.current = 0;
      return value.equals(parser.advanceToEnd())
        && parser.current == value.size();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "A"));
      parser.current = 0;
      return parser.parseNames("c").isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "B"));
      parser.current = 0;
      return parser.parseNames("c").isSuccess();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseNew("n").isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A"));
      parser.current = 0;
      return parser.parseNew("n").isSuccess();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseBuild("b").isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A"));
      parser.current = 0;
      return parser.parseBuild("b").isSuccess();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseRun("r").isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A"));
      parser.current = 0;
      return parser.parseRun("r").isSuccess();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "B"));
      parser.current = 0;
      return parser.parseCheck("c").isSuccess();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("A", "B"));
      parser.current = 0;
      return parser.parseTest("t").isSuccess();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      return parser.parseCommand().isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("n", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.New;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("new", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.New;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("c"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Check;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("check"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Check;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("t"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Test;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("test"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Test;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("b", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Build;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("build", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Build;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("r", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Run;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("run", "A"));
      parser.current = 0;
      final var parsed = parser.parseCommand();
      return parsed.isSuccess() && parsed.value() instanceof Command.Run;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("0"));
      parser.current = 0;
      return parser.parseCommand().isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("new", "A", "0", "1"));
      parser.current = 0;
      return parser.parseCommand().isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("\""));
      parser.current = 0;
      return parser.parseDirectory("-d").isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("."));
      parser.current = 0;
      return parser.parseDirectory("-d").isSuccess();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of());
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-"));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("dd"));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-d", "."));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isFull()
        && parsed.value().get() instanceof Option.Directory;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-0"));
      parser.current = 0;
      return parser.parseOption().isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("directory"));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isEmpty();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("--directory", "."));
      parser.current = 0;
      final var parsed = parser.parseOption();
      return parsed.isSuccess() && parsed.value().isFull()
        && parsed.value().get() instanceof Option.Directory;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("--0"));
      parser.current = 0;
      return parser.parseOption().isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("--0"));
      return parser.parse().isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("c"));
      final var parsed = parser.parse();
      return parsed.isSuccess()
        && parsed.value().command() instanceof Command.Check;
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("-d", ".", "-d", "."));
      return parser.parse().isFailure();
    });

    tester.run(() -> {
      final var parser = new CLIEntry(List.of("n"));
      return parser.parse().isFailure();
    });
  }
}
