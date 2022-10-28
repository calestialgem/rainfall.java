package rainfall.workspace;

import java.util.Map;

public record Module<Model>(PhysicalName name,
  Map<PhysicalName, Source<Model>> sources,
  Map<PhysicalName, Module<Model>> submodules) {}
