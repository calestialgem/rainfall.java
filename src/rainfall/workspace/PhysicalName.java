package rainfall.workspace;

import rainfall.utility.Result;
import rainfall.utility.Tester;

public class PhysicalName {
  public final String value;

  public static Result<PhysicalName, String> of(final String value) {
    if (value.isEmpty()) return Result.failure("Name cannot be empty!");

    final var contents = value.getBytes();

    if (!isUppercase(contents[0])) return Result
      .failure("Name must start with an uppercase English letter!");

    for (var character : contents) if (!isUppercase(character)
      && !isLowercase(character) && !isDigit(character))
      return Result.failure(
        "Name must solely consist of English letters and decimal digits!");

    return Result.success(new PhysicalName(value));
  }

  private static boolean isUppercase(byte checked) {
    return inRange(checked, 'A', 'Z');
  }

  private static boolean isLowercase(byte checked) {
    return inRange(checked, 'a', 'z');
  }

  private static boolean isDigit(byte checked) {
    return inRange(checked, '0', '9');
  }

  private static boolean inRange(int checked, int first, int last) {
    return checked >= first && checked <= last;
  }

  private PhysicalName(final String value) { this.value = value; }

  public static void test(Tester tester) {
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
