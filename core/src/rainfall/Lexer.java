package rainfall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

final class Lexer {
  static Workspace<Lexical> lex(Workspace<Linear> lexed) {
    var packages = new HashMap<UTF8, Package<Lexical>>();
    for (var package_ : lexed.packages().entrySet()) {
      packages.put(package_.getKey(), lex(package_.getValue()));
    }
    return new Workspace<>(Collections.unmodifiableMap(packages));
  }

  private static Package<Lexical> lex(Package<Linear> lexed) {
    return switch (lexed) {
    case Package.Directory<Linear> directory -> lex(directory);
    case Package.File<Linear> file -> lex(file);
    };
  }

  private static Package.Directory<Lexical>
    lex(Package.Directory<Linear> lexed) {
    return new Package.Directory<>(lex(lexed.module()));
  }

  private static Package.File<Lexical> lex(Package.File<Linear> lexed) {
    return new Package.File<>(lex(lexed.source()));
  }

  private static Module<Lexical> lex(Module<Linear> lexed) {
    var sources = new HashMap<UTF8, Source<Lexical>>();
    for (var source : lexed.sources().entrySet()) {
      sources.put(source.getKey(), lex(source.getValue()));
    }

    var submodules = new HashMap<UTF8, Module<Lexical>>();
    for (var submodule : lexed.submodules().entrySet()) {
      submodules.put(submodule.getKey(), lex(submodule.getValue()));
    }

    return new Module<>(lexed.path(), lexed.name(),
      Collections.unmodifiableMap(sources),
      Collections.unmodifiableMap(submodules));
  }

  private static Source<Lexical> lex(Source<Linear> lexed) {
    return new Source<Lexical>(lexed.path(), lexed.name(),
      new Lexer(lexed.model()).lex());
  }

  private final Linear lexed;
  private int          nextCharacter;
  private List<Lexeme> lex;

  private Lexer(Linear lexed) {
    this.lexed    = lexed;
    nextCharacter = 0;
    lex           = new ArrayList<>();
  }

  private Lexical lex() {
    while (nextCharacter < lexed.contents().length()) {
      if (skip() || lexMark() || lexCharacterLiteral() || lexStringLiteral()
        || lexRawStringLiteral() || lexDecimalLiteral() || lexWord()) continue;
      lex.add(new Lexeme(Lexeme.Type.UNKNOWN,
        Portion.at(lexed.contents(), nextCharacter, nextCharacter + 1)));
      nextCharacter++;
    }

    return new Lexical(Collections.unmodifiableList(lex));
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
    for (var markString : Lexeme.MARKS.keySet()) {
      if (takeString(markString)) {
        addLexeme(Lexeme.MARKS.get(markString), startCharacter);
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
    if (!Lexeme.KEYWORDS.containsKey(initialPortion)) {
      addLexeme(Lexeme.Type.IDENTIFIER, startCharacter);
    } else {
      if (takeCharacter('_')) {
        addLexeme(Lexeme.Type.IDENTIFIER, startCharacter);
      } else {
        addLexeme(Lexeme.KEYWORDS.get(initialPortion), startCharacter);
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
    return Portion.at(lexed.contents(), startCharacter, nextCharacter);
  }

  private int getNext() { return lexed.contents().codepoint(nextCharacter); }
}
