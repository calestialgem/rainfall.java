package rainfall.cli;

import rainfall.launcher.Command;
import rainfall.physical.Name;
import rainfall.utility.List;
import rainfall.utility.Message;
import rainfall.utility.Result;

public final class CommandLineInterface {
  public static void main(String[] arguments) {
    System.out.println("Hello, world!");
  }

  public static Result<Void, Message> parse() {
    return Result.failure(Message.info(
      """
        Rainfall Thrice Compiler v0.0.1
        usage: rainfall [options] <command> [arguments]
        options:
         * --directory (-d) <path>: set the workspace directory to the given path, defaults to working directory
        commands:
         * new   (n) <name>:             creates a new package
         * check (c) [names]:            checks the validity of the given or all packages
         * test  (t) [names]:            runs the tests for the given or all packages
         * build (b) <name>:             generates the C file of the given package
         * run   (r) <name> [arguments]: runs the given executable package"""));
  }
  public static Result<Command, Message> parse(String argument) {
    if (argument.equals("check")) {
      return Result.success(Command.check(List.of()));
    }
    return Message.failure("Provide a name for the created package!");
  }
  public static Result<Command, Message> parse(String... arguments) {
    return switch (arguments[0]) {
    case "new", "n" -> parseNewCommand(arguments);
    case "check" -> parseCheckCommand(arguments);
    default -> Message.failure(null);
    };
  }

  private static Result<Command, Message> parseNewCommand(String... arguments) {
    var errors = List.<Message>mutable();
    var name   = Name.of(arguments[1]);
    if (name.isFailure()) {
      errors.push(Message.error(
        "Package name for the created package was invalid!", name.error()));
    }
    if (arguments.length > 2) { errors.push(Message.error("""
      There were extra arguments:
       - extra
       - arguments""")); }
    if (errors.isFinite()) { return Result.failure(Message.group(errors)); }
    return Result.success(Command.new_(name.value()));
  }
  private static Result<Command, Message>
    parseCheckCommand(String... arguments) {
    var checked = List.<Name>mutable();
    var errors  = List.<Message>mutable();
    for (var argument = 1; argument < arguments.length; argument++) {
      var name = Name.of(arguments[argument]);
      if (name.isFailure()) {
        errors.push(Message.error("Checked package name `%s` is invalid!"
          .formatted(arguments[argument]), name.error()));
      } else {
        checked.push(name.value());
      }
    }
    if (errors.isFinite()) { return Result.failure(Message.group(errors)); }
    return Result.success(Command.check(checked));
  }
}
