package rainfall.utility;

import java.util.Arrays;

public sealed abstract class Set<Member> implements Iterable<Member> {
  @SafeVarargs public static <Member> Set<Member> of(Member... members) {
    var underlying = Set.<Member>mutable();
    for (var member : members) { underlying.put(member); }
    return underlying;
  }
  public static <Member> Mutable<Member> mutable() {
    return new Mutable<>(Mutable.createBuffer(0), 0);
  }

  public abstract boolean isEmpty();
  public abstract boolean isFinite();
  public abstract int count();
  public abstract Member member(Member equivalent);
  public abstract boolean contains(Member equivalent);

  @Override public abstract Sequence<Member> iterator();

  @Override public String toString() {
    var buffer = new StringBuilder();
    buffer.append('[');
    var sequence = iterator();
    if (sequence.continues()) {
      buffer.append(sequence.take());
      while (sequence.continues()) {
        buffer.append(',');
        buffer.append(' ');
        buffer.append(sequence.take());
      }
    }
    buffer.append(']');
    return buffer.toString();
  }
  @Override public int hashCode() {
    var hash = 1;
    for (var member : this) {
      hash *= 31;
      hash += member.hashCode();
    }
    return hash;
  }
  @SuppressWarnings("unchecked") @Override public boolean equals(Object other) {
    if (this == other) { return true; }
    if (!(other instanceof Set asSet)) { return false; }
    if (count() != asSet.count()) { return false; }
    for (var member : (Set<Member>) asSet) {
      if (!contains(member)) { return false; }
    }
    return true;
  }

  public static final class Mutable<Member> extends Set<Member> {
    private static final int GROWTH_FACTOR = 16;

    private Optional<Member>[] buffer;
    private int                used;
    private Mutable(Optional<Member>[] buffer, int used) {
      this.buffer = buffer;
      this.used   = used;
    }

    @SuppressWarnings("unchecked") private static <Member> Optional<Member>[]
      createBuffer(int capacity) {
      var buffer = new Optional[capacity];
      Arrays.fill(buffer, Optional.absent());
      return buffer;
    }

    @Override public boolean isEmpty() { return used == 0; }
    @Override public boolean isFinite() { return used > 0; }
    @Override public int count() { return used; }
    @Override public Member member(Member equivalent) {
      var offset = equivalent.hashCode();
      for (var index = 0; index < buffer.length; index++) {
        var bucket = buffer[(index + offset) % buffer.length];
        if (bucket.isAbsent()) { break; }
        if (bucket.value().equals(equivalent)) { return bucket.value(); }
      }
      return null;
    }
    @Override public boolean contains(Member equivalent) {
      var offset = equivalent.hashCode();
      for (var index = 0; index < buffer.length; index++) {
        var bucket = buffer[(index + offset) % buffer.length];
        if (bucket.isAbsent()) { break; }
        if (bucket.value().equals(equivalent)) { return true; }
      }
      return false;
    }

    @Override public Sequence<Member> iterator() {
      return ForwardSkippingSequence.start(buffer);
    }

    public void put(Member put) {
      if (used * GROWTH_FACTOR > buffer.length) {
        var grownCapacity = Math.max(1, buffer.length) * GROWTH_FACTOR;
        var grownSet      = new Mutable<Member>(createBuffer(grownCapacity), 0);
        for (var member : this) { grownSet.put(member); }
        buffer = grownSet.buffer;
      }
      var offset = put.hashCode();
      for (var index = 0; index < buffer.length; index++) {
        int bucket = (index + offset) % buffer.length;
        if (buffer[bucket].isAbsent()) {
          buffer[bucket] = Optional.present(put);
          return;
        }
      }
    }
  }
}
