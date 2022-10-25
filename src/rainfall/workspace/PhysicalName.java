package rainfall.workspace;

import rainfall.utility.Result;

/**
 * Represents the name of physical elements of a Thrice program, which are
 * packages, modules, and sources.
 */
public final class PhysicalName {
  /**
   * Convert the given value to a name.
   *
   * @param value Value that is converted.
   *
   * @return Converted name.
   */
  public static Result<PhysicalName, String> of(String value) {
    // Check whether there is a value.
    if (value.isEmpty())
      return Result.ofFailure("Name is empty!");

    // Check the first character.
    char initial = value.charAt(0);
    if (initial < 'a' || initial > 'z')
      return Result
        .ofFailure("Name must start with a lowercase English letter!");

    // Check the rest of the characters.
    for (int index = 1; index < value.length(); index++) {
      char character = value.charAt(index);
      if ((character < 'a' || character > 'z')
        && (character < '0' || character > '9'))
        return Result.ofFailure(
          "Name must solely consist of lowercase English letters and decimal digits!");
    }

    // Check the value as a whole.
    return switch (value) {
      case "const", "let", "var", "bool", "signed", "unsigned", "byte", "short",
        "int", "long", "ixs", "float", "double", "class", "struct", "enum",
        "union", "variant", "module", "intern", "extern" ->
        Result.ofFailure("Name cannot be a reserved word!");
      default -> Result.ofSuccess(new PhysicalName(value));
    };
  }

  /**
   * Value of the name as a string.
   */
  public final String value;

  /**
   * Constructs a name.
   *
   * @param value Value of the constructed name.
   */
  private PhysicalName(String value) {
    this.value = value;
  }
}
