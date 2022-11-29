package rainfall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class Lexer {
  static List<Lexeme> lex(Source lexed) { return new Lexer(lexed).lex(); }

  private static final Map<UTF8, Lexeme.Type> MARKS =
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

  private static final Map<UTF8, Lexeme.Type> KEYWORDS =
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

  private final Source lexed;
  private int          nextCharacter;
  private List<Lexeme> lex;

  private Lexer(Source lexed) {
    this.lexed    = lexed;
    nextCharacter = 0;
    lex           = new ArrayList<>();
  }

  private List<Lexeme> lex() {
    while (nextCharacter < lexed.contents().length()) {
      if (skip() || lexMark() || lexCharacterLiteral() || lexStringLiteral()
        || lexRawStringLiteral() || lexDecimalLiteral() || lexWord()) continue;
      lex.add(new Lexeme(Lexeme.Type.UNKNOWN,
        Portion.at(lexed, nextCharacter, nextCharacter + 1)));
      nextCharacter++;
    }

    return lex;
  }

  private boolean skip() { return skipWhitespace() || skipComments(); }

  private boolean skipWhitespace() { return takeCharacter(' '); }

  private boolean skipComments() {
    if (!takeCharacter('#')) return false;
    if (!takeCharacter('{')) while (!takeCharacter('\n')) nextCharacter++;
    else while (!takeString(UTF8.from("}#"))) nextCharacter++;
    return true;
  }

  private boolean lexMark() {
    var startCharacter = nextCharacter;
    for (var markString : MARKS.keySet()) {
      if (takeString(markString)) {
        addLexeme(MARKS.get(markString), startCharacter);
        return true;
      }
    }
    return false;
  }

  private boolean lexCharacterLiteral() {
    var startCharacter = nextCharacter;
    if (!takeCharacter('\'')) return false;
    if (takeCharacter('\'')) throw new RuntimeException();
    takeEscapedCharacter();
    if (!takeCharacter('\'')) throw new RuntimeException();
    addLexeme(Lexeme.Type.CHARACTER, startCharacter);
    return true;
  }

  private boolean lexStringLiteral() {
    var startCharacter = nextCharacter;
    if (!takeCharacter('\'')) return false;
    while (!takeCharacter('"')) takeEscapedCharacter();
    addLexeme(Lexeme.Type.STRING, startCharacter);
    return true;
  }

  private boolean lexRawStringLiteral() {
    var startCharacter = nextCharacter;
    if (!takeCharacter('`')) return false;
    while (!takeCharacter('`')) nextCharacter++;
    addLexeme(Lexeme.Type.RAW_STRING, startCharacter);
    return true;
  }

  private boolean lexDecimalLiteral() {
    var startCharacter = nextCharacter;
    if (!takeDecimalDigit()) { return false; }
    while (takeDecimalDigit() || takeCharacter('_')) {}

    var startFraction = nextCharacter;
    if (takeCharacter('.')) {
      if (!takeDecimalDigit()) {
        nextCharacter = startFraction; // Retreat from '.'
      } else {
        while (takeDecimalDigit() || takeCharacter('_')) {}
      }
    }

    if (takeAny(UTF8.from("eE"))) {
      if (!takeDecimalDigit()) throw new RuntimeException();
      while (takeDecimalDigit() || takeCharacter('_')) {}
    }

    addLexeme(Lexeme.Type.DECIMAL, startCharacter);
    return true;
  }

  private boolean lexWord() {
    var startCharacter = nextCharacter;

    if (takeUppercaseLetter()) {
      while (takeAlphabetic()) {}
      addLexeme(Lexeme.Type.IDENTIFIER, startCharacter);
      return true;
    }

    if (!takeLowercaseLetter()) return false;
    while (takeAlphabetic()) {}

    var initialPortion = getPortion(startCharacter).contents();
    if (!KEYWORDS.containsKey(initialPortion)) {
      addLexeme(Lexeme.Type.IDENTIFIER, startCharacter);
    } else {
      if (takeCharacter('_')) {
        addLexeme(Lexeme.Type.IDENTIFIER, startCharacter);
      } else {
        addLexeme(KEYWORDS.get(initialPortion), startCharacter);
      }
    }

    return true;
  }

  private void takeEscapedCharacter() {
    if (getNext() == '\n') throw new RuntimeException();
    if (!takeCharacter('\\')) {
      nextCharacter++;
      return;
    }
    if (takeAny(UTF8.from("\\'\"`"))) return;
    int digitCount = 0;
    while (takeHexadecimalDigit()) { digitCount++; }
    if (digitCount == 0 || digitCount > 8) throw new RuntimeException();
  }

  private boolean takeAny(UTF8 alternatives) {
    if (!alternatives.contains(getNext())) return false;
    nextCharacter++;
    return true;
  }

  private boolean takeAlphabetic() {
    return takeUppercaseLetter() || takeLowercaseLetter();
  }

  private boolean takeUppercaseLetter() { return takeRange('A', 'Z'); }

  private boolean takeLowercaseLetter() { return takeRange('a', 'z'); }

  private boolean takeHexadecimalDigit() {
    return takeDecimalDigit() || takeRange('a', 'f') || takeRange('A', 'F');
  }

  private boolean takeDecimalDigit() { return takeRange('0', '9'); }

  private boolean takeRange(int first, int last) {
    if (getNext() < first || getNext() > last) return false;
    nextCharacter++;
    return true;
  }

  private boolean takeString(UTF8 taken) {
    if (!lexed.contents().startsWith(taken, nextCharacter)) { return false; }
    nextCharacter += taken.length();
    return true;
  }

  private boolean takeCharacter(int taken) {
    if (getNext() != taken) { return false; }
    nextCharacter++;
    return true;
  }

  private void addLexeme(Lexeme.Type type, int startCharacter) {
    lex.add(new Lexeme(type, getPortion(startCharacter)));
  }

  private Portion getPortion(int startCharacter) {
    return Portion.at(lexed, startCharacter, nextCharacter);
  }

  private int getNext() { return lexed.contents().codepoint(nextCharacter); }
}
