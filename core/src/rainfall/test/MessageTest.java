package rainfall.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.Message;

final class MessageTest {
  @Test void failureIsResult() { assertTrue(Message.failure("").isFailure()); }
  @Test void failureIsError() {
    assertEquals("error: ", Message.failure("").error().toString());
  }
  @Test void failureShowsBody() {
    var body = "b";
    assertEquals("error: %s".formatted(body),
      Message.failure(body).error().toString());
  }
  @Test void failureShowsCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      error: %s Due to:
        error: %s""".formatted(bodies[0], bodies[1]),
      Message.failure(bodies[0], Message.error(bodies[1])).error().toString());
  }
  @Test void failureListsCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      error: %s Due to:
        error: %s
        error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .failure(bodies[0], Message.error(bodies[1]), Message.error(bodies[2]))
        .error().toString());
  }
  @Test void failureIndentsNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      error: %s Due to:
        error: %s Due to:
          error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .failure(bodies[0], Message.error(bodies[1], Message.error(bodies[2])))
        .error().toString());
  }

  @Test void errorShowsCorrectTitle() {
    assertEquals("error: ", Message.error("").toString());
  }
  @Test void errorShowsBody() {
    var body = "b";
    assertEquals("error: %s".formatted(body), Message.error(body).toString());
  }
  @Test void errorShowsCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      error: %s Due to:
        error: %s""".formatted(bodies[0], bodies[1]),
      Message.error(bodies[0], Message.error(bodies[1])).toString());
  }
  @Test void errorListsCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      error: %s Due to:
        error: %s
        error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .error(bodies[0], Message.error(bodies[1]), Message.error(bodies[2]))
        .toString());
  }
  @Test void errorIndentsNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      error: %s Due to:
        error: %s Due to:
          error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .error(bodies[0], Message.error(bodies[1], Message.error(bodies[2])))
        .toString());
  }

  @Test void warningShowsCorrectTitle() {
    assertEquals("warning: ", Message.warning("").toString());
  }
  @Test void warningShowsBody() {
    var body = "b";
    assertEquals("warning: %s".formatted(body),
      Message.warning(body).toString());
  }
  @Test void warningShowsCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      warning: %s Due to:
        warning: %s""".formatted(bodies[0], bodies[1]),
      Message.warning(bodies[0], Message.warning(bodies[1])).toString());
  }
  @Test void warningListsCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      warning: %s Due to:
        warning: %s
        warning: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.warning(bodies[0], Message.warning(bodies[1]),
        Message.warning(bodies[2])).toString());
  }
  @Test void warningIndentsNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      warning: %s Due to:
        warning: %s Due to:
          warning: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.warning(bodies[0],
        Message.warning(bodies[1], Message.warning(bodies[2]))).toString());
  }

  @Test void infoShowsCorrectTitle() {
    assertEquals("info: ", Message.info("").toString());
  }
  @Test void infoShowsBody() {
    var body = "b";
    assertEquals("info: %s".formatted(body), Message.info(body).toString());
  }
  @Test void infoShowsCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      info: %s Due to:
        info: %s""".formatted(bodies[0], bodies[1]),
      Message.info(bodies[0], Message.info(bodies[1])).toString());
  }
  @Test void infoListsCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      info: %s Due to:
        info: %s
        info: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.info(bodies[0], Message.info(bodies[1]), Message.info(bodies[2]))
        .toString());
  }
  @Test void infoIndentsNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      info: %s Due to:
        info: %s Due to:
          info: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.info(bodies[0], Message.info(bodies[1], Message.info(bodies[2])))
        .toString());
  }

  @Test void groupMustHaveMembers() {
    assertThrows(IllegalArgumentException.class, () -> Message.group());
  }
  @Test void groupShowsMember() {
    var body = "b";
    assertEquals("error: %s".formatted(body),
      Message.group(Message.error(body)).toString());
  }
  @Test void groupListsMembers() {
    var bodies = new String[] { "b0", "b1" };
    assertEquals("""
      error: %s
      warning: %s""".formatted(bodies[0], bodies[1]), Message
      .group(Message.error(bodies[0]), Message.warning(bodies[1])).toString());
  }
  @Test void groupIndentsMemberCauses() {
    var bodies = new String[] { "b0", "b1" };
    assertEquals("""
      error: %s Due to:
        warning: %s""".formatted(bodies[0], bodies[1]), Message
      .group(Message.error(bodies[0], Message.warning(bodies[1]))).toString());
  }
}
