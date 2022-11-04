package rainfall.qa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import rainfall.utility.Set;

final class SetQA {
  @Test void aSetThatIsCreatedWithoutElementsIsEmpty() {
    assertTrue(Set.of().isEmpty());
  }
  @Test void aSetThatIsCreatedWithoutElementsIsNotFinite() {
    assertFalse(Set.of().isFinite());
  }
  @Test void countOfASetThatIsCreatedWithoutElementsIsZero() {
    assertEquals(0, Set.of().count());
  }
  @Test void aSetThatIsCreatedWithoutElementsDoesNotContainElements() {
    assertFalse(Set.of().contains(1));
    assertFalse(Set.of().contains(2));
  }
  @Test void aSetThatIsCreatedWithoutElementsWhenAskedWithAnElementThrows() {
    assertThrows(UnsupportedOperationException.class, () -> Set.of().get(1));
  }
  @Test void
    aSetThatIsCreatedWithoutElementsWhenAccessedWithAnElementReturnsEmptyBox() {
    assertTrue(Set.of().access(1).isEmpty());
  }

  @Test void aSetThatIsCreatedWithAnElementIsNotEmpty() {
    assertFalse(Set.of(1).isEmpty());
  }
  @Test void aSetThatIsCreatedWithAnElementIsFinite() {
    assertTrue(Set.of(1).isFinite());
  }
  @Test void countOfASetThatIsCreatedWithAnElementIsOne() {
    assertEquals(1, Set.of(1).count());
  }
  @Test void aSetThatIsCreatedWithAnElementContainsThatElement() {
    var element = 1;
    assertTrue(Set.of(element).contains(element));
  }
  @Test void
    aSetThatIsCreatedWithAnElementWhenAskedWithAnEqualElementReturnsThatElement() {
    final class Element {
      private final int a;
      private Element(int a) { this.a = a; }
      @Override public int hashCode() { return a; }
      @Override public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof Element element)) { return false; }
        return a == element.a;
      }
    }
    var element      = new Element(1);
    var equalElement = new Element(1);
    assertSame(element, Set.of(element).get(equalElement));
  }
  @Test void
    aSetThatIsCreatedWithAnElementWhenAccessedWithAnEqualElementReturnsFullBox() {
    final class Element {
      private final int a;
      private Element(int a) { this.a = a; }
      @Override public int hashCode() { return a; }
      @Override public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof Element element)) { return false; }
        return a == element.a;
      }
    }
    var element      = new Element(1);
    var equalElement = new Element(1);
    assertTrue(Set.of(element).access(equalElement).isFull());
  }
  @Test void
    aSetThatIsCreatedWithAnElementWhenAccessedWithAnEqualElementReturnsThatElement() {
      final class Element {
        private final int a;
        private Element(int a) { this.a = a; }
        @Override public int hashCode() { return a; }
        @Override public boolean equals(Object obj) {
          if (this == obj) { return true; }
          if (!(obj instanceof Element element)) { return false; }
          return a == element.a;
        }
      }
      var element      = new Element(1);
      var equalElement = new Element(1);
      assertSame(element, Set.of(element).access(equalElement).value());
  }
  @Test void aSetThatIsCreatedWithAnElementDoesNotContainUnspecifiedElements() {
    var set = Set.of(1);
    assertFalse(set.contains(2));
    assertFalse(set.contains(3));
  }

  @Test void aSetThatIsCreatedWithElementsIsNotEmpty() {
    assertFalse(Set.of(1, 2).isEmpty());
  }
  @Test void aSetThatIsCreatedWithElementsIsFinite() {
    assertTrue(Set.of(1, 2).isFinite());
  }
  @Test void countOfASetThatIsCreatedWithElementsIsTheNumberOfThoseElements() {
    assertEquals(2, Set.of(1, 2).count());
  }
  @Test void aSetThatIsCreatedWithElementsContainsThoseElements() {
    var elements = new int[] { 1, 2 };
    var set      = Set.of(elements[0], elements[1]);
    assertTrue(set.contains(elements[0]));
    assertTrue(set.contains(elements[1]));
  }
  @Test void aSetThatIsCreatedWithElementsDoesNotContainUnspecifiedElements() {
    var set = Set.of(1, 2);
    assertFalse(set.contains(3));
    assertFalse(set.contains(4));
  }

  @Test void aSetThatIsCreatedWithDuplicateElementsIsNotEmpty() {
    assertFalse(Set.of(1, 1).isEmpty());
  }
  @Test void aSetThatIsCreatedWithDuplicateElementsIsNotFinite() {
    assertTrue(Set.of(1, 1).isFinite());
  }
  @Test void countOfASetThatIsCreatedWithDuplicateElementsIsOne() {
    assertEquals(1, Set.of(1, 1).count());
  }
  @Test void aSetThatIsCreatedWithDuplicateElementsContainsThoseElements() {
    var element = 1;
    assertTrue(Set.of(element, element).contains(element));
  }

  @Test void aNewlyCreatedMutableSetIsEmpty() {
    assertTrue(Set.mutable().isEmpty());
  }
  @Test void aNewlyCreatedMutableSetIsNotFinite() {
    assertFalse(Set.mutable().isFinite());
  }
  @Test void countOfANewlyCreatedMutableSetIsZero() {
    assertEquals(0, Set.mutable().count());
  }
  @Test void aNewlyCreatedMutableSetDoesNotContainElements() {
    var set = Set.mutable();
    assertFalse(set.contains(1));
    assertFalse(set.contains(2));
  }

  @Test void pushingToANewlyCreatedMutableSetMakesItNotEmpty() {
    var set = Set.mutable();
    set.push(1);
    assertFalse(set.isEmpty());
  }
  @Test void pushingToANewlyCreatedMutableSetMakesItFinite() {
    var set = Set.mutable();
    set.push(1);
    assertTrue(set.isFinite());
  }
  @Test void pushingToANewlyCreatedMutableSetIncrementsItsCount() {
    var set = Set.mutable();
    set.push(1);
    assertEquals(1, set.count());
  }
  @Test void pushingToANewlyCreatedMutableSetInsertsThePushedElement() {
    var element = 1;
    var set     = Set.mutable();
    set.push(element);
    assertTrue(set.contains(element));
  }
  @Test void
    pushingToANewlyCreatedMutableSetDoesNotInsertUnspecifiedElements() {
    var set = Set.mutable();
    set.push(1);
    assertFalse(set.contains(2));
    assertFalse(set.contains(3));
  }

  @Test void pushingToAFiniteMutableSetDoesNotMakeItEmpty() {
    var set = Set.mutable();
    set.push(1);
    set.push(2);
    assertFalse(set.isEmpty());
  }
  @Test void pushingToAFiniteMutableSetKeepsItFinite() {
    var set = Set.mutable();
    set.push(1);
    set.push(2);
    assertTrue(set.isFinite());
  }
  @Test void pushingToAFiniteMutableSetIncrementsItsCount() {
    var set = Set.mutable();
    set.push(1);
    set.push(2);
    assertEquals(2, set.count());
  }
  @Test void pushingToAFiniteMutableSetInsertsPushedElement() {
    var element = 2;
    var set     = Set.mutable();
    set.push(1);
    set.push(element);
    assertTrue(set.contains(element));
  }
  @Test void pushingToAFiniteMutableSetKeepsPreviousElement() {
    var previousElement = 1;
    var set             = Set.mutable();
    set.push(previousElement);
    set.push(2);
    assertTrue(set.contains(previousElement));
  }
  @Test void pushingToAFiniteMutableSetKeepsPreviousElements() {
    var set = Set.mutable();
    set.push(1);
    set.push(2);
    assertFalse(set.contains(3));
    assertFalse(set.contains(4));
  }
}
