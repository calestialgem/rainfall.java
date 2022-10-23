package rainfall.launcher;

import java.util.*;

/**
 * Checks the validity of the given package or packages, or all the packages
 * in the workspace if none is given.
 */
public final class Check extends Command {
  /**
   * Names of the packages that will be checked. Will check all the packages in
   * the workspace when it is left empty.
   */
  public final List<String> checked;

  /**
   * Constructs a check command.
   *
   * @param checked Names of the packages that will be checked by the
   *                constructed check command.
   */
  public Check(List<String> checked) {
    this.checked = checked;
  }
}
