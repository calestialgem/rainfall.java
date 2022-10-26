package rainfall;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import rainfall.launcher.Command;
import rainfall.launcher.Launcher;
import rainfall.launcher.Option;
import rainfall.workspace.Designation;

class CommandLineEntry {
  public static void main(String[] args) {
    var launcher = new CommandLineEntry(List.of(args)).enter();
    if (launcher.isFailure()) {
      System.err.printf("Failed to parse the arguments due to:%n%s",
          launcher.getError());
      return;
    }
  }

  private final List<String> arguments;

  private CommandLineEntry(List<String> arguments) {
    this.arguments = arguments;
  }

  private int current;

  private Result<Launcher, String> enter() {
    // Initialize mutable state.
    current = 0;
    // Parse all the options until cannot.
    var options = new HashMap<Class<? extends Option>, Option>();
    while (true) {
      var parsed = parseOption();
      if (parsed.isFailure()) return parsed.propagate();
      if (parsed.get().isEmpty()) break;
      var registeration = Option.register(options, parsed.get().get());
      if (registeration.isFailure()) registeration.propagate();
    }
    // Parse the command.
    var command = parseCommand();
    if (command.isFailure()) return command.propagate();
    return Result.success(new Launcher(options, command.get()));
  }

  private Result<Box<Option>, String> parseOption() {
    // Check whether there is an argument.
    if (!has()) return Result.success(Box.empty());
    var option = take();
    // Check whether the option is a shortcut.
    if (option.length() == 2 && option.charAt(0) == '-') {
      var shortcut = option.charAt(1);
      return switch (shortcut) {
        case 'd' -> parseDirectory(option).map(Box::full);
        default -> Result.failure("""
          Could not recognize option shortcut `%c`! Use:
           - directory (d): sets workspace directory""".formatted(shortcut));
      };
    }
    // Check whether the option is a name.
    if (option.length() > 2 && option.startsWith(
        "--")) { var name = option.substring(2); return switch (name) {
          case "directory" -> parseDirectory(option).map(Box::full);
          default -> Result.failure("""
            Could not recognize option name `%s`! Use:
             - directory (d): sets workspace directory""".formatted(name));
        }; }
    // Roll back the taken argument since it was not an option.
    back();
    return Result.success(Box.empty());
  }

  private Result<Option.Directory, String> parseDirectory(String option) {
    // Check whether the path to the workspace directory exists.
    if (!has()) return Result
        .failure("Expected workspace directory argument after `%s` option!"
            .formatted(option));
    var argument = take();
    // Try converting the argument to a path.
    try {
      var path = Path.of(argument);
      var file = path.toFile();
      // Check the path.
      if (!file.exists()) return Result
          .failure("Given workspace path `%s` does not exist!".formatted(path));
      if (!file.isDirectory()) return Result.failure(
          "Given workspace path `%s` is not a directory!".formatted(path));
      if (!file.canRead()) return Result.failure(
          "Given workspace path `%s` is not a readable!".formatted(path));
      if (!file.canWrite()) return Result.failure(
          "Given workspace path `%s` is not a writable!".formatted(path));
      return Result.success(new Option.Directory(path));
    } catch (InvalidPathException exception) {
      return Result.failure(exception.getLocalizedMessage());
    }
  }

  private Result<Command, String> parseCommand() {
    // Check whether a command is given.
    if (!has()) Result.failure("""
      There is no command given! Use:
       - new   (n): creates a new package
       - check (c): checks packages
       - test  (t): tests packages
       - build (b): builds executable package
       - run   (r): runs executable package""");
    var command = take();
    // Dispatch command name or shortcut.
    var result = switch (command) {
      case "new  ", "n" -> parseNew(command);
      case "check", "c" -> parseCheck(command);
      case "test ", "t" -> parseTest(command);
      case "build", "b" -> parseBuild(command);
      case "run  ", "r" -> parseRun(command);
      default -> Result.<Command, String>failure("""
        Could not recognize command `%s`! Use:
        - new   (n): creates a new package
        - check (c): checks packages
        - test  (t): tests packages
        - build (b): builds executable package
        - run   (r): runs executable package""".formatted(command));
    };
    // Check whether all the arguments are consumed.
    if (!has()) result = Result
        .failure("Unexpected arguments `%s` after the command `%s`!".formatted(
            String.join(" ", takeAll().toArray(String[]::new)), command));
    return result;
  }

  private Result<Command, String> parseNew(String command) {
    // Check whether a name is given.
    if (!has()) return Result.failure(
        "Expected a name for the new package that will be created after `%s` command!"
            .formatted(command));
    var name = take();
    return Designation.of(name).map(Command.New::new);
  }

  private Result<Command, String> parseCheck(String command) {
    var remaining = takeAllUnique();
  }

  private Result<Command, String> parseTest(String command) {}

  private Result<Command, String> parseBuild(String command) {}

  private Result<Command, String> parseRun(String command) {}

  private Result<Set<String>, String> takeAllUnique() {
    var remaining       = takeAll();
    var uniqueArguments = remaining.stream().collect(Collectors.toSet());
    if (uniqueArguments.size() < remaining.size())
      return Result.failure("There are duplicate arguments!");
    return Result.success(uniqueArguments);
  }

  private List<String> takeAll() {
    var remaining = arguments.subList(current, arguments.size());
    current = arguments.size();
    return remaining;
  }

  private String take() { return arguments.get(current++); }

  private void back() { current--; }

  private boolean has() { return current < arguments.size(); }
}
