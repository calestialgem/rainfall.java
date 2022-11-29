package rainfall;

record Portion(Location first, Location last) {
  static Portion at(String source, int first, int last) {
    return new Portion(Location.at(source, first), Location.at(source, last));
  }

  @Override public String toString() {
    return first.source().substring(first.index(), last.index() + 1);
  }
}
