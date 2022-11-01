package rainfall;

public final class PhysicalName {
  private final String value;
  private PhysicalName(String value) { this.value = value; }

  public static Result<PhysicalName, Message> of(String value) {
    var asASCII = value.getBytes();
    if (asASCII.length == 0) {
      return Message.failure("Name cannot be empty!");
    }
    if (!isUppercase(asASCII[0])) {
      return Message
        .failure("Name must start with an uppercase English letter!");
    }
    for (var character : asASCII) {
      if (!isValid(character)) {
        return Message.failure(
          "Name must solely consist of English letters and decimal digits!");
      }
    }
    return Result.success(new PhysicalName(value));
  }

  public String value() { return value; }

  private static boolean isValid(byte character) {
    return isUppercase(character) || isLowercase(character)
      || isDigit(character);
  }
  private static boolean isUppercase(byte character) {
    return character >= 'A' && character <= 'Z';
  }
  private static boolean isLowercase(byte character) {
    return character >= 'a' && character <= 'z';
  }
  private static boolean isDigit(byte character) {
    return character >= '0' && character <= '9';
  }
}
