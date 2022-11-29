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

  static final Map<String, Lexeme.Type> MARKS = Map.ofEntries(
    Map.entry("(", Lexeme.Type.OPENING_PARENTHESIS),
    Map.entry(")", Lexeme.Type.CLOSING_PARENTHESIS),
    Map.entry("{", Lexeme.Type.OPENING_BRACE),
    Map.entry("}", Lexeme.Type.CLOSING_BRACE),
    Map.entry("[", Lexeme.Type.OPENING_BRACKET),
    Map.entry("]", Lexeme.Type.CLOSING_BRACKET),
    Map.entry("'", Lexeme.Type.PRIME), Map.entry("\"", Lexeme.Type.QUOTE),
    Map.entry("`", Lexeme.Type.BACKTICK),
    Map.entry("^=", Lexeme.Type.CARET_EQUALS),
    Map.entry("^", Lexeme.Type.CARET), Map.entry("*=", Lexeme.Type.STAR_EQUALS),
    Map.entry("*", Lexeme.Type.STAR), Map.entry("/=", Lexeme.Type.SLASH_EQUALS),
    Map.entry("/", Lexeme.Type.SLASH), Map.entry("+=", Lexeme.Type.PLUS_EQUALS),
    Map.entry("+", Lexeme.Type.PLUS), Map.entry("-=", Lexeme.Type.MINUS_EQUALS),
    Map.entry("-", Lexeme.Type.MINUS),
    Map.entry("&=", Lexeme.Type.AMPERSAND_EQUALS),
    Map.entry("&", Lexeme.Type.AMPERSAND),
    Map.entry("|=", Lexeme.Type.PIPE_EQUALS), Map.entry("|", Lexeme.Type.PIPE),
    Map.entry("!", Lexeme.Type.EXCLAMATION),
    Map.entry("<=", Lexeme.Type.LEFT_ARROW_EQUALS),
    Map.entry("<", Lexeme.Type.LEFT_ARROW),
    Map.entry(">=", Lexeme.Type.RIGHT_ARROW_EQUALS),
    Map.entry(">", Lexeme.Type.RIGHT_ARROW),
    Map.entry("!=", Lexeme.Type.EXCLAMATION_EQUALS),
    Map.entry("==", Lexeme.Type.EQUALS_EQUALS),
    Map.entry("$", Lexeme.Type.DOLLAR), Map.entry("~", Lexeme.Type.TILDE),
    Map.entry(".", Lexeme.Type.DOT), Map.entry("=", Lexeme.Type.EQUALS),
    Map.entry(",", Lexeme.Type.COMMA), Map.entry(":", Lexeme.Type.COLON),
    Map.entry(";", Lexeme.Type.SEMICOLON), Map.entry("@", Lexeme.Type.AT));

  static final Map<String, Lexeme.Type> KEYWORDS = Map.ofEntries(
    Map.entry("private", Lexeme.Type.PRIVATE),
    Map.entry("protected", Lexeme.Type.PROTECTED),
    Map.entry("public", Lexeme.Type.PUBLIC),
    Map.entry("import", Lexeme.Type.IMPORT),
    Map.entry("const", Lexeme.Type.CONST), Map.entry("var", Lexeme.Type.VAR),
    Map.entry("func", Lexeme.Type.FUNC), Map.entry("proc", Lexeme.Type.PROC),
    Map.entry("entrypoint", Lexeme.Type.ENTRYPOINT),
    Map.entry("free", Lexeme.Type.FREE),
    Map.entry("interface", Lexeme.Type.INTERFACE),
    Map.entry("struct", Lexeme.Type.STRUCT),
    Map.entry("class", Lexeme.Type.CLASS), Map.entry("enum", Lexeme.Type.ENUM),
    Map.entry("union", Lexeme.Type.UNION),
    Map.entry("variant", Lexeme.Type.VARIANT), Map.entry("if", Lexeme.Type.IF),
    Map.entry("else", Lexeme.Type.ELSE), Map.entry("for", Lexeme.Type.FOR),
    Map.entry("while", Lexeme.Type.WHILE), Map.entry("do", Lexeme.Type.DO),
    Map.entry("switch", Lexeme.Type.SWITCH),
    Map.entry("case", Lexeme.Type.CASE),
    Map.entry("default", Lexeme.Type.DEFAULT),
    Map.entry("fallthrough", Lexeme.Type.FALLTHROUGH),
    Map.entry("break", Lexeme.Type.BREAK),
    Map.entry("continue", Lexeme.Type.CONTINUE),
    Map.entry("return", Lexeme.Type.RETURN),
    Map.entry("mutable", Lexeme.Type.MUTABLE),
    Map.entry("shared", Lexeme.Type.SHARED),
    Map.entry("volatile", Lexeme.Type.VOLATILE),
    Map.entry("alignas", Lexeme.Type.ALIGNAS),
    Map.entry("threadlocal", Lexeme.Type.THREADLOCAL));
}
