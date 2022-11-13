package rainfall.utility;

public final class ForwardSkippingSequence<Value> implements Sequence<Value> {
  private Optional<Value>[] buckets;
  private int               index;
  private ForwardSkippingSequence(Optional<Value>[] buckets, int index) {
    this.buckets = buckets;
    this.index   = index;
  }

  public static <Value> Sequence<Value> start(Optional<Value>[] buckets) {
    var sequence = new ForwardSkippingSequence<>(buckets, 0);
    sequence.skip();
    return sequence;
  }

  @Override public boolean continues() { return index < buckets.length; }
  @Override public Value value() { return buckets[index].value(); }
  @Override public void advance() {
    index++;
    skip();
  }

  private void skip() {
    while (index < buckets.length && buckets[index].isAbsent()) { index++; }
  }
}
