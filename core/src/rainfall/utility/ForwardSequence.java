package rainfall.utility;

public final class ForwardSequence<Value> implements Sequence<Value> {
  private final Value[] values;
  private int           index;
  private ForwardSequence(Value[] values, int index) {
    this.values = values;
    this.index  = index;
  }

  public static <Value> Sequence<Value> start(Value[] values) {
    return new ForwardSequence<>(values, 0);
  }

  @Override public boolean continues() { return index < values.length; }
  @Override public Value value() { return values[index]; }
  @Override public void advance() { index++; }
}
