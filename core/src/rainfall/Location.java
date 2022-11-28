package rainfall;

final class Location {
  private final String source;
  private final int    index;
  private final int    line;
  private final int    column;
  private Location(String source, int index, int line, int column) {
    this.source = source;
    this.index  = index;
    this.line   = line;
    this.column = column;
  }
}
