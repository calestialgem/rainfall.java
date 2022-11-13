package rainfall.utility;

import java.util.Iterator;

public final class SequenceIterator<Value> implements Iterator<Value> {
  private final Sequence<Value> sequence;
  private SequenceIterator(Sequence<Value> sequence) {
    this.sequence = sequence;
  }

  public static <Value> SequenceIterator<Value>
    iterator(Sequence<Value> sequence) {
    return new SequenceIterator<>(sequence);
  }

  @Override public boolean hasNext() { return sequence.continues(); }
  @Override public Value next() { return sequence.take(); }
}
