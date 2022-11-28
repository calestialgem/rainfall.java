package rainfall;

final class Portion {
  private final Location first;
  private final Location last;
  private Portion(Location first, Location last) {
    this.first = first;
    this.last  = last;
  }
}
