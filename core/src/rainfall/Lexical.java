package rainfall;

import java.util.List;

record Lexical(List<Lexeme> contents) {

  @Override public String toString() {
    var buffer = new StringBuilder();
    for (var lexeme : contents)
      buffer.append(lexeme).append(System.lineSeparator());
    return buffer.toString();
  }
}
