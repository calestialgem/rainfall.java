package rainfall.launcher;

import rainfall.workspace.*;

/**
 * Creates a new package with the given name in the workspace if a package
 * with the same name does not exists already.
 */
public final class New extends Command {
  /**
   * Name of the package that will be created.
   */
  public final PhysicalName created;

  /**
   * Constructs a new command.
   *
   * @param created Name of the package that will be created by the constructed
   *                new command.
   */
  public New(PhysicalName created) {
    this.created = created;
  }
}
