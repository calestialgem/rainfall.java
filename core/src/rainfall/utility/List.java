package rainfall.utility;

public sealed abstract class List<Element> {
  @SafeVarargs public static <Element> List<Element> of(Element... elements) {
    return new Array<>(elements);
  }

  public static <Element> Mutable<Element> mutable() { return new Mutable<>(); }

  public abstract boolean isEmpty();
  public abstract boolean isFinite();
  public abstract int count();
  public abstract Element get(int index);

  private static final class Array<Element> extends List<Element> {
    private final Element[] elements;
    private Array(Element[] elements) { this.elements = elements; }

    @Override public boolean isEmpty() { return elements.length == 0; }
    @Override public boolean isFinite() { return elements.length > 0; }
    @Override public int count() { return elements.length; }
    @Override public Element get(int accessedIndex) {
      return elements[accessedIndex];
    }
  }

  public static final class Mutable<Element> extends List<Element> {
    private Element[] elements;
    private int       count;
    @SuppressWarnings("unchecked") private Mutable() {
      elements = (Element[]) new Object[0];
      count    = 0;
    }

    @Override public boolean isEmpty() { return count == 0; }
    @Override public boolean isFinite() { return count > 0; }
    @Override public int count() { return count; }
    @Override public Element get(int accessedIndex) {
      return elements[accessedIndex];
    }

    @SuppressWarnings("unchecked") public void push(Element pushedElement) {
      if (count == elements.length) {
        var previousElements = elements;
        elements = (Element[]) new Object[previousElements.length == 0 ? 1
          : previousElements.length * 2];
        System.arraycopy(previousElements, 0, elements, 0,
          previousElements.length);
      }
      elements[count++] = pushedElement;
    }
    public void pop() { count--; }
  }
}
