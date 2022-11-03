package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import rainfall.PhysicalName;
import rainfall.launcher.Command;

final class CommandTest {
  @Test void newHasName() {
    var created = PhysicalName.of("A").value();
    assertEquals(created, Command.new_(created).created());
  }

  @Test void checkHasEmptyChecked() {
    assertTrue(Command.check(List.of()).checked().isEmpty());
  }
  @Test void checkHasChecked() {
    var checked =
      List.of(PhysicalName.of("A").value(), PhysicalName.of("B").value());
    assertEquals(checked, Command.check(checked).checked());
  }

  @Test void testHasEmptyTested() {
    assertTrue(Command.test(List.of()).tested().isEmpty());
  }
  @Test void testHasTested() {
    var tested =
      List.of(PhysicalName.of("A").value(), PhysicalName.of("B").value());
    assertEquals(tested, Command.test(tested).tested());
  }

  @Test void buildHasName() {
    var built = PhysicalName.of("A").value();
    assertEquals(built, Command.build(built).built());
  }

  @Test void runHasName() {
    var run = PhysicalName.of("A").value();
    assertEquals(run, Command.run(run, List.of()).run());
  }
  @Test void runHasEmptyPassed() {
    assertTrue(
      Command.run(PhysicalName.of("A").value(), List.of()).passed().isEmpty());
  }
  @Test void runHasPassed() {
    var passed = List.of("a", "b");
    assertEquals(passed,
      Command.run(PhysicalName.of("A").value(), passed).passed());
  }
}
