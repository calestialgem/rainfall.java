package rainfall.utility;

public final class Timer {
  public static double currentMilliseconds() {
    return System.nanoTime() * 1e-6;
  }

  public static double elapsedMilliseconds(double startMilliseconds) {
    return currentMilliseconds() - startMilliseconds;
  }

  private Timer() {}
}
