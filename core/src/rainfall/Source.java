package rainfall;

import java.nio.file.Path;

record Source<Model>(Path path, String name, Model model) {}
