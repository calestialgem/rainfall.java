package rainfall.launcher;

import java.nio.file.Path;
import java.util.Map;

import rainfall.utility.Message;
import rainfall.utility.Result;

public sealed abstract class Option {
  public static final class Directory extends Option {
    private final Path path;
    private Directory(Path path) { this.path = path; }

    public Path path() { return path; }
  }

  public static Directory directory(Path path) { return new Directory(path); }

  public static Result<Void, Message>
    register(Map<Class<? extends Option>, Option> target, Option registered) {
    if (target.containsKey(registered.getClass())) {
      return Message.failure("Option is already provided!");
    }
    target.put(registered.getClass(), registered);
    return Result.success(null);
  }
}
