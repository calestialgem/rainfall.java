package rainfall.qa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.utility.List;

final class ListQA {
  @Test void aListThatWasCreatedWithoutElementsIsEmpty() {
    assertTrue(List.of().isEmpty());
  }
  @Test void aListThatWasCreatedWithoutElementsIsNotFinite() {
    assertFalse(List.of().isFinite());
  }
  @Test void countOfAListThatWasCreatedWithoutElementsIsZero() {
    assertEquals(0, List.of().count());
  }

  @Test void aListThatWasCreatedWithAnElementIsNotEmpty() {
    assertFalse(List.of(1).isEmpty());
  }
  @Test void aListThatWasCreatedWithAnElementIsFinite() {
    assertTrue(List.of(1).isFinite());
  }
  @Test void countOfAListThatWasCreatedWithAnElementIsOne() {
    assertEquals(1, List.of(1).count());
  }
  @Test void aListThatWasCreatedWithAnElementHasThatElement() {
    var element = 1;
    assertEquals(element, List.of(element).get(0));
  }

  @Test void aListThatWasCreatedWithElementsIsNotEmpty() {
    assertFalse(List.of(1, 2).isEmpty());
  }
  @Test void aListThatWasCreatedWithElementsIsFinite() {
    assertTrue(List.of(1, 2).isFinite());
  }
  @Test void
    countOfAListThatWasCreatedWithElementsIsTheNumberOfThoseElements() {
    assertEquals(2, List.of(1, 2).count());
  }
  @Test void aListThatWasCreatedWithElementsHasThoseElements() {
    var elements = new int[] { 1, 2 };
    var list     = List.of(elements[0], elements[1]);
    assertEquals(elements[0], list.get(0));
    assertEquals(elements[1], list.get(1));
  }

  @Test void aNewlyCreatedMutableListIsEmpty() {
    assertTrue(List.mutable().isEmpty());
  }
  @Test void aNewlyCreatedMutableListIsNotFinite() {
    assertFalse(List.mutable().isFinite());
  }
  @Test void countOfANewlyCreatedMutableListIsZero() {
    assertEquals(0, List.mutable().count());
  }

  @Test void pushingToANewlyCreatedMutableListMakesItNotEmpty() {
    var list = List.mutable();
    list.push(1);
    assertFalse(list.isEmpty());
  }
  @Test void pushingToANewlyCreatedMutableListMakesItFinite() {
    var list = List.mutable();
    list.push(1);
    assertTrue(list.isFinite());
  }
  @Test void pushingToANewlyCreatedMutableListIncrementsItsCount() {
    var list = List.mutable();
    list.push(1);
    assertEquals(1, list.count());
  }
  @Test void aNewlyCreatedAndPushedMutableListHasThePushedElementAtIndexZero() {
    var element = 1;
    var list    = List.mutable();
    list.push(element);
    assertEquals(element, list.get(0));
  }

  @Test void pushingToAListWithAnElementDoesNotMakeItEmpty() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    assertFalse(list.isEmpty());
  }
  @Test void pushingToAListWithAnElementKeepsItFinite() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    assertTrue(list.isFinite());
  }
  @Test void pushingToAListWithAnElementIncrementsItsCount() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    assertEquals(2, list.count());
  }
  @Test void pushingToAListWithAnElementKeepsThatElement() {
    var previousElement = 1;
    var list            = List.mutable();
    list.push(previousElement);
    list.push(2);
    assertEquals(previousElement, list.get(0));
  }
  @Test void pushingToAListWithElementsKeepsThoseElements() {
    var previousElements = new int[] { 1, 2 };
    var list             = List.mutable();
    list.push(previousElements[0]);
    list.push(previousElements[1]);
    list.push(3);
    assertEquals(previousElements[0], list.get(0));
    assertEquals(previousElements[1], list.get(1));
  }

  @Test void poppingFromAListWithAnElementMakesItEmpty() {
    var list = List.mutable();
    list.push(1);
    list.pop();
    assertTrue(list.isEmpty());
  }
  @Test void poppingFromAListWithAnElementMakesItNotFinite() {
    var list = List.mutable();
    list.push(1);
    list.pop();
    assertFalse(list.isFinite());
  }

  @Test void poppingFromAListDecrementsItsCount() {
    var list = List.mutable();
    list.push(1);
    list.pop();
    assertEquals(0, list.count());
  }
  @Test void poppingFromAListAsManyTimesAsItsCountMakesItEmpty() {
    var list = List.mutable();
    list.push(1);
    list.push(1);
    for (var i = list.count(); i > 0; i--) { list.pop(); }
    assertTrue(list.isEmpty());
  }

  @Test void listsWithAnEqualElementAreEqual() {
    assertEquals(List.of(1), List.of(1));
  }
  @Test void listsWithEqualElementsAreEqual() {
    assertEquals(List.of(1, 2), List.of(1, 2));
  }
  @Test void mutableAndImmutableListsWithEqualElementsAreEqual() {
    var elements = new int[] { 1, 2 };
    var mutable  = List.mutable();
    mutable.push(elements[0]);
    mutable.push(elements[1]);
    assertEquals(List.of(elements[0], elements[1]), mutable);
  }

  @Test void anEmptyListIsStringifiedCorrectly() {
    assertEquals("[]", List.of().toString());
  }
  @Test void aListWithASingleElementIsStringifiedCorrectly() {
    var element = 1;
    assertEquals("[%s]".formatted(element), List.of(element).toString());
  }
  @Test void aListWithElementsIsStringifiedCorrectly() {
    var elements = new int[] { 1, 2 };
    assertEquals("[%s,%s]".formatted(elements[0], elements[1]),
      List.of(elements[0], elements[1]).toString());
  }
}
