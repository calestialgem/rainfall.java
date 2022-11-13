package rainfall.utility;

import java.util.Iterator;

public interface Sequence<Value> extends Iterator<Value> {
  boolean continues();
  Value value();
  void advance();

  default Value take() {
    var value = value();
    advance();
    return value;
  }

  @Override public default boolean hasNext() { return continues(); }
  @Override public default Value next() { return take(); }
}
