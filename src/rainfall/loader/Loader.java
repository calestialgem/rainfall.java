package rainfall.loader;

import java.nio.file.Path;
import java.util.ArrayList;

import rainfall.workspace.Package;
import rainfall.workspace.Workspace;

/**
 * Loads the packages in the workspace.
 */
public final class Loader {
  /**
   * Path to the directory of the workspace that is loaded.
   */
  public final Path loaded;

  /**
   * Constructs a loader.
   *
   * @param loaded Path to the workspace directory that is loaded by the
   *               constructed loader.
   */
  public Loader(Path loaded) {
    this.loaded = loaded;
  }

  /**
   * Loads the workspace.
   *
   * @return Linearly modelled workspace.
   */
  public Workspace<Linear> load() {
    var packages = new ArrayList<Package<Linear>>();
    return null;
  }
}
