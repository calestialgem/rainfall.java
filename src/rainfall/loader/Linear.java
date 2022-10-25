package rainfall.loader;

/**
 * Model of the source as a linear collection of characters.
 */
public final class Linear {
  /**
   * Contents of the source's file.
   */
  public final char[] contents;

  /**
   * Constructs a linear model.
   *
   * @param contents Contents of the constructed linear model.
   */
  Linear(char[] contents) {
    this.contents = contents;
  }
}
