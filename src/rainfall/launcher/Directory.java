package rainfall.launcher;

import java.nio.file.*;

/**
 * Option that sets the path to the workspace directory. The current working
 * directory is taken as the workspace directory if its not provided.
 */
public final class Directory extends Option {
  /**
   * Path to the workspace directory.
   */
  public final Path workspace;

  /**
   * Constructs a directory.
   *
   * @param workspace Path to the workspace directory provided from the
   *                  constructed directory option.
   */
  public Directory(Path workspace) {
    this.workspace = workspace;
  }
}
