package rainfall;

record Portion(Location first, Location last) {
  static Portion at(Source source, int first, int last) {
    return new Portion(Location.at(source, first), Location.at(source, last));
  }

  UTF8 contents() {
    return first.source().contents().sub(first.index(), last.index());
  }
}
