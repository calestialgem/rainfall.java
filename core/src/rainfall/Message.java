package rainfall;

public sealed abstract class Message {
  public static Message error(final String body, final Message... causes) {
    return new Single("error", body, causes);
  }
  public static Message warning(final String body, final Message... causes) {
    return new Single("warning", body, causes);
  }
  public static Message info(final String body, final Message... causes) {
    return new Single("info", body, causes);
  }

  public static Message group() {
    throw new IllegalArgumentException("There are no messages to group!");
  }
  public static Message group(Message member) { return member; }
  public static Message group(Message... members) {
    return new Multiple(members);
  }

  @Override public String toString() { return toString(0); }

  protected abstract String toString(int indentationLevel);

  private static final class Single extends Message {
    private final String    title;
    private final String    body;
    private final Message[] causes;
    private Single(final String title, final String body,
      final Message... causes) {
      this.title  = title;
      this.body   = body;
      this.causes = causes;
    }

    @Override protected String toString(final int indentationLevel) {
      final var builder = new StringBuilder();
      builder.append(title).append(": ").append(body);
      if (causes.length > 0) {
        builder.append(" Due to:");
        for (final var cause : causes) {
          indent(indentationLevel + 1, builder);
          builder.append(cause.toString(indentationLevel + 1));
        }
      }
      return builder.toString();
    }
  }

  private static final class Multiple extends Message {
    private final Message[] members;
    private Multiple(Message... members) { this.members = members; }

    @Override protected String toString(int indentationLevel) {
      final var builder = new StringBuilder();
      builder.append(members[0].toString(indentationLevel));
      for (var member = 1; member < members.length; member++) {
        indent(indentationLevel, builder);
        builder.append(members[member].toString(indentationLevel));
      }
      return builder.toString();
    }
  }

  private static void indent(final int indentationLevel,
    final StringBuilder builder) {
    builder.append(System.lineSeparator());
    for (var level = 0; level < indentationLevel; level++) {
      builder.append("  ");
    }
  }
}
