package rainfall.launcher;

import java.util.List;

import rainfall.workspace.PhysicalName;

public sealed interface Command {
  record New(PhysicalName created) implements Command {}
  record Check(List<PhysicalName> checked) implements Command {}
  record Test(List<PhysicalName> tested) implements Command {}
  record Build(PhysicalName built) implements Command {}
  record Run(PhysicalName run, List<String> passed) implements Command {}
}
