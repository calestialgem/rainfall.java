package rainfall.launcher;

import java.nio.file.Path;
import java.util.Map;

import rainfall.Result;

public sealed interface Option {
  record Directory(Path path) implements Option {}

  static Result<Void, String>
      register(Map<Class<? extends Option>, Option> target, Option inserted) {
    // Check whether an option with the same type is already registered.
    if (target.containsKey(inserted.getClass()))
      return Result.failure("Option is already in the set!");
    // Register the option to its type.
    target.put(inserted.getClass(), inserted);
    return Result.success(null);
  }
}
