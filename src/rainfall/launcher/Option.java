package rainfall.launcher;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import rainfall.utility.Message;
import rainfall.utility.Result;
import rainfall.utility.Tester;

/**
 * Settings for a launch of the compiler.
 *
 * @author calestialgem
 */
public sealed interface Option {
  /**
   * Sets the path to the workspace directory, which is the current working
   * directory if this option is not set.
   *
   * @param  workspace Path to the workspace directory.
   * @author           calestialgem
   */
  record Directory(Path workspace) implements Option {}

  /**
   * Registers an option to a map.
   *
   * @param  options    Map that is the target of registration.
   * @param  registered Option that will be added to the map.
   * @return            Success, or error message.
   */
  static Result<Void, Message> register(
    final Map<Class<? extends Option>, Option> options,
    final Option registered) {
    // Check whether an option with the same type exists.
    if (options.containsKey(registered.getClass()))
      return Message.failure("Option is already registered!");

    // Put and return success.
    options.put(registered.getClass(), registered);
    return Result.success(null);
  }

  /**
   * Runs all the unit tests for {@link Option}.
   *
   * @param tester Used unit test runner.
   */
  static void test(final Tester tester) {
    // Test registering an option for the first time.
    tester.run(() -> {
      final var option  = new Directory(Path.of("."));
      final var options = new HashMap<Class<? extends Option>, Option>();
      return register(options, option).isSuccess();
    });

    // Test registering an option for the second time.
    tester.run(() -> {
      final var option  = new Directory(Path.of("."));
      final var options = new HashMap<Class<? extends Option>, Option>();
      return register(options, option).isSuccess()
        && register(options, option).isFailure();
    });
  }
}
