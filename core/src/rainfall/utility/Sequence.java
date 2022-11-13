package rainfall.utility;

import java.util.Iterator;

public interface Sequence<Value> extends Iterable<Value> {
  boolean continues();
  Value value();
  void advance();

  default Value take() {
    var value = value();
    advance();
    return value;
  }

  @Override default Iterator<Value> iterator() {
    return SequenceIterator.iterator(this);
  }
}
