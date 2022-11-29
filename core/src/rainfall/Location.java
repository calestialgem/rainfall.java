package rainfall;

record Location(String source, int index, int line, int column) {
  static Location at(String source, int index) {
    if (source.charAt(index) == '\n') throw new RuntimeException();
    int line   = 1;
    int column = 1;
    for (int i = 0; i < index; i++) {
      column++;
      if (source.charAt(i) == '\n') {
        line++;
        column = 1;
      }
    }
    return new Location(source, index, line, column);
  }
}
