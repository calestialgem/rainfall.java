package rainfall.launcher;

/**
 * The request for compiler to execute in launch.
 */
public sealed abstract class Command permits Build, Check, New, Run, Test {
}
