package rainfall;

import java.util.Arrays;

record Unicode(int[] codepoints, int offset, int length) {
  static Unicode from(String string) {
    var codepoints = string.codePoints().toArray();
    return new Unicode(codepoints, 0, codepoints.length);
  }

  int codepoint(int index) { return codepoints[offset + index]; }

  boolean startsWith(Unicode string, int index) {
    if (index + string.length >= length) return false;
    return Arrays.equals(codepoints, offset + index, index + string.length(),
      string.codepoints, string.offset, string.length);
  }

  boolean contains(int checked) {
    for (int i = offset; i < length; i++)
      if (codepoints[i] == checked) return true;
    return false;
  }

  Unicode sub(int first) { return sub(first, codepoints.length - 1); }

  Unicode sub(int first, int last) {
    return new Unicode(codepoints, offset + first, last - first + 1);
  }

  @Override public String toString() {
    return new String(codepoints, offset, length);
  }
}
