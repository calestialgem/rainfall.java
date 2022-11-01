package rainfall;

import java.util.List;

public sealed abstract class Command {
  public static final class New extends Command {
    private final PhysicalName created;
    private New(PhysicalName created) { this.created = created; }

    public PhysicalName created() { return created; }
  }

  public static final class Check extends Command {
    private final List<PhysicalName> checked;
    private Check(List<PhysicalName> checked) { this.checked = checked; }

    public List<PhysicalName> checked() { return checked; }
  }

  public static final class Test extends Command {
    private final List<PhysicalName> tested;
    private Test(List<PhysicalName> tested) { this.tested = tested; }

    public List<PhysicalName> tested() { return tested; }
  }

  public static final class Build extends Command {
    private final PhysicalName built;
    private Build(PhysicalName built) { this.built = built; }

    public PhysicalName built() { return built; }
  }

  public static final class Run extends Command {
    private final PhysicalName run;
    private final List<String> passed;
    private Run(PhysicalName run, List<String> passed) {
      this.run    = run;
      this.passed = passed;
    }

    public PhysicalName run() { return run; }
    public List<String> passed() { return passed; }
  }

  public static New new_(PhysicalName created) { return new New(created); }
  public static Check check(List<PhysicalName> checked) {
    return new Check(checked);
  }
  public static Test test(List<PhysicalName> tested) {
    return new Test(tested);
  }
  public static Build build(PhysicalName built) { return new Build(built); }
  public static Run run(PhysicalName run, List<String> passed) {
    return new Run(run, passed);
  }
}
