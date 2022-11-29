package rainfall;

import java.nio.file.Path;
import java.util.Map;

record Module<Model>(Path path, UTF8 name, Map<UTF8, Source<Model>> sources,
  Map<UTF8, Module<Model>> submodules) {}
