package rainfall.workspace;

/**
 * Thrice package that is formed out of a module directory.
 */
public final class DirectoryPackage<Model> extends Package<Model> {
  /**
   * Module directory that forms the package.
   */
  public final Module<Model> contents;

  /**
   * Constructs a directory package.
   *
   * @param contents Contents of the constructed directory package.
   */
  public DirectoryPackage(Module<Model> contents) {
    this.contents = contents;
  }
}
