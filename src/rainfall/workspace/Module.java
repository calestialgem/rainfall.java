package rainfall.workspace;

import java.util.Map;

/**
 * Represents a Thrice module.
 *
 * @param  <Model>    Type of the model compiler constructed for the sources
 *                      contained by the module.
 * @param  name       Name of the module.
 * @param  sources    Sources that are directly under the module. Does not
 *                      include sources that are contained by the module's
 *                      submodules.
 * @param  submodules Submodules that are directly under the module. Does not
 *                      include submodules that are contained by the module's
 *                      submodules.
 * @author            calestialgem
 */
public record Module<Model>(PhysicalName name,
  Map<PhysicalName, Source<Model>> sources,
  Map<PhysicalName, Module<Model>> submodules) {}
