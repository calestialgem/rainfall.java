package rainfall.workspace;

import java.util.Map;

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
  public final Map<PhysicalName, Source<Model>> sources;

  /**
   * Portion of the contained submodules that are directly under the module,
   * which means the they are entires of the module's directory. Thus, does not
   * include the submodules that are contained by the submodules of the module.
   */
  public final Map<PhysicalName, Module<Model>> submodules;

  /**
   * Constructs a module.
   *
   * @param name       Name of the constructed module.
   * @param sources    Sources directly under the constructed module.
   * @param submodules Submodules directly under the constructed module.
   */
  public Module(PhysicalName name, Map<PhysicalName, Source<Model>> sources,
    Map<PhysicalName, Module<Model>> submodules) {
    this.name       = name;
    this.sources    = sources;
    this.submodules = submodules;
  }

  /**
   * Checks whether there are no source files contained by the module.
   *
   * @return Whether there are not any source files contained by the module.
   */
  public boolean isEmpty() {
    return sources.isEmpty()
      && submodules.values().stream().allMatch(Module::isEmpty);
  }
}
