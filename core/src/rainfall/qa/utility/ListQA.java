package rainfall.qa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.utility.List;

final class ListQA {
  @Test void emptyIsEmpty() { assertTrue(List.of().isEmpty()); }
  @Test void emptyIsNotFinite() { assertFalse(List.of().isFinite()); }
  @Test void emptyHasZeroCount() { assertEquals(0, List.of().count()); }

  @Test void singleElementIsNotEmpty() { assertFalse(List.of(1).isEmpty()); }
  @Test void singleElementIsFinite() { assertTrue(List.of(1).isFinite()); }
  @Test void singleElementHasOneCount() { assertEquals(1, List.of(1).count()); }
  @Test void singleElementHasElement() {
    var element = 1;
    assertEquals(element, List.of(element).get(0));
  }

  @Test void multiElementIsNotEmpty() { assertFalse(List.of(1, 2).isEmpty()); }
  @Test void multiElementIsFinite() { assertTrue(List.of(1, 2).isFinite()); }
  @Test void multiElementHasCorrectCount() {
    assertEquals(2, List.of(1, 2).count());
  }
  @Test void multiElementHasElements() {
    var elements = new int[] { 1, 2 };
    var list     = List.of(elements[0], elements[1]);
    assertEquals(elements[0], list.get(0));
    assertEquals(elements[1], list.get(1));
  }

  @Test void newMutableIsEmpty() { assertTrue(List.mutable().isEmpty()); }
  @Test void newMutableIsNotFinite() { assertFalse(List.mutable().isFinite()); }
  @Test void newMutableHasZeroCount() {
    assertEquals(0, List.mutable().count());
  }

  @Test void pushingToNewMutableMakesNotEmpty() {
    var list = List.mutable();
    list.push(1);
    assertFalse(list.isEmpty());
  }
  @Test void pushingToNewMutableMakesFinite() {
    var list = List.mutable();
    list.push(1);
    assertTrue(list.isFinite());
  }
  @Test void pushingToNewMutableIncrementsCount() {
    var list = List.mutable();
    list.push(1);
    assertEquals(1, list.count());
  }
  @Test void pushingToNewMutableInsertsElement() {
    var element = 1;
    var list    = List.mutable();
    list.push(element);
    assertEquals(element, list.get(0));
  }

  @Test void pushingToFiniteDoesNotMakeEmpty() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    assertFalse(list.isEmpty());
  }
  @Test void pushingToFiniteKeepsFinite() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    assertTrue(list.isFinite());
  }
  @Test void pushingToFiniteIncrementsCount() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    assertEquals(2, list.count());
  }
  @Test void pushingToFiniteKeepsPreviousElement() {
    var previousElement = 1;
    var list            = List.mutable();
    list.push(previousElement);
    list.push(2);
    assertEquals(previousElement, list.get(0));
  }
  @Test void pushingToFiniteKeepsPreviousElements() {
    var previousElements = new int[] { 1, 2 };
    var list             = List.mutable();
    list.push(previousElements[0]);
    list.push(previousElements[1]);
    list.push(3);
    assertEquals(previousElements[0], list.get(0));
    assertEquals(previousElements[1], list.get(1));
  }

  @Test void poppingFromSingleElementMakesEmpty() {
    var list = List.mutable();
    list.push(1);
    list.pop();
    assertTrue(list.isEmpty());
  }
  @Test void poppingFromSingleElementMakesNotFinite() {
    var list = List.mutable();
    list.push(1);
    list.pop();
    assertFalse(list.isFinite());
  }

  @Test void poppingFromFiniteDecrementsCount() {
    var list = List.mutable();
    list.push(1);
    list.pop();
    assertEquals(0, list.count());
  }
  @Test void poppingCountManyFromFiniteMakesEmpty() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    for (var i = list.count(); i > 0; i--) { list.pop(); }
    assertTrue(list.isEmpty());
  }
}
