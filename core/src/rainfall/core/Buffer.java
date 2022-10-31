package rainfall.core;

public final class Buffer {
  private static final String INDENTATION = "  ";
  private final StringBuilder builder;
  private int                 indentationLevel;

  public Buffer() {
    builder          = new StringBuilder();
    indentationLevel = 0;
  }

  public void format(final String specifier, final Object... values) {
    final var formatted = specifier.formatted(values);
    final var lines     = formatted.split(System.lineSeparator());
    builder.append(lines[0]);
    for (var line = 1; line < lines.length; line++) {
      builder.append(System.lineSeparator());
      for (var level = 0; level < indentationLevel; level++) {
        builder.append(INDENTATION);
      }
      builder.append(lines[line]);
    }
  }

  public void indent() { indentationLevel++; }
  public void outdent() { indentationLevel--; }

  @Override public String toString() { return builder.toString(); }

  public final class TestSuite {
    public static boolean defaultConstructedBufferIsClean() {
      final var clean = new Buffer();
      return clean.toString().equals("") && clean.indentationLevel == 0;
    }
    public static boolean formatAppendsSingleLine() {
      final var line   = "a";
      final var buffer = new Buffer();
      buffer.format(line);
      return buffer.toString().equals(line.formatted());
    }
    public static boolean formatAppendsDoubleLine() {
      final var line   = "a%nb";
      final var buffer = new Buffer();
      buffer.format(line);
      return buffer.toString().equals(line.formatted());
    }
    public static boolean formatAppendsMultipleLines() {
      final var line   = "a%nb%nc%nd%ne%nf%ng";
      final var buffer = new Buffer();
      buffer.format(line);
      return buffer.toString().equals(line.formatted());
    }
    public static boolean formatAppendsDoubleLineWithSingleIndentationLevel() {
      final var line   = "a%nb";
      final var buffer = new Buffer();
      buffer.indentationLevel = 1;
      buffer.format(line);
      return buffer.toString().equals("a%n  b".formatted());
    }
    public static boolean
      formatAppendsMultipleLinesWithSingleIndentationLevel() {
      final var line   = "a%nb%nc%nd%ne%nf%ng";
      final var buffer = new Buffer();
      buffer.indentationLevel = 1;
      buffer.format(line);
      return buffer.toString()
        .equals("a%n  b%n  c%n  d%n  e%n  f%n  g".formatted());
    }
    public static boolean
      formatAppendsDoubleLineWithMultipleIndentationLevels() {
      final var line   = "a%nb";
      final var buffer = new Buffer();
      buffer.indentationLevel = 10;
      buffer.format(line);
      return buffer.toString().equals("a%n                    b".formatted());
    }
    public static boolean
      formatAppendsMultipleLinesWithMultipleIndentationLevels() {
      final var line   = "a%nb%nc%nd%ne%nf%ng";
      final var buffer = new Buffer();
      buffer.indentationLevel = 10;
      buffer.format(line);
      return buffer.toString().equals(
        "a%n                    b%n                    c%n                    d%n                    e%n                    f%n                    g"
          .formatted());
    }
    public static boolean indentIncreasesIndentationLevel() {
      final var buffer  = new Buffer();
      final var initial = 1;
      buffer.indentationLevel = initial;
      buffer.indent();
      return buffer.indentationLevel == initial + 1;
    }
    public static boolean indentDecreasesIndentationLevel() {
      final var buffer  = new Buffer();
      final var initial = 1;
      buffer.indentationLevel = initial;
      buffer.outdent();
      return buffer.indentationLevel == initial - 1;
    }
    public static boolean stringifiedAsBuilderContents() {
      final var buffer = new Buffer();
      final var line   = "a";
      buffer.builder.append(line);
      return buffer.toString().equals(line);
    }
  }
}
