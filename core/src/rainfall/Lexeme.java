package rainfall;

final class Lexeme {
  static enum Type {
    // Deliminator
    OPENING_PARENTHESIS, CLOSING_PARENTHESIS, OPENING_BRACE, CLOSING_BRACE,
    OPENING_BRACKET, CLOSING_BRACKET, PRIME, QUOTE, BACKTICK,

    // Arithmetic
    CARET_EQUALS, CARET, STAR_EQUALS, STAR, SLASH_EQUALS, SLASH, PLUS_EQUALS,
    PLUS, MINUS_EQUALS, MINUS,

    // Logical
    AMPERSAND_EQUALS, AMPERSAND, PIPE_EQUALS, PIPE, EXCLAMATION,

    // Comparison
    LEFT_ARROW_EQUALS, LEFT_ARROW, RIGHT_ARROW_EQUALS, RIGHT_ARROW,
    EXCLAMATION_EQUALS, EQUALS_EQUALS,

    // Other Operators
    DOLLAR, TILDE, DOT,

    // Markers
    EQUALS, COMMA, COLON, SEMICOLON, HASH, EOF,

    // Visibility
    PRIVATE, PROTECTED, PUBLIC, IMPORT,

    // Definitions
    CONST, VAR, FUNC, PROC, ENTRY_POINT,

    // Types
    INTERFACE, STRUCT, CLASS, ENUM, UNION, VARIANT,

    // Control Flow
    IF, ELSE, FOR, WHILE, DO, SWITCH, CASE, DEFAULT, FALLTHROUGH, BREAK,
    CONTINUE, RETURN, FREE,

    // Qualifiers
    MUTABLE, SHARED, VOLATILE, ALIGNAS, THREADLOCAL,

    // Attributes
    DEPRECATED, DISCARDABLE, MAYBE_UNUSED, NORETURN,

    // Other
    IDENTIFIER, DECIMAL_LITERAL;
  }

  private final Type    type;
  private final Portion portion;
  private Lexeme(Type type, Portion portion) {
    this.type    = type;
    this.portion = portion;
  }
}
