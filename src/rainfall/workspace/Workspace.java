package rainfall.workspace;

import java.nio.file.Path;
import java.util.Map;

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
  public final Map<PhysicalName, Package<Model>> packages;

  /**
   * Constructs a workspace.
   *
   * @param packages Packages in the constructed workspace.
   */
  public Workspace(Path path, Map<PhysicalName, Package<Model>> packages) {
    this.path     = path;
    this.packages = packages;
  }
}
