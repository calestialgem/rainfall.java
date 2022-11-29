package rainfall;

import java.nio.file.Path;

record Source<Model>(Path path, Unicode name, Model model) {}
