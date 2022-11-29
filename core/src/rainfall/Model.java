package rainfall;

import java.util.List;

sealed interface Model {
  record Linear(UTF8 contents) implements Model {}
  record Lexical(List<Lexeme> lexemes) implements Model {}
}
