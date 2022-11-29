package rainfall;

import java.nio.file.Path;

final class Main {
  public static void main(String[] arguments) {
    var linear  = Loader.load(Path.of("res"));
    var lexical = Lexer.lex(linear);
  }
}
