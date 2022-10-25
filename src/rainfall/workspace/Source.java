package rainfall.workspace;

import java.nio.file.*;

/**
 * Represents a Thrice source that is modeled with the given extra data.
 */
public final class Source<Model> {
  /**
   * Name of the source.
   */
  public final PhysicalName name;

  /**
   * Path to the source's file.
   */
  public final Path path;

  /**
   * Extra data that is used to model the source. This is used by the various
   * stages of the compiler to pass information while keeping the workspace
   * structure unchanged.
   */
  public final Model model;

  /**
   * Constructs a source.
   *
   * @param name  Name of the constructed source.
   * @param path  Path to the constructed source's file.
   * @param model Model data of the constructed source.
   */
  public Source(PhysicalName name, Path path, Model model) {
    this.name  = name;
    this.path  = path;
    this.model = model;
  }
}
