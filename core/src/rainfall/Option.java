package rainfall;

import java.nio.file.Path;

public sealed abstract class Option {
  public static final class Directory extends Option {
    private final Path path;
    private Directory(Path path) { this.path = path; }

    public Path path() { return path; }
  }

  public static Directory directory(Path path) { return new Directory(path); }
}
