package rainfall.utility;

public sealed abstract class List<Element> implements Iterable<Element> {
  @SafeVarargs public static <Element> List<Element> of(Element... elements) {
    var underlying = List.<Element>mutable();
    for (var element : elements) { underlying.push(element); }
    return underlying;
  }
  @SuppressWarnings("unchecked") public static <Element> Mutable<Element>
    mutable() {
    return new Mutable<>((Element[]) new Object[0], 0);
  }

  public abstract boolean isEmpty();
  public abstract boolean isFinite();
  public abstract int count();
  public abstract Element element(int index);

  @Override public Sequence<Element> iterator() {
    return ForwardSequence.start(this);
  }

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
    for (var element : this) {
      hash *= 31;
      hash += element.hashCode();
    }
    return hash;
  }
  @Override public boolean equals(Object other) {
    if (this == other) { return true; }
    if (!(other instanceof List asList)) { return false; }
    if (count() != asList.count()) { return false; }
    for (var index = 0; index < count(); index++) {
      if (!element(index).equals(asList.element(index))) { return false; }
    }
    return true;
  }

  public static final class Mutable<Element> extends List<Element> {
    private Element[] buffer;
    private int       used;
    private Mutable(Element[] buffer, int used) {
      this.buffer = buffer;
      this.used   = used;
    }

    @Override public boolean isEmpty() { return used == 0; }
    @Override public boolean isFinite() { return used > 0; }
    @Override public int count() { return used; }
    @Override public Element element(int index) { return buffer[index]; }

    @SuppressWarnings("unchecked") public void push(Element pushed) {
      if (used == buffer.length) {
        var grownCapacity = Math.max(1, buffer.length * 2);
        var grownBuffer   = (Element[]) new Object[grownCapacity];
        System.arraycopy(buffer, 0, grownBuffer, 0, buffer.length);
        buffer = grownBuffer;
      }
      buffer[used++] = pushed;
    }
    public Element pop() { return buffer[--used]; }
  }

  private static final class ForwardSequence<Element>
    implements Sequence<Element> {
    private final List<Element> list;
    private int                 index;
    private ForwardSequence(List<Element> list, int index) {
      this.list  = list;
      this.index = index;
    }

    private static <Element> ForwardSequence<Element>
      start(List<Element> list) {
      return new ForwardSequence<>(list, 0);
    }

    @Override public boolean continues() { return index < list.count(); }
    @Override public Element value() { return list.element(index); }
    @Override public void advance() { index++; }
  }
}
