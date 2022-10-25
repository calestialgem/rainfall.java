package rainfall.workspace;

/**
 * Represents a Thrice package, which is structured as a file. Thrice
 * sources are modeled with the given extra data.
 */
public final class FilePackage<Model> extends Package<Model> {
  /**
   * Contents of the package.
   */
  public final Source<Model> contents;

  /**
   * Constructs a file package.
   *
   * @param contents File that is the contents of the constructed file package.
   */
  public FilePackage(Source<Model> contents) {
    this.contents = contents;
  }
}
