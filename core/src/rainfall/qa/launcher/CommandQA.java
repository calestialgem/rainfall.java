package rainfall.qa.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.launcher.Command;
import rainfall.physical.Name;
import rainfall.utility.List;

final class CommandQA {
  @Test void newHasName() {
    var created = Name.of("A").value();
    assertEquals(created, Command.new_(created).created());
  }

  @Test void checkHasEmptyChecked() {
    assertTrue(Command.check(List.of()).checked().isEmpty());
  }
  @Test void checkHasChecked() {
    var checked = List.of(Name.of("A").value(), Name.of("B").value());
    assertEquals(checked, Command.check(checked).checked());
  }

  @Test void testHasEmptyTested() {
    assertTrue(Command.test(List.of()).tested().isEmpty());
  }
  @Test void testHasTested() {
    var tested = List.of(Name.of("A").value(), Name.of("B").value());
    assertEquals(tested, Command.test(tested).tested());
  }

  @Test void buildHasName() {
    var built = Name.of("A").value();
    assertEquals(built, Command.build(built).built());
  }

  @Test void runHasName() {
    var run = Name.of("A").value();
    assertEquals(run, Command.run(run, List.of()).run());
  }
  @Test void runHasEmptyPassed() {
    assertTrue(Command.run(Name.of("A").value(), List.of()).passed().isEmpty());
  }
  @Test void runHasPassed() {
    var passed = List.of("a", "b");
    assertEquals(passed, Command.run(Name.of("A").value(), passed).passed());
  }
}
