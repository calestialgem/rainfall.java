package rainfall.launcher;

/**
 * The request for compiler to execute in launch.
 */
public enum Command {
  /**
   * Generates the C file of the given executable package if it and all its
   * dependencies are valid.
   */
  Build("build", 'b'),

  /**
   * Checks the validity of the given package or packages, or all the packages
   * in the workspace if none is given.
   */
  Check("check", 'c'),

  /**
   * Creates a new package with the given name in the workspace if a package
   * with the same name does not exists already.
   */
  New("new", 'n'),

  /**
   * Runs the given executable package if it builds.
   */
  Run("run", 'r'),

  /**
   * Tests the given package or packages, or all the packages in the workspace
   * if none is given.
   */
  Test("test", 't');

  /**
   * Name of the command.
   */
  public final String name;

  /**
   * Single character shortcut of the command.
   */
  public final char shortcut;

  /**
   * Constructs a command.
   *
   * @param name     Name of the constructed option command.
   * @param shortcut Single character shortcut of the constructed command.
   */
  private Command(String name, char shortcut) {
    this.name     = name;
    this.shortcut = shortcut;
  }
}
