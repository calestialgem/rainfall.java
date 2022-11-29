package rainfall;

import java.nio.file.Path;

record Source<Model>(Path path, UTF8 name, Model model) {}
