package rainfall.workspace;

/**
 * Represents name of a Thrice package, module, or source.
 */
public class Name {
  /**
   * Convert the given value to a name.
   *
   * @param value Value that is converted.
   *
   * @return Converted name.
   *
   * @throws InvalidNameException If the value is invalid.
   */
  public static Name of(String value) throws InvalidNameException {
    // Check whether there is a value.
    if (value.isEmpty())
      throw new InvalidNameException("Name is empty!");

    // Check the first character.
    char initial = value.charAt(0);
    if ((initial < 'a' || initial > 'z') && initial != '_')
      throw new InvalidNameException(
        "Name must start with a lowercase English letter or an underscore!");

    // Check the rest of the characters.
    for (int index = 1; index < value.length(); index++) {
      char character = value.charAt(index);
      if ((character < 'a' || character > 'z')
        && (character < '0' || character > '9') && character != '_')
        throw new InvalidNameException(
          "Name must consist of lowercase English letters, decimal digits and underscores!");
    }

    // Check the value as a whole.
    return switch (value) {
      case "const", "let", "var", "bool", "signed", "unsigned", "byte", "short",
        "int", "long", "ixs", "float", "double", "class", "struct", "enum",
        "union", "variant", "module", "intern", "extern" ->
        throw new InvalidNameException("Name cannot be a reserved word!");
      default -> new Name(value);
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
  private Name(String value) {
    this.value = value;
  }
}
