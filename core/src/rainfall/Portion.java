package rainfall;

record Portion(Location first, Location last) {
  static Portion at(UTF8 source, int first, int last) {
    return new Portion(Location.at(source, first), Location.at(source, last));
  }

  UTF8 contents() { return first.source().sub(first.index(), last.index()); }
}
