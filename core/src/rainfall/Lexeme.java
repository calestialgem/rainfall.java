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
    CONST, VAR, FUNC, PROC, ENTRYPOINT, FREE,

    // Types
    INTERFACE, STRUCT, CLASS, ENUM, UNION, VARIANT,

    // Control Flow
    IF, ELSE, FOR, WHILE, DO, SWITCH, CASE, DEFAULT, FALLTHROUGH, BREAK,
    CONTINUE, RETURN,

    // Qualifiers
    MUTABLE, SHARED, VOLATILE, ALIGNAS, THREADLOCAL,

    IDENTIFIER, UNKNOWN;
  }

  static final Map<Unicode, Lexeme.Type> MARKS =
    Map.ofEntries(Map.entry(Unicode.from("("), Lexeme.Type.OPENING_PARENTHESIS),
      Map.entry(Unicode.from(")"), Lexeme.Type.CLOSING_PARENTHESIS),
      Map.entry(Unicode.from("{"), Lexeme.Type.OPENING_BRACE),
      Map.entry(Unicode.from("}"), Lexeme.Type.CLOSING_BRACE),
      Map.entry(Unicode.from("["), Lexeme.Type.OPENING_BRACKET),
      Map.entry(Unicode.from("]"), Lexeme.Type.CLOSING_BRACKET),
      Map.entry(Unicode.from("'"), Lexeme.Type.PRIME),
      Map.entry(Unicode.from("\""), Lexeme.Type.QUOTE),
      Map.entry(Unicode.from("`"), Lexeme.Type.BACKTICK),
      Map.entry(Unicode.from("^="), Lexeme.Type.CARET_EQUALS),
      Map.entry(Unicode.from("^"), Lexeme.Type.CARET),
      Map.entry(Unicode.from("*="), Lexeme.Type.STAR_EQUALS),
      Map.entry(Unicode.from("*"), Lexeme.Type.STAR),
      Map.entry(Unicode.from("/="), Lexeme.Type.SLASH_EQUALS),
      Map.entry(Unicode.from("/"), Lexeme.Type.SLASH),
      Map.entry(Unicode.from("+="), Lexeme.Type.PLUS_EQUALS),
      Map.entry(Unicode.from("+"), Lexeme.Type.PLUS),
      Map.entry(Unicode.from("-="), Lexeme.Type.MINUS_EQUALS),
      Map.entry(Unicode.from("-"), Lexeme.Type.MINUS),
      Map.entry(Unicode.from("&="), Lexeme.Type.AMPERSAND_EQUALS),
      Map.entry(Unicode.from("&"), Lexeme.Type.AMPERSAND),
      Map.entry(Unicode.from("|="), Lexeme.Type.PIPE_EQUALS),
      Map.entry(Unicode.from("|"), Lexeme.Type.PIPE),
      Map.entry(Unicode.from("!"), Lexeme.Type.EXCLAMATION),
      Map.entry(Unicode.from("<="), Lexeme.Type.LEFT_ARROW_EQUALS),
      Map.entry(Unicode.from("<"), Lexeme.Type.LEFT_ARROW),
      Map.entry(Unicode.from(">="), Lexeme.Type.RIGHT_ARROW_EQUALS),
      Map.entry(Unicode.from(">"), Lexeme.Type.RIGHT_ARROW),
      Map.entry(Unicode.from("!="), Lexeme.Type.EXCLAMATION_EQUALS),
      Map.entry(Unicode.from("=="), Lexeme.Type.EQUALS_EQUALS),
      Map.entry(Unicode.from("$"), Lexeme.Type.DOLLAR),
      Map.entry(Unicode.from("~"), Lexeme.Type.TILDE),
      Map.entry(Unicode.from("."), Lexeme.Type.DOT),
      Map.entry(Unicode.from("="), Lexeme.Type.EQUALS),
      Map.entry(Unicode.from(","), Lexeme.Type.COMMA),
      Map.entry(Unicode.from(":"), Lexeme.Type.COLON),
      Map.entry(Unicode.from(";"), Lexeme.Type.SEMICOLON),
      Map.entry(Unicode.from("@"), Lexeme.Type.AT));

  static final Map<Unicode, Lexeme.Type> KEYWORDS =
    Map.ofEntries(Map.entry(Unicode.from("private"), Lexeme.Type.PRIVATE),
      Map.entry(Unicode.from("protected"), Lexeme.Type.PROTECTED),
      Map.entry(Unicode.from("public"), Lexeme.Type.PUBLIC),
      Map.entry(Unicode.from("import"), Lexeme.Type.IMPORT),
      Map.entry(Unicode.from("const"), Lexeme.Type.CONST),
      Map.entry(Unicode.from("var"), Lexeme.Type.VAR),
      Map.entry(Unicode.from("func"), Lexeme.Type.FUNC),
      Map.entry(Unicode.from("proc"), Lexeme.Type.PROC),
      Map.entry(Unicode.from("entrypoint"), Lexeme.Type.ENTRYPOINT),
      Map.entry(Unicode.from("free"), Lexeme.Type.FREE),
      Map.entry(Unicode.from("interface"), Lexeme.Type.INTERFACE),
      Map.entry(Unicode.from("struct"), Lexeme.Type.STRUCT),
      Map.entry(Unicode.from("class"), Lexeme.Type.CLASS),
      Map.entry(Unicode.from("enum"), Lexeme.Type.ENUM),
      Map.entry(Unicode.from("union"), Lexeme.Type.UNION),
      Map.entry(Unicode.from("variant"), Lexeme.Type.VARIANT),
      Map.entry(Unicode.from("if"), Lexeme.Type.IF),
      Map.entry(Unicode.from("else"), Lexeme.Type.ELSE),
      Map.entry(Unicode.from("for"), Lexeme.Type.FOR),
      Map.entry(Unicode.from("while"), Lexeme.Type.WHILE),
      Map.entry(Unicode.from("do"), Lexeme.Type.DO),
      Map.entry(Unicode.from("switch"), Lexeme.Type.SWITCH),
      Map.entry(Unicode.from("case"), Lexeme.Type.CASE),
      Map.entry(Unicode.from("default"), Lexeme.Type.DEFAULT),
      Map.entry(Unicode.from("fallthrough"), Lexeme.Type.FALLTHROUGH),
      Map.entry(Unicode.from("break"), Lexeme.Type.BREAK),
      Map.entry(Unicode.from("continue"), Lexeme.Type.CONTINUE),
      Map.entry(Unicode.from("return"), Lexeme.Type.RETURN),
      Map.entry(Unicode.from("mutable"), Lexeme.Type.MUTABLE),
      Map.entry(Unicode.from("shared"), Lexeme.Type.SHARED),
      Map.entry(Unicode.from("volatile"), Lexeme.Type.VOLATILE),
      Map.entry(Unicode.from("alignas"), Lexeme.Type.ALIGNAS),
      Map.entry(Unicode.from("threadlocal"), Lexeme.Type.THREADLOCAL));
}
