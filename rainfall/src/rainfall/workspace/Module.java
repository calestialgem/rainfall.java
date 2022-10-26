package rainfall.workspace;

import java.nio.file.Path;
import java.util.Map;

public record Module<Model> (Designation designation, Path directory,
    Map<Designation, Source<Model>> sources,
    Map<Designation, Module<Model>> submodules) {
  public boolean isEmpty() {
    if (!sources.isEmpty()) return false;
    for (var submodule : submodules.values())
      if (!submodule.isEmpty()) return false;
    return true;
  }
}
