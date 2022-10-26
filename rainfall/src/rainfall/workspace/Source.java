package rainfall.workspace;

import java.nio.file.Path;

public record Source<Model> (Designation designation, Path file, Model model) {}
