package rainfall.workspace;

/**
 * Represents a Thrice package. Thrice sources are modeled with the given extra
 * data.
 */
public final class Package<Model> {
  /**
   * Module created by the package.
   */
  public final Module<Model> contents;

  /**
   * Constructs a package.
   *
   * @param contents Module that is the contents of the constructed package.
   */
  public Package(Module<Model> contents) {
    this.contents = contents;
  }
}
