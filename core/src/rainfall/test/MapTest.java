package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import rainfall.Map;

final class MapTest {
  @Test void anEntryThatIsCreatedHasTheGivenKey() {
    var key = 1;
    assertEquals(key, Map.entry(key, 1).key());
  }
  @Test void anEntryThatIsCreatedHasTheGivenValue() {
    var value = 1;
    assertEquals(value, Map.entry(1, value).value());
  }

  @Test void aMapThatIsCreatedWithoutEntriesIsEmpty() {
    assertTrue(Map.of().isEmpty());
  }
  @Test void aMapThatIsCreatedWithoutEntriesIsNotFinite() {
    assertFalse(Map.of().isFinite());
  }
  @Test void countOfAMapThatIsCreatedWithoutEntriesIsZero() {
    assertEquals(0, Map.of().count());
  }
  @Test void aMapThatIsCreatedWithoutEntriesDoesNotContainKeys() {
    var map = Map.of();
    assertFalse(map.contains(1));
    assertFalse(map.contains(2));
  }
  @Test void aMapThatIsCreatedWithoutEntriesThrowsWhenAccessedWithAKey() {
    assertThrows(UnsupportedOperationException.class, () -> Map.of().get(1));
  }

  @Test void aMapThatIsCreatedWithAnEntryIsNotEmpty() {
    assertFalse(Map.of(Map.entry(1, 1)).isEmpty());
  }
  @Test void aMapThatIsCreatedWithAnEntryIsFinite() {
    assertTrue(Map.of(Map.entry(1, 1)).isFinite());
  }
  @Test void countOfAMapThatIsCreatedWithAnEntryIsOne() {
    assertEquals(1, Map.of(Map.entry(1, 1)).count());
  }
  @Test void aMapThatIsCreatedWithAnEntryContainsKeyOfThatEntry() {
    var key = 1;
    assertTrue(Map.of(Map.entry(key, 1)).contains(key));
  }
  @Test void aMapThatIsCreatedWithAnEntryContainsValueOfThatEntry() {
    var key   = 1;
    var value = 1;
    assertEquals(value, Map.of(Map.entry(key, value)).get(key));
  }
  @Test void aMapThatIsCreatedWithAnEntryDoesNotContainUnspecifiedKeys() {
    var map = Map.of(Map.entry(1, 1));
    assertFalse(map.contains(2));
    assertFalse(map.contains(3));
  }
  @Test void
    aMapThatIsCreatedWithAnEntryThrowsWhenAccessedWithAnUnspecifiedKey() {
    assertThrows(UnsupportedOperationException.class,
      () -> Map.of(Map.entry(1, 1)).get(2));
  }

  @Test void aMapThatIsCreatedWithEntriesIsNotEmpty() {
    assertFalse(Map.of(Map.entry(1, 1), Map.entry(2, 2)).isEmpty());
  }
  @Test void aMapThatIsCreatedWithEntriesIsFinite() {
    assertTrue(Map.of(Map.entry(1, 1), Map.entry(2, 2)).isFinite());
  }
  @Test void countOfAMapThatIsCreatedWithEntriesIsTheNumberOfThoseEntries() {
    assertEquals(2, Map.of(Map.entry(1, 1), Map.entry(2, 2)).count());
  }
  @Test void aMapThatIsCreatedWithEntriesContainsKeysOfThoseEntries() {
    var keys = new int[] { 1, 2 };
    var map  = Map.of(Map.entry(keys[0], 1), Map.entry(keys[1], 2));
    assertTrue(map.contains(keys[0]));
    assertTrue(map.contains(keys[1]));
  }
  @Test void aMapThatIsCreatedWithEntriesContainsValuesOfThoseEntries() {
    var keys   = new int[] { 1, 2 };
    var values = new int[] { 1, 2 };
    var map    =
      Map.of(Map.entry(keys[0], values[0]), Map.entry(keys[1], values[1]));
    assertEquals(values[0], map.get(keys[0]));
    assertEquals(values[1], map.get(keys[1]));
  }
  @Test void aMapThatIsCreatedWithEntriesDoesNotContainUnspecifiedKeys() {
    var map = Map.of(Map.entry(1, 1), Map.entry(2, 2));
    assertFalse(map.contains(3));
    assertFalse(map.contains(4));
  }
  @Test void
    aMapThatIsCreatedWithEntriesThrowsWhenAccessedWithAnUnspecifiedKey() {
    assertThrows(UnsupportedOperationException.class,
      () -> Map.of(Map.entry(1, 1), Map.entry(2, 2)).get(3));
  }

  @Test void aMapThatIsCreatedWithDuplicateKeysIsNotEmpty() {
    assertFalse(Map.of(Map.entry(1, 1), Map.entry(1, 1)).isEmpty());
  }
  @Test void aMapThatIsCreatedWithDuplicateKeysIsFinite() {
    assertTrue(Map.of(Map.entry(1, 1), Map.entry(1, 1)).isFinite());
  }
  @Test void countOfAMapThatIsCreatedWithDuplicateKeysIsOne() {
    assertEquals(1, Map.of(Map.entry(1, 1), Map.entry(1, 1)).count());
  }
  @Test void aMapThatIsCreatedWithDuplicateKeysContainsThoseKeys() {
    var key = 1;
    assertTrue(Map.of(Map.entry(key, 1), Map.entry(key, 1)).contains(key));
  }
  @Test void
    aMapThatIsCreatedWithDuplicateKeysContainsTheFirstValueOfThoseKeys() {
    var key    = 1;
    var values = new int[] { 1, 2 };
    assertEquals(values[0],
      Map.of(Map.entry(key, values[0]), Map.entry(key, values[1])).get(key));
  }
  @Test void aMapThatIsCreatedWithDuplicateKeysDoesNotContainUnspecifiedKeys() {
    var map = Map.of(Map.entry(1, 1), Map.entry(1, 1));
    assertFalse(map.contains(2));
    assertFalse(map.contains(3));
  }
  @Test void
    aMapThatIsCreatedWithDuplicateKeysThrowsWhenAccessedWithAnUnspecifiedKey() {
    assertThrows(UnsupportedOperationException.class,
      () -> Map.of(Map.entry(1, 1), Map.entry(1, 1)).get(2));
  }

  @Test void aNewlyCreatedMutableMapIsEmpty() {
    assertTrue(Map.mutable().isEmpty());
  }
  @Test void aNewlyCreatedMutableMapIsNotFinite() {
    assertFalse(Map.mutable().isFinite());
  }
  @Test void countOfANewlyCreatedMutableMapIsZero() {
    assertEquals(0, Map.mutable().count());
  }
  @Test void aNewlyCreatedMutableMapDoesNotContainKeys() {
    var map = Map.mutable();
    assertFalse(map.contains(1));
    assertFalse(map.contains(2));
  }
  @Test void aNewlyCreatedMutableMapThrowsWhenAccessedWithAKey() {
    assertThrows(UnsupportedOperationException.class,
      () -> Map.mutable().get(1));
  }

  @Test void puttingToANewlyCreatedMutableMapMakesItNotEmpty() {
    var map = Map.mutable();
    map.put(1, 1);
    assertFalse(map.isEmpty());
  }
  @Test void puttingToANewlyCreatedMutableMapMakesItFinite() {
    var map = Map.mutable();
    map.put(1, 1);
    assertTrue(map.isFinite());
  }
  @Test void puttingToANewlyCreatedMutableMapIncrementsItsCount() {
    var map = Map.mutable();
    map.put(1, 1);
    assertEquals(1, map.count());
  }
  @Test void aNewlyCreatedMutableMapThatIsPutContainsThePutKey() {
    var key = 1;
    var map = Map.mutable();
    map.put(key, 1);
    assertTrue(map.contains(key));
  }
  @Test void aNewlyCreatedMutableMapThatIsPutContainsThePutValue() {
    var key   = 1;
    var value = 1;
    var map   = Map.mutable();
    map.put(key, value);
    assertEquals(value, map.get(key));
  }
  @Test void
    aNewlyCreatedAndInsertedMapDoesNotContainKeysThatWereNotInserted() {
    var map = Map.mutable();
    map.put(1, 1);
    assertFalse(map.contains(2));
    assertFalse(map.contains(3));
  }
  @Test void
    aNewlyCreatedAndInsertedMapThrowsWhenAccessedWithAKeyThatWasNotInserted() {
    var map = Map.mutable();
    map.put(1, 1);
    assertThrows(UnsupportedOperationException.class, () -> map.get(2));
  }

  @Test void insertingToAFiniteMutableMapDoesNotMakeItEmpty() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(2, 2);
    assertFalse(map.isEmpty());
  }
  @Test void pushingToAFiniteMutableMapKeepsItFinite() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(2, 2);
    assertTrue(map.isFinite());
  }
  @Test void pushingToAFiniteMutableMapIncrementsItsCount() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(2, 2);
    assertEquals(2, map.count());
  }
  @Test void pushingToAFiniteMutableMapInsertsPushedKey() {
    var key = 2;
    var map = Map.mutable();
    map.put(1, 1);
    map.put(key, 2);
    assertTrue(map.contains(key));
  }
  @Test void pushingToAFiniteMutableMapKeepsPreviousKey() {
    var previousKey = 1;
    var map         = Map.mutable();
    map.put(previousKey, 1);
    map.put(2, 2);
    assertTrue(map.contains(previousKey));
  }
  @Test void pushingToAFiniteMutableMapKeepsPreviousKeys() {
    var previousKeys = new int[] { 1, 2 };
    var map          = Map.mutable();
    map.put(previousKeys[0], 1);
    map.put(previousKeys[1], 2);
    map.put(3, 3);
    assertTrue(map.contains(previousKeys[0]));
    assertTrue(map.contains(previousKeys[1]));
  }
  @Test void pushingToAFiniteMutableMapDoesNotInsertUnspecifiedKeys() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(2, 2);
    assertFalse(map.contains(3));
    assertFalse(map.contains(4));
  }

  @Test void
    pushingAnEntryWithAnExistingKeyToAFiniteMutableMapDoesNotMakeItEmpty() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(1, 1);
    assertFalse(map.isEmpty());
  }
  @Test void pushingAnEntryWithAnExistingKeyToAFiniteMutableMapKeepsItFinite() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(1, 1);
    assertTrue(map.isFinite());
  }
  @Test void
    pushingAnEntryWithAnExistingKeyToAFiniteMutableMapKeepsItsCountSame() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(1, 1);
    assertEquals(1, map.count());
  }
  @Test void
    pushingAnEntryWithAnExistingKeyToAFiniteMutableMapKeepsPreviousKey() {
    var previousKey = 1;
    var map         = Map.mutable();
    map.put(previousKey, 1);
    map.put(previousKey, 1);
    assertTrue(map.contains(previousKey));
  }
  @Test void
    pushingAnEntryWithAnExistingKeyToAFiniteMutableMapKeepsPreviousKeys() {
    var previousKeys = new int[] { 1, 2 };
    var map          = Map.mutable();
    map.put(previousKeys[0], 1);
    map.put(previousKeys[1], 2);
    map.put(previousKeys[1], 2);
    assertTrue(map.contains(previousKeys[0]));
    assertTrue(map.contains(previousKeys[1]));
  }
  @Test void
    pushingAnEntryWithAnExistingKeyToAFiniteMutableMapDoesNotInsertUnspecifiedKeys() {
    var map = Map.mutable();
    map.put(1, 1);
    map.put(1, 1);
    assertFalse(map.contains(2));
    assertFalse(map.contains(3));
  }
}
