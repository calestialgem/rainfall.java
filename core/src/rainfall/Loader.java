package rainfall;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

final class Loader {
  static Workspace<Linear> load(Path loaded) {
    var packages = new HashMap<String, Package<Linear>>();

    for (var entry : loaded.toFile().listFiles()) {
      if (entry.isFile()) {
        var source = loadSource(entry.toPath());
        if (source.isPresent()) {
          packages.put(source.get().name(), new Package.File<>(source.get()));
        }
        continue;
      }
      if (entry.isDirectory()) {
        var module = loadModule(entry.toPath());
        if (module.isPresent()) {
          packages.put(module.get().name(),
            new Package.Directory<>(module.get()));
        }
      }
    }

    return new Workspace<>(Collections.unmodifiableMap(packages));
  }

  private static Optional<Module<Linear>> loadModule(Path loaded) {
    var name = loaded.getFileName().toString();
    checkName(name);

    var sources    = new HashMap<String, Source<Linear>>();
    var submodules = new HashMap<String, Module<Linear>>();

    for (var entry : loaded.toFile().listFiles()) {
      if (entry.isFile()) {
        var source = loadSource(entry.toPath());
        if (source.isPresent()) {
          sources.put(source.get().name(), source.get());
        }
        continue;
      }
      if (entry.isDirectory()) {
        var module = loadModule(entry.toPath());
        if (module.isPresent()) {
          submodules.put(module.get().name(), module.get());
        }
      }
    }

    if (sources.isEmpty() && submodules.isEmpty()) { return Optional.empty(); }

    return Optional
      .of(new Module<>(loaded, name, Collections.unmodifiableMap(sources),
        Collections.unmodifiableMap(submodules)));
  }

  private static Optional<Source<Linear>> loadSource(Path loaded) {
    var fileName = loaded.getFileName().toString();
    if (!fileName.endsWith(".tr")) { return Optional.empty(); }
    var name = fileName.substring(0, fileName.length() - 3);
    checkName(name);
    return Optional.of(new Source<>(loaded, name, new Loader(loaded).load()));
  }

  private static void checkName(String name) {
    if (name.isEmpty()) throw new RuntimeException();
    if (name.charAt(name.length() - 1) == '_') {
      var initialPortion = name.substring(0, name.length() - 1);
      if (!Lexeme.KEYWORDS.containsKey(initialPortion))
        throw new RuntimeException();
    } else {
      if ((name.charAt(0) < 'a' || name.charAt(0) > 'z'))
        throw new RuntimeException();
      for (var i = 1; i < name.length();
        i++) if ((name.charAt(i) < 'a' || name.charAt(i) > 'z')
          && (name.charAt(i) < '0' || name.charAt(i) > '9'))
          throw new RuntimeException();
    }
  }

  private final Path loaded;

  private Loader(Path loaded) { this.loaded = loaded; }

  private Linear load() {
    try {
      return new Linear(
        Files.readString(loaded).replaceAll(System.lineSeparator(), "\n"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
