package rainfall;

import java.util.Map;

record Lexeme(Type type, Portion portion) {
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
    EQUALS, COMMA, COLON, SEMICOLON, AT,

    // Literals
    CHARACTER, STRING, RAW_STRING, DECIMAL,

    // Visibility
    PRIVATE, PROTECTED, PUBLIC, IMPORT,

    // Definitions
    CONST, VAR, FUNC, PROC, ENTRYPOINT, IDENTIFIER,

    // Types
    INTERFACE, STRUCT, CLASS, ENUM, UNION, VARIANT,

    // Control Flow
    IF, ELSE, FOR, WHILE, DO, SWITCH, CASE, DEFAULT, FALLTHROUGH, BREAK,
    CONTINUE, RETURN, FREE,

    // Qualifiers
    MUTABLE, SHARED, VOLATILE, ALIGNAS, THREADLOCAL,

    UNKNOWN;
  }

  static final Map<UTF8, Lexeme.Type> MARKS =
    Map.ofEntries(Map.entry(UTF8.from("("), Lexeme.Type.OPENING_PARENTHESIS),
      Map.entry(UTF8.from(")"), Lexeme.Type.CLOSING_PARENTHESIS),
      Map.entry(UTF8.from("{"), Lexeme.Type.OPENING_BRACE),
      Map.entry(UTF8.from("}"), Lexeme.Type.CLOSING_BRACE),
      Map.entry(UTF8.from("["), Lexeme.Type.OPENING_BRACKET),
      Map.entry(UTF8.from("]"), Lexeme.Type.CLOSING_BRACKET),
      Map.entry(UTF8.from("'"), Lexeme.Type.PRIME),
      Map.entry(UTF8.from("\""), Lexeme.Type.QUOTE),
      Map.entry(UTF8.from("`"), Lexeme.Type.BACKTICK),
      Map.entry(UTF8.from("^="), Lexeme.Type.CARET_EQUALS),
      Map.entry(UTF8.from("^"), Lexeme.Type.CARET),
      Map.entry(UTF8.from("*="), Lexeme.Type.STAR_EQUALS),
      Map.entry(UTF8.from("*"), Lexeme.Type.STAR),
      Map.entry(UTF8.from("/="), Lexeme.Type.SLASH_EQUALS),
      Map.entry(UTF8.from("/"), Lexeme.Type.SLASH),
      Map.entry(UTF8.from("+="), Lexeme.Type.PLUS_EQUALS),
      Map.entry(UTF8.from("+"), Lexeme.Type.PLUS),
      Map.entry(UTF8.from("-="), Lexeme.Type.MINUS_EQUALS),
      Map.entry(UTF8.from("-"), Lexeme.Type.MINUS),
      Map.entry(UTF8.from("&="), Lexeme.Type.AMPERSAND_EQUALS),
      Map.entry(UTF8.from("&"), Lexeme.Type.AMPERSAND),
      Map.entry(UTF8.from("|="), Lexeme.Type.PIPE_EQUALS),
      Map.entry(UTF8.from("|"), Lexeme.Type.PIPE),
      Map.entry(UTF8.from("!"), Lexeme.Type.EXCLAMATION),
      Map.entry(UTF8.from("<="), Lexeme.Type.LEFT_ARROW_EQUALS),
      Map.entry(UTF8.from("<"), Lexeme.Type.LEFT_ARROW),
      Map.entry(UTF8.from(">="), Lexeme.Type.RIGHT_ARROW_EQUALS),
      Map.entry(UTF8.from(">"), Lexeme.Type.RIGHT_ARROW),
      Map.entry(UTF8.from("!="), Lexeme.Type.EXCLAMATION_EQUALS),
      Map.entry(UTF8.from("=="), Lexeme.Type.EQUALS_EQUALS),
      Map.entry(UTF8.from("$"), Lexeme.Type.DOLLAR),
      Map.entry(UTF8.from("~"), Lexeme.Type.TILDE),
      Map.entry(UTF8.from("."), Lexeme.Type.DOT),
      Map.entry(UTF8.from("="), Lexeme.Type.DOLLAR),
      Map.entry(UTF8.from(","), Lexeme.Type.COMMA),
      Map.entry(UTF8.from(":"), Lexeme.Type.COLON),
      Map.entry(UTF8.from(";"), Lexeme.Type.SEMICOLON),
      Map.entry(UTF8.from("@"), Lexeme.Type.AT));

  static final Map<UTF8, Lexeme.Type> KEYWORDS =
    Map.ofEntries(Map.entry(UTF8.from("private"), Lexeme.Type.PRIVATE),
      Map.entry(UTF8.from("protected"), Lexeme.Type.PROTECTED),
      Map.entry(UTF8.from("public"), Lexeme.Type.PUBLIC),
      Map.entry(UTF8.from("import"), Lexeme.Type.IMPORT),
      Map.entry(UTF8.from("const"), Lexeme.Type.CONST),
      Map.entry(UTF8.from("var"), Lexeme.Type.VAR),
      Map.entry(UTF8.from("func"), Lexeme.Type.FUNC),
      Map.entry(UTF8.from("proc"), Lexeme.Type.PROC),
      Map.entry(UTF8.from("entrypoint"), Lexeme.Type.ENTRYPOINT),
      Map.entry(UTF8.from("interface"), Lexeme.Type.INTERFACE),
      Map.entry(UTF8.from("struct"), Lexeme.Type.STRUCT),
      Map.entry(UTF8.from("class"), Lexeme.Type.CLASS),
      Map.entry(UTF8.from("enum"), Lexeme.Type.ENUM),
      Map.entry(UTF8.from("union"), Lexeme.Type.UNION),
      Map.entry(UTF8.from("variant"), Lexeme.Type.VARIANT),
      Map.entry(UTF8.from("if"), Lexeme.Type.IF),
      Map.entry(UTF8.from("else"), Lexeme.Type.ELSE),
      Map.entry(UTF8.from("for"), Lexeme.Type.FOR),
      Map.entry(UTF8.from("while"), Lexeme.Type.WHILE),
      Map.entry(UTF8.from("do"), Lexeme.Type.DO),
      Map.entry(UTF8.from("switch"), Lexeme.Type.SWITCH),
      Map.entry(UTF8.from("case"), Lexeme.Type.CASE),
      Map.entry(UTF8.from("default"), Lexeme.Type.DEFAULT),
      Map.entry(UTF8.from("fallthrough"), Lexeme.Type.FALLTHROUGH),
      Map.entry(UTF8.from("break"), Lexeme.Type.BREAK),
      Map.entry(UTF8.from("continue"), Lexeme.Type.CONTINUE),
      Map.entry(UTF8.from("return"), Lexeme.Type.RETURN),
      Map.entry(UTF8.from("free"), Lexeme.Type.FREE),
      Map.entry(UTF8.from("mutable"), Lexeme.Type.MUTABLE),
      Map.entry(UTF8.from("shared"), Lexeme.Type.SHARED),
      Map.entry(UTF8.from("volatile"), Lexeme.Type.VOLATILE),
      Map.entry(UTF8.from("alignas"), Lexeme.Type.ALIGNAS),
      Map.entry(UTF8.from("threadlocal"), Lexeme.Type.THREADLOCAL));
}
