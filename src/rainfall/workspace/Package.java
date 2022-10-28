package rainfall.workspace;

/**
 * Represents a Thrice package.
 *
 * @param  <Model> Type of the model compiler constructed for the sources
 *                   contained by the package.
 * @author         calestialgem
 */
public sealed interface Package<Model> {
  /**
   * Represents a Thrice package that is formed out of a source file.
   *
   * @param  <Model>  Type of the model compiler constructed for the source
   *                    contained by the package.
   * @param  contents Source that forms the package.
   * @author          calestialgem
   */
  record File<Model>(Source<Model> contents) implements Package<Model> {}

  /**
   * Represents a Thrice package that is formed out of a module directory.
   *
   * @param  <Model>  Type of the model compiler constructed for the sources
   *                    contained by the package.
   * @param  contents Module that forms the package.
   * @author          calestialgem
   */
  record Directory<Model>(Module<Model> contents) implements Package<Model> {}
}
