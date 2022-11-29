package rainfall;

import java.util.List;

sealed interface Model {
  record Linear(String contents) implements Model {}
  record Lexical(List<Lexeme> lexemes) implements Model {}
}
