package rainfall.workspace;

import rainfall.utility.Result;
import rainfall.utility.Tester;

/**
 * Represents the name of a Thrice package, module, or source.
 *
 * @author calestialgem
 */
public class PhysicalName {
  /**
   * Name's value as a string.
   */
  public final String value;

  /**
   * Create a name from a string.
   *
   * @param  value String that is converted.
   * @return       Converted name, or error message.
   */
  public static Result<PhysicalName, String> of(final String value) {
    // Check whether there are any characters.
    if (value.isEmpty()) return Result.failure("Name cannot be empty!");

    final var contents = value.getBytes();

    // Check the initial character.
    if (!isUppercase(contents[0])) return Result
      .failure("Name must start with an uppercase English letter!");

    // Check rest of the characters.
    for (var character : contents) if (!isUppercase(character)
      && !isLowercase(character) && !isDigit(character))
      return Result.failure(
        "Name must solely consist of English letters and decimal digits!");

    return Result.success(new PhysicalName(value));
  }

  /**
   * @param  checked One byte long character that is checked.
   * @return         Whether the given character is an uppercase letter.
   */
  private static boolean isUppercase(final byte checked) {
    return inRange(checked, 'A', 'Z');
  }

  /**
   * @param  checked One byte long character that is checked.
   * @return         Whether the given character is a lowercase letter.
   */
  private static boolean isLowercase(final byte checked) {
    return inRange(checked, 'a', 'z');
  }

  /**
   * @param  checked One byte long character that is checked.
   * @return         Whether the given character is a decimal digit.
   */
  private static boolean isDigit(final byte checked) {
    return inRange(checked, '0', '9');
  }

  /**
   * @param  checked Integer that is checked.
   * @param  first   First integer in the range.
   * @param  last    Last integer in the range. Thus, the range is inclusive.
   * @return         Whether the given integer is in the given range.
   */
  private static boolean inRange(final int checked, final int first,
    final int last) {
    return checked >= first && checked <= last;
  }

  /**
   * Constructs a physical name.
   *
   * @param value Value of the constructed name.
   */
  private PhysicalName(final String value) { this.value = value; }

  /**
   * Run all the unit tests for {@link PhysicalName}.
   * 
   * @param tester Used unit test runner.
   */
  public static void test(final Tester tester) {
    tester.run(() -> !inRange(0, 1, 1));
    tester.run(() -> !inRange(2, 1, 1));
    tester.run(() -> inRange(1, 1, 1));

    tester.run(() -> isUppercase((byte) 'A'));
    tester.run(() -> isLowercase((byte) 'a'));
    tester.run(() -> isDigit((byte) '0'));

    tester.run(of("")::isFailure);
    tester.run(of("someName")::isFailure);
    tester.run(of("S")::isSuccess);
    tester.run(of("S0m3_N4m3")::isFailure);
    tester.run(of("SomeName")::isSuccess);
  }
}
