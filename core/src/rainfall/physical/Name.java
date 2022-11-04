package rainfall.physical;

import rainfall.utility.List;
import rainfall.utility.Message;
import rainfall.utility.Result;

public final class Name {
  private final String value;
  private Name(String value) { this.value = value; }

  public static Result<Name, Message> of(String value) {
    var asASCII = value.getBytes();
    if (asASCII.length == 0) {
      return Message.failure("Name cannot be empty!");
    }
    var errorMessages = List.<Message>mutable();
    if (!isUppercase(asASCII[0])) {
      errorMessages.push(
        Message.error("Name must start with an uppercase English letter!"));
    }
    for (var character : asASCII) {
      if (!isValid(character)) {
        errorMessages.push(Message.error(
          "Name must solely consist of English letters and decimal digits!"));
        break;
      }
    }
    if (errorMessages.isFinite()) {
      return Result.failure(Message.group(errorMessages));
    }
    return Result.success(new Name(value));
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
