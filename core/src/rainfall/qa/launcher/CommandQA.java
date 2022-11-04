package rainfall.qa.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.launcher.Command;
import rainfall.physical.Name;
import rainfall.utility.List;

final class CommandQA {
  @Test void aNewCommandHasTheGivenName() {
    var created = Name.of("A").value();
    assertEquals(created, Command.new_(created).created());
  }

  @Test void aCheckCommandWithoutNamesHasEmptyNames() {
    assertTrue(Command.check(List.of()).checked().isEmpty());
  }
  @Test void aCheckCommandWithNamesHasGivenNames() {
    var checked = List.of(Name.of("A").value(), Name.of("B").value());
    assertEquals(checked, Command.check(checked).checked());
  }

  @Test void aTestCommandWithoutNamesHasEmptyNames() {
    assertTrue(Command.test(List.of()).tested().isEmpty());
  }
  @Test void aTestCommandWithNamesHasGivenNames() {
    var tested = List.of(Name.of("A").value(), Name.of("B").value());
    assertEquals(tested, Command.test(tested).tested());
  }

  @Test void aBuildCommandHasTheGivenName() {
    var built = Name.of("A").value();
    assertEquals(built, Command.build(built).built());
  }

  @Test void aRunCommandHasTheGivenName() {
    var run = Name.of("A").value();
    assertEquals(run, Command.run(run, List.of()).run());
  }
  @Test void aRunCommandWithoutArgumentsHasEmptyArguments() {
    assertTrue(Command.run(Name.of("A").value(), List.of()).passed().isEmpty());
  }
  @Test void aRunCommandWithArgumentsHasGivenArguments() {
    var passed = List.of("a", "b");
    assertEquals(passed, Command.run(Name.of("A").value(), passed).passed());
  }
}
