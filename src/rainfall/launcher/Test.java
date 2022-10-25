package rainfall.launcher;

import java.util.List;

import rainfall.workspace.PhysicalName;

/**
 * Tests the given package or packages, or all the packages in the workspace
 * if none is given.
 */
public final class Test extends Command {
  /**
   * Names of the packages that will be tested. Will test all the packages in
   * the workspace when it is left empty.
   */
  public final List<PhysicalName> tested;

  /**
   * Constructs a test command.
   *
   * @param tested Names of the packages that will be tested by the
   *               constructed test command.
   */
  public Test(List<PhysicalName> tested) {
    this.tested = tested;
  }
}
