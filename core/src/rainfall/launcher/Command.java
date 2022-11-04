package rainfall.launcher;

import rainfall.physical.Name;
import rainfall.utility.List;

public sealed abstract class Command {
  public static final class New extends Command {
    private final Name created;
    private New(Name created) { this.created = created; }

    public Name created() { return created; }
  }

  public static final class Check extends Command {
    private final List<Name> checked;
    private Check(List<Name> checked) { this.checked = checked; }

    public List<Name> checked() { return checked; }
  }

  public static final class Test extends Command {
    private final List<Name> tested;
    private Test(List<Name> tested) { this.tested = tested; }

    public List<Name> tested() { return tested; }
  }

  public static final class Build extends Command {
    private final Name built;
    private Build(Name built) { this.built = built; }

    public Name built() { return built; }
  }

  public static final class Run extends Command {
    private final Name         run;
    private final List<String> passed;
    private Run(Name run, List<String> passed) {
      this.run    = run;
      this.passed = passed;
    }

    public Name run() { return run; }
    public List<String> passed() { return passed; }
  }

  public static New new_(Name created) { return new New(created); }
  public static Check check(List<Name> checked) { return new Check(checked); }
  public static Test test(List<Name> tested) { return new Test(tested); }
  public static Build build(Name built) { return new Build(built); }
  public static Run run(Name run, List<String> passed) {
    return new Run(run, passed);
  }
}
