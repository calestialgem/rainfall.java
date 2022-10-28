package rainfall.workspace;

import java.nio.file.Path;

public record Source<Model>(PhysicalName name, Path path, Model model) {}
