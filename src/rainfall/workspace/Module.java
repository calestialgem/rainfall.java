package rainfall.workspace;

import java.util.*;

/**
 * Represents a Thrice module. Thrice sources are modeled with the given extra
 * data.
 */
public final class Module<Model> {
  /**
   * Name of the module.
   */
  public final PhysicalName name;

  /**
   * Portion of the contained sources that are directly under the module, which
   * means the they are entires of the module's directory. Thus, does not
   * include the sources that are contained by the submodules of the module.
   */
  public final List<Source<Model>> sources;

  /**
   * Portion of the contained submodules that are directly under the module,
   * which means the they are entires of the module's directory. Thus, does not
   * include the submodules that are contained by the submodules of the module.
   */
  public final List<Module<Model>> submodules;

  /**
   * Constructs a module.
   *
   * @param name       Name of the constructed module.
   * @param sources    Sources directly under the constructed module.
   * @param submodules Submodules directly under the constructed module.
   */
  public Module(PhysicalName name, List<Source<Model>> sources,
    List<Module<Model>> submodules) {
    this.name       = name;
    this.sources    = sources;
    this.submodules = submodules;
  }
}
