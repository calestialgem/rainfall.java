package rainfall;

record Portion(Location first, Location last) {
  static Portion at(Unicode source, int first, int last) {
    return new Portion(Location.at(source, first), Location.at(source, last));
  }

  Unicode contents() { return first.source().sub(first.index(), last.index()); }
}
