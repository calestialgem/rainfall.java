package rainfall;

public sealed abstract class Command {
  public static final class New extends Command {
    private final PhysicalName created;
    private New(PhysicalName created) { this.created = created; }

    public PhysicalName created() { return created; }
  }

  public static New new_(PhysicalName created) { return new New(created); }
}
