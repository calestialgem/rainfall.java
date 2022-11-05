package rainfall.qa.cli;

import org.junit.jupiter.api.Test;

import rainfall.cli.CommandLineInterface;
import rainfall.launcher.Command;
import rainfall.utility.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class CommandLineInterfaceQA {
  @Test void parsingEmptyArgumentsFails() {
    assertTrue(CommandLineInterface.parse().isFailure());
  }
  @Test void parsingEmptyArgumentsInformsUsage() {
    assertEquals(
      """
        info: Rainfall Thrice Compiler v0.0.1
        usage: rainfall [options] <command> [arguments]
        options:
         * --directory (-d) <path>: set the workspace directory to the given path, defaults to working directory
        commands:
         * new   (n) <name>:             creates a new package
         * check (c) [names]:            checks the validity of the given or all packages
         * test  (t) [names]:            runs the tests for the given or all packages
         * build (b) <name>:             generates the C file of the given package
         * run   (r) <name> [arguments]: runs the given executable package""",
      CommandLineInterface.parse().error().toString());
  }

  @Test void parsingANewCommandWithoutAPackageNameFails() {
    assertTrue(CommandLineInterface.parse("new").isFailure());
  }
  @Test void parsingANewCommandWithoutAPackageNameReports() {
    assertEquals("error: Provide a name for the created package!",
      CommandLineInterface.parse("new").error().toString());
  }
  @Test void parsingANewCommandWithAnInvalidPackageNameFails() {
    assertTrue(CommandLineInterface.parse("new", "package").isFailure());
  }
  @Test void parsingANewCommandWithAnInvalidPackageNameReports() {
    assertEquals("""
      error: Package name for the created package was invalid! Due to:
        error: Name must start with an uppercase English letter!""",
      CommandLineInterface.parse("new", "package").error().toString());
  }
  @Test void parsingANewCommandWithExtraArgumentsFails() {
    assertTrue(CommandLineInterface
      .parse("new", "Package", "extra", "arguments").isFailure());
  }
  @Test void parsingANewCommandWithExtraArgumentsReports() {
    assertEquals("""
      error: There were extra arguments:
       - extra
       - arguments""", CommandLineInterface
      .parse("new", "Package", "extra", "arguments").error().toString());
  }
  @Test void
    parsingANewCommandWithAnInvalidPackageNameAndExtraArgumentsFails() {
    assertTrue(CommandLineInterface
      .parse("new", "package", "extra", "arguments").isFailure());
  }
  @Test void
    parsingANewCommandWithAnInvalidPackageNameAndExtraArgumentsReports() {
    assertEquals("""
      error: Package name for the created package was invalid! Due to:
        error: Name must start with an uppercase English letter!
      error: There were extra arguments:
       - extra
       - arguments""", CommandLineInterface
      .parse("new", "package", "extra", "arguments").error().toString());
  }
  @Test void parsingANewCommandWithAValidPackageNameSucceeds() {
    assertTrue(CommandLineInterface.parse("new", "Package").isSuccess());
  }
  @Test void
    parsingANewCommandWithAValidPackageNameResultsInANewCommandWithTheGivenPackageName() {
    var name = "Package";
    assertEquals(name,
      ((Command.New) CommandLineInterface.parse("new", name).value()).created()
        .value());
  }

  @Test void parsingANewCommandShortcutWithoutAPackageNameFails() {
    assertTrue(CommandLineInterface.parse("n").isFailure());
  }
  @Test void parsingANewCommandShortcutWithoutAPackageNameReports() {
    assertEquals("error: Provide a name for the created package!",
      CommandLineInterface.parse("n").error().toString());
  }
  @Test void parsingANewCommandShortcutWithAnInvalidPackageNameFails() {
    assertTrue(CommandLineInterface.parse("n", "package").isFailure());
  }
  @Test void parsingANewCommandShortcutWithAnInvalidPackageNameReports() {
    assertEquals("""
      error: Package name for the created package was invalid! Due to:
        error: Name must start with an uppercase English letter!""",
      CommandLineInterface.parse("n", "package").error().toString());
  }
  @Test void parsingANewCommandShortcutWithExtraArgumentsFails() {
    assertTrue(CommandLineInterface.parse("n", "Package", "extra", "arguments")
      .isFailure());
  }
  @Test void parsingANewCommandShortcutWithExtraArgumentsReports() {
    assertEquals("""
      error: There were extra arguments:
       - extra
       - arguments""", CommandLineInterface
      .parse("n", "Package", "extra", "arguments").error().toString());
  }
  @Test void
    parsingANewCommandShortcutWithAnInvalidPackageNameAndExtraArgumentsFails() {
    assertTrue(CommandLineInterface.parse("n", "package", "extra", "arguments")
      .isFailure());
  }
  @Test void
    parsingANewCommandShortcutWithAnInvalidPackageNameAndExtraArgumentsReports() {
    assertEquals("""
      error: Package name for the created package was invalid! Due to:
        error: Name must start with an uppercase English letter!
      error: There were extra arguments:
       - extra
       - arguments""", CommandLineInterface
      .parse("n", "package", "extra", "arguments").error().toString());
  }
  @Test void parsingANewCommandShortcutWithAValidPackageNameSucceeds() {
    assertTrue(CommandLineInterface.parse("n", "Package").isSuccess());
  }
  @Test void
    parsingANewCommandShortcutWithAValidPackageNameResultsInANewCommandWithTheGivenPackageName() {
    var name = "Package";
    assertEquals(name,
      ((Command.New) CommandLineInterface.parse("n", name).value()).created()
        .value());
  }

  @Test void parsingACheckCommandWithoutAPackageNameSucceeds() {
    assertTrue(CommandLineInterface.parse("check").isSuccess());
  }
  @Test void
    parsingACheckCommandWithoutAPackageNameResultsInACheckCommandWithEmptyCheckedPackagesList() {
    assertTrue(((Command.Check) CommandLineInterface.parse("check").value())
      .checked().isEmpty());
  }
  @Test void parsingACheckCommandWithAnInvalidPackageNameFails() {
    assertTrue(CommandLineInterface.parse("check", "package").isFailure());
  }
  @Test void parsingACheckCommandWithAnInvalidPackageNameReports() {
    assertEquals("""
      error: Checked package name `package` is invalid! Due to:
        error: Name must start with an uppercase English letter!""",
      CommandLineInterface.parse("check", "package").error().toString());
  }
  @Test void parsingACheckCommandWithInvalidPackageNamesFails() {
    assertTrue(CommandLineInterface
      .parse("check", "firstPackage", "secondPackage").isFailure());
  }
  @Test void parsingACheckCommandWithInvalidPackageNamesReports() {
    assertEquals(
      """
        error: Checked package name `firstPackage` is invalid! Due to:
          error: Name must start with an uppercase English letter!
        error: Checked package name `Second_Package` is invalid! Due to:
          error: Name must solely consist of English letters and decimal digits!""",
      CommandLineInterface.parse("check", "firstPackage", "Second_Package")
        .error().toString());
  }
  @Test void parsingACheckCommandWithValidPackageNamesSucceeds() {
    assertTrue(CommandLineInterface
      .parse("check", "FirstPackage", "SecondPackage").isSuccess());
  }
  @Test void
    parsingACheckCommandWithValidPackageNamesResultsInACheckCommandWithTheGivenPackageNames() {
    var names = List.of("FirstPackage", "SecondPackage");
    assertEquals(names.toString(),
      ((Command.Check) CommandLineInterface
        .parse("check", names.get(0), names.get(1)).value()).checked()
        .toString());
  }
}
