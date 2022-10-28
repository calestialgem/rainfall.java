package rainfall.launcher;

import java.util.List;

import rainfall.workspace.PhysicalName;

/**
 * A task that can be done by the compiler.
 *
 * @author calestialgem
 */
public sealed interface Command {
  /**
   * Creating a new package.
   *
   * @param  created Name of the newly created package.
   * @author         calestialgem
   */
  record New(PhysicalName created) implements Command {}

  /**
   * Validating the code in some or all the packages.
   *
   * @param  checked Packages that are asked to be checked. Checks all the
   *                   packages if its empty.
   * @author         calestialgem
   */
  record Check(List<PhysicalName> checked) implements Command {}

  /**
   * Running the unit tests for some or all the packages.
   *
   * @param  tested Packages that are asked to be tested. Tests all the packages
   *                  if its empty.
   * @author        calestialgem
   */
  record Test(List<PhysicalName> tested) implements Command {}

  /**
   * Compiling a package.
   *
   * @param  built Package that is asked to be built.
   * @author       calestialgem
   */
  record Build(PhysicalName built) implements Command {}

  /**
   * Running a package.
   *
   * @param  run    Package that is asked to be run.
   * @param  passed Arguments that will be passed to the run package on the
   *                  command line.
   * @author        calestialgem
   */
  record Run(PhysicalName run, List<String> passed) implements Command {}
}
