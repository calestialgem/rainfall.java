package rainfall;

import java.nio.file.Path;
import java.util.Map;

record Module<Model>(Path path, Unicode name,
  Map<Unicode, Source<Model>> sources,
  Map<Unicode, Module<Model>> submodules) {}
