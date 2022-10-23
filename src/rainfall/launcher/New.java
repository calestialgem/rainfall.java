package rainfall.launcher;

/**
 * Creates a new package with the given name in the workspace if a package
 * with the same name does not exists already.
 */
public final class New extends Command {
  /**
   * Name of the package that will be created.
   */
  public final String created;

  /**
   * Constructs a new command.
   *
   * @param created Name of the package that will be created by the constructed
   *                new command.
   */
  public New(String created) {
    this.created = created;
  }
}
