package rainfall.workspace;

import rainfall.Result;

public record Designation(String value) {
  public static Result<Designation, String> of(String value) {
    // Check whether it has characters.
    if (value.isEmpty()) return Result.failure("Designation cannot be empty!");
    var contents = value.getBytes();
    // Check the initial character.
    if (!isLowercase(contents[0])) return Result
        .failure("Designation must start with a lowercase English letter!");
    // Check the remaining characters.
    for (var character = 1; character < contents.length; character++)
      if (!isValid(contents[character])) return Result.failure(
          "Designation must solely consist of lowercase English letters and decimal digits!");
    // Check the value as a whole.
    return switch (value) {
    case "const", "let", "var", "module", "intern", "extern", "bool", "byte",
        "short", "int", "long", "ixs", "signed", "unsigned", "float", "double",
        "class", "struct", "enum", "union", "interface", "operator", "static" ->
      Result.failure("Designation cannot be a keyword!");
    default -> Result.success(new Designation(value));
    };
  }

  private static boolean isValid(byte character) {
    return isLowercase(character) || isDecimal(character);
  }

  private static boolean isLowercase(byte character) {
    return inRange(character, (byte) 'a', (byte) 'z');
  }

  private static boolean isDecimal(byte character) {
    return inRange(character, (byte) '0', (byte) '9');
  }

  private static boolean inRange(byte value, byte first, byte last) {
    return value >= first && value <= last;
  }
}
