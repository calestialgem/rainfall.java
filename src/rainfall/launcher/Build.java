package rainfall.launcher;

/**
 * Generates the C file of the given executable package if it and all its
 * dependencies are valid.
 */
public final class Build extends Command {
  /**
   * Name of the executable package that will be built.
   */
  public final String built;

  /**
   * Constructs a build command.
   *
   * @param built Name of the executable package that will be built by the
   *              constructed build command.
   */
  public Build(String built) {
    this.built = built;
  }
}
