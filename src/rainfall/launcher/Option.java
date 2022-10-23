package rainfall.launcher;

/**
 * A setting given for the executed command.
 */
public enum Option {
  /**
   * Provides the workspace directory. Defaults to the current working directory
   * when left out.
   */
  Directory("directory", 'd');

  /**
   * Name of the option.
   */
  public final String name;

  /**
   * Single character shortcut of the option.
   */
  public final char shortcut;

  /**
   * Constructs an option.
   *
   * @param name     Name of the constructed option.
   * @param shortcut Single character shortcut of the constructed option.
   */
  private Option(String name, char shortcut) {
    this.name     = name;
    this.shortcut = shortcut;
  }

}
