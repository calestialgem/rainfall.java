package rainfall.workspace;

/**
 * Thrice package that is formed out of a source file.
 */
public final class FilePackage<Model> extends Package<Model> {
  /**
   * Source file that forms the package.
   */
  public final Source<Model> contents;

  /**
   * Constructs a file package.
   *
   * @param contents Contents of the constructed file package.
   */
  public FilePackage(Source<Model> contents) {
    this.contents = contents;
  }
}
