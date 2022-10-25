package rainfall.workspace;

import java.util.*;

/**
 * Represents a Rainfall workspace. Thrice sources are modeled with the given
 * extra data.
 */
public final class Workspace<Model> {
  /**
   * Packages in the workspace.
   */
  public final List<Package<Model>> packages;

  /**
   * Constructs a workspace.
   *
   * @param packages Packages in the constructed workspace.
   */
  public Workspace(List<Package<Model>> packages) {
    this.packages = packages;
  }
}
