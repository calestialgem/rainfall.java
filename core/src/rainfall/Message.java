package rainfall;

public sealed abstract class Message {
  public static <Value> Result<Value, Message> failure(String body,
    Message... causes) {
    return Result.failure(error(body, causes));
  }

  public static Message error(String body, Message... causes) {
    return new Single("error", body, List.of(causes));
  }
  public static Message warning(String body, Message... causes) {
    return new Single("warning", body, List.of(causes));
  }
  public static Message info(String body, Message... causes) {
    return new Single("info", body, List.of(causes));
  }

  public static Message group() {
    throw new IllegalArgumentException("There are no messages to group!");
  }
  public static Message group(Message member) { return member; }
  public static Message group(Message... members) {
    return new Multiple(List.of(members));
  }

  @Override public String toString() { return toString(0); }
  protected abstract String toString(int indentationLevel);

  private static final class Single extends Message {
    private final String        title;
    private final String        body;
    private final List<Message> causes;
    private Single(String title, String body, List<Message> causes) {
      this.title  = title;
      this.body   = body;
      this.causes = causes;
    }

    @Override protected String toString(int indentationLevel) {
      var builder = new StringBuilder();
      builder.append(title).append(": ").append(body);
      if (causes.count() > 0) {
        builder.append(" Due to:");
        for (var cause = 0; cause < causes.count(); cause++) {
          indent(indentationLevel + 1, builder);
          builder.append(causes.get(cause).toString(indentationLevel + 1));
        }
      }
      return builder.toString();
    }
  }

  private static final class Multiple extends Message {
    private final List<Message> members;
    private Multiple(List<Message> members) { this.members = members; }

    @Override protected String toString(int indentationLevel) {
      var builder = new StringBuilder();
      builder.append(members.get(0).toString(indentationLevel));
      for (var member = 1; member < members.count(); member++) {
        indent(indentationLevel, builder);
        builder.append(members.get(member).toString(indentationLevel));
      }
      return builder.toString();
    }
  }

  private static void indent(int indentationLevel, StringBuilder builder) {
    builder.append('\n');
    for (var level = 0; level < indentationLevel; level++) {
      builder.append(' ').append(' ');
    }
  }
}
