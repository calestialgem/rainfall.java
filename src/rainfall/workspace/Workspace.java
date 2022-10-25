package rainfall.workspace;

import java.nio.file.*;
import java.util.*;

/**
 * Represents a Rainfall workspace. Thrice sources are modeled with the given
 * extra data.
 */
public final class Workspace<Model> {
  /**
   * Path to the workspace's directory.
   */
  public final Path path;

  /**
   * Packages in the workspace.
   */
  public final List<Package<Model>> packages;

  /**
   * Constructs a workspace.
   *
   * @param packages Packages in the constructed workspace.
   */
  public Workspace(Path path, List<Package<Model>> packages) {
    this.path     = path;
    this.packages = packages;
  }
}
