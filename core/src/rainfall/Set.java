package rainfall;

public sealed abstract class Set<Element> {
  public static final class Mutable<Element> extends Set<Element> {
    private Box<Element>[] buckets;
    private int            count;
    private Mutable(Box<Element>[] buckets, int elements) {
      this.buckets = buckets;
      this.count   = elements;
    }

    @Override public boolean isEmpty() { return count == 0; }
    @Override public boolean isFinite() { return count > 0; }
    @Override public int count() { return count; }
    @Override public boolean contains(Element element) {
      var start = element.hashCode();
      for (var index = 0; index < buckets.length; index++) {
        var bucket = buckets[(start + index) % buckets.length];
        if (bucket.isEmpty()) { return false; }
        if (bucket.value().equals(element)) { return true; }
      }
      return false;
    }

    public void push(Element element) {
      if (buckets.length == 0 || (double) count / buckets.length >= 0.5) {
        var rehashed = new Mutable<Element>(
          emptyBuckets(buckets.length == 0 ? 16 : buckets.length * 16), 0);
        for (var bucket : buckets) {
          if (bucket.isFull()) { rehashed.uncheckedPush(bucket.value()); }
        }
        buckets = rehashed.buckets;
      }
      uncheckedPush(element);
    }

    private void uncheckedPush(Element element) {
      var start = element.hashCode();
      for (var index = 0; index < buckets.length; index++) {
        var bucketIndex = (start + index) % buckets.length;
        var bucket      = buckets[bucketIndex];
        if (bucket.isEmpty()) {
          buckets[bucketIndex] = Box.full(element);
          count++;
          return;
        }
        if (bucket.value().equals(element)) { return; }
      }
      throw new UnsupportedOperationException(
        "There is no place in set to insert the element!");
    }
  }

  public static <Element> Mutable<Element> mutable() {
    return new Mutable<>(emptyBuckets(0), 0);
  }

  @SafeVarargs public static <Element> Set<Element> of(Element... elements) {
    var underlying = Set.<Element>mutable();
    for (var element : elements) { underlying.push(element); }
    return new Persistent<>(underlying);
  }

  public abstract boolean isEmpty();
  public abstract boolean isFinite();
  public abstract int count();
  public abstract boolean contains(Element element);

  private static final class Persistent<Element> extends Set<Element> {
    private final Mutable<Element> underlying;
    private Persistent(Mutable<Element> underlying) {
      this.underlying = underlying;
    }

    @Override public boolean isEmpty() { return underlying.isEmpty(); }
    @Override public boolean isFinite() { return underlying.isFinite(); }
    @Override public int count() { return underlying.count(); }
    @Override public boolean contains(Element element) {
      return underlying.contains(element);
    }
  }

  @SuppressWarnings("unchecked") private static <Element> Box<Element>[]
    emptyBuckets(int capacity) {
    var array = new Box[capacity];
    for (var bucket = 0; bucket < capacity; bucket++) {
      array[bucket] = Box.empty();
    }
    return array;
  }
}
