package rainfall;

import java.util.Arrays;

record UTF8(int[] codepoints) {
  static UTF8 from(String string) {
    return new UTF8(string.codePoints().toArray());
  }

  int length() { return codepoints.length; }
  int codepoint(int index) { return codepoints[index]; }
  boolean startsWith(UTF8 string, int offset) {
    return Arrays.equals(codepoints, offset, offset + string.length(),
      string.codepoints, 0, string.length());
  }
  boolean contains(int checked) {
    for (var codepoint : codepoints) if (codepoint == checked) return true;
    return false;
  }
  UTF8 sub(int first) { return sub(first, codepoints.length - 1); }
  UTF8 sub(int first, int last) {
    return new UTF8(Arrays.copyOfRange(codepoints, first, last + 1));
  }
}
