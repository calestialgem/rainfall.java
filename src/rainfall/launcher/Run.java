package rainfall.launcher;

import java.util.*;

import rainfall.workspace.*;

/**
 * Runs the given executable package if it builds.
 */
public final class Run extends Command {
  /**
   * Name of the executable package that will be run.
   */
  public final Name run;

  /**
   * Arguments that will be passed to the run executable.
   */
  public final List<Name> passed;

  /**
   * Constructs a run command.
   *
   * @param run    Name of the executable package that will be run by the
   *               constructed run command.
   * @param passed Arguments that will be passed to the executable that will be
   *               run by the constructed run command.
   */
  public Run(Name run, List<Name> passed) {
    this.run    = run;
    this.passed = passed;
  }
}
