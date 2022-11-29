package rainfall;

record Location(Source source, int index, int line, int column) {
  static Location at(Source source, int index) {
    int line   = 1;
    int column = 1;
    for (int i = 0; i < index; i++) {
      column++;
      if (source.contents().codepoint(i) == '\n') {
        line++;
        column = 1;
      }
    }
    return new Location(source, index, line, column);
  }
}
