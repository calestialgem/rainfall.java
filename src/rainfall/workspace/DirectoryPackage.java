package rainfall.workspace;

/**
 * Represents a Thrice package, which is structured as a directory. Thrice
 * sources are modeled with the given extra data.
 */
public final class DirectoryPackage<Model> extends Package<Model> {
  /**
   * Contents of the package.
   */
  public final Module<Model> contents;

  /**
   * Constructs a directory package.
   *
   * @param contents Directory that is the contents of the constructed directory
   *                 package.
   */
  public DirectoryPackage(Module<Model> contents) {
    this.contents = contents;
  }
}
