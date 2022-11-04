package rainfall.utility;

public sealed abstract class Map<Key, Value> {
  public abstract boolean isEmpty();
  public abstract boolean isFinite();
  public abstract int count();
  public abstract boolean contains(Key key);
  public abstract Value get(Key key);

  public static <Key, Value> Entry<Key, Value> entry(Key key, Value value) {
    return new Entry<>(key, value);
  }

  public static <Key, Value> Mutable<Key, Value> mutable() {
    return new Mutable<>(emptyBuckets(0), emptyBuckets(0), 0);
  }

  @SafeVarargs public static <Key, Value> Map<Key, Value>
    of(Entry<Key, Value>... entries) {
    var underlying = Map.<Key, Value>mutable();
    for (var entry : entries) { underlying.put(entry.key(), entry.value()); }
    return new Persistent<>(underlying);
  }

  public static final class Entry<Key, Value> {
    private final Key   key;
    private final Value value;
    private Entry(Key key, Value value) {
      this.key   = key;
      this.value = value;
    }

    public Key key() { return key; }
    public Value value() { return value; }
  }

  public static final class Mutable<Key, Value> extends Map<Key, Value> {
    private Box<Key>[]   keyBuckets;
    private Box<Value>[] valueBuckets;
    private int          count;
    private Mutable(Box<Key>[] keyBuckets, Box<Value>[] valueBuckets,
      int elements) {
      this.keyBuckets   = keyBuckets;
      this.valueBuckets = valueBuckets;
      this.count        = elements;
    }

    @Override public boolean isEmpty() { return count == 0; }
    @Override public boolean isFinite() { return count > 0; }
    @Override public int count() { return count; }
    @Override public boolean contains(Key key) {
      var start = key.hashCode();
      for (var index = 0; index < keyBuckets.length; index++) {
        var bucket = keyBuckets[(start + index) % keyBuckets.length];
        if (bucket.isEmpty()) { break; }
        if (bucket.value().equals(key)) { return true; }
      }
      return false;
    }
    @Override public Value get(Key key) {
      var start = key.hashCode();
      for (var index = 0; index < keyBuckets.length; index++) {
        int bucketIndex = (start + index) % keyBuckets.length;
        var bucket      = keyBuckets[bucketIndex];
        if (bucket.isEmpty()) { break; }
        if (bucket.value().equals(key)) {
          return valueBuckets[bucketIndex].value();
        }
      }
      throw new UnsupportedOperationException(
        "There is no entry for the given key in the map!");
    }

    public void put(Key insertedKey, Value insertedValue) {
      growIfNecessary();
      uncheckedPut(insertedKey, insertedValue);
    }

    private void growIfNecessary() {
      if (keyBuckets.length == 0 || (double) count / keyBuckets.length >= 0.5) {
        int newCapacity = keyBuckets.length == 0 ? 16 : keyBuckets.length * 16;
        var rehashed    = new Mutable<Key, Value>(emptyBuckets(newCapacity),
          emptyBuckets(newCapacity), 0);
        for (var bucketIndex = 0; bucketIndex < keyBuckets.length;
          bucketIndex++) {
          var keyBucket = keyBuckets[bucketIndex];
          if (keyBucket.isFull()) {
            rehashed.uncheckedPut(keyBucket.value(),
              valueBuckets[bucketIndex].value());
          }
        }
        keyBuckets   = rehashed.keyBuckets;
        valueBuckets = rehashed.valueBuckets;
      }
    }

    private void uncheckedPut(Key insertedKey, Value insertedValue) {
      var start = insertedKey.hashCode();
      for (var index = 0; index < keyBuckets.length; index++) {
        var bucketIndex = (start + index) % keyBuckets.length;
        var bucket      = keyBuckets[bucketIndex];
        if (bucket.isEmpty()) {
          keyBuckets[bucketIndex]   = Box.full(insertedKey);
          valueBuckets[bucketIndex] = Box.full(insertedValue);
          count++;
          return;
        }
        if (bucket.value().equals(insertedKey)) { return; }
      }
      throw new UnsupportedOperationException(
        "There is no place in map to put the entry!");
    }
  }

  private static final class Persistent<Key, Value> extends Map<Key, Value> {
    private final Mutable<Key, Value> underlying;
    private Persistent(Mutable<Key, Value> underlying) {
      this.underlying = underlying;
    }

    @Override public boolean isEmpty() { return underlying.isEmpty(); }
    @Override public boolean isFinite() { return underlying.isFinite(); }
    @Override public int count() { return underlying.count(); }
    @Override public boolean contains(Key key) {
      return underlying.contains(key);
    }
    @Override public Value get(Key key) { return underlying.get(key); }
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
