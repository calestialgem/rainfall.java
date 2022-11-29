package rainfall;

record Location(UTF8 source, int index, int line, int column) {
  static Location at(UTF8 source, int index) {
    int line   = 1;
    int column = 1;
    for (int i = 0; i < index; i++) {
      column++;
      if (source.codepoint(i) == '\n') {
        line++;
        column = 1;
      }
    }
    return new Location(source, index, line, column);
  }
}
