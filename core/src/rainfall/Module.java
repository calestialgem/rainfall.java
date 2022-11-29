package rainfall;

import java.nio.file.Path;
import java.util.Map;

record Module<Model>(Path path, String name, Map<String, Source<Model>> sources,
  Map<String, Module<Model>> submodules) {}
