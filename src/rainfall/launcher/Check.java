package rainfall.launcher;

import java.util.List;

import rainfall.workspace.PhysicalName;

/**
 * Checks the validity of the given package or packages, or all the packages
 * in the workspace if none is given.
 */
public final class Check extends Command {
  /**
   * Names of the packages that will be checked. Will check all the packages in
   * the workspace when it is left empty.
   */
  public final List<PhysicalName> checked;

  /**
   * Constructs a check command.
   *
   * @param checked Names of the packages that will be checked by the
   *                constructed check command.
   */
  public Check(List<PhysicalName> checked) {
    this.checked = checked;
  }
}
