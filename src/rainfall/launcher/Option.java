package rainfall.launcher;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import rainfall.utility.Result;
import rainfall.utility.Tester;

public sealed interface Option {
  record Directory(Path workspace) implements Option {}

  static Result<Void, String> register(
    final Map<Class<? extends Option>, Option> options,
    final Option registered) {
    if (options.containsKey(registered.getClass()))
      return Result.failure("Option is already registered!");

    options.put(registered.getClass(), registered);
    return Result.success(null);
  }

  static void test(final Tester tester) {
    final var option  = new Directory(Path.of("."));
    final var options = new HashMap<Class<? extends Option>, Option>();
    tester.run(register(options, option)::isSuccess);
    tester.run(register(options, option)::isFailure);
  }
}
