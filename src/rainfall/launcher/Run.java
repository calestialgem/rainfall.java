package rainfall.launcher;

import java.util.List;

import rainfall.workspace.PhysicalName;

/**
 * Runs the given executable package if it builds.
 */
public final class Run extends Command {
  /**
   * Name of the executable package that will be run.
   */
  public final PhysicalName run;

  /**
   * Arguments that will be passed to the run executable.
   */
  public final List<String> passed;

  /**
   * Constructs a run command.
   *
   * @param run    Name of the executable package that will be run by the
   *               constructed run command.
   * @param passed Arguments that will be passed to the executable that will be
   *               run by the constructed run command.
   */
  public Run(PhysicalName run, List<String> passed) {
    this.run    = run;
    this.passed = passed;
  }
}
