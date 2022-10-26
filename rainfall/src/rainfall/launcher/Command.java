package rainfall.launcher;

import java.util.List;

import rainfall.workspace.Designation;

public sealed interface Command {
  record New(Designation created) implements Command {}

  record Check(List<Designation> checked) implements Command {}

  record Test(List<Designation> tested) implements Command {}

  record Build(Designation built) implements Command {}

  record Run(Designation run, List<String> passed) implements Command {}
}
