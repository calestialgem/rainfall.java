package rainfall.workspace;

import java.nio.file.Path;

/**
 * Represents a Thrice source.
 *
 * @param  <Model> Type of the model compiler constructed for the source.
 * @param  name    Name of the source.
 * @param  path    Path to the source file.
 * @param  model   Model compiler constructed for the source.
 * @author         calestialgem
 */
public record Source<Model>(PhysicalName name, Path path, Model model) {}
