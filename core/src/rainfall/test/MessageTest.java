package rainfall.test;

import org.junit.jupiter.api.Test;

import rainfall.Message;

import static org.junit.jupiter.api.Assertions.*;

final class MessageTest {
  @Test void failureIsResult() { assertTrue(Message.failure("").isFailure()); }
  @Test void failureIsError() {
    assertEquals("error: ", Message.failure("").error().toString());
  }
  @Test void failureShowsBody() {
    assertEquals("error: body", Message.failure("body").error().toString());
  }
  @Test void failureShowsCause() {
    assertEquals("""
      error: body Due to:
        error: cause""",
      Message.failure("body", Message.error("cause")).error().toString());
  }
  @Test void failureListsCauses() {
    assertEquals("""
      error: body Due to:
        error: cause1
        error: cause2""",
      Message.failure("body", Message.error("cause1"), Message.error("cause2"))
        .error().toString());
  }
  @Test void failureIndentsNestedCauses() {
    assertEquals("""
      error: body Due to:
        error: cause Due to:
          error: cause of cause""", Message
      .failure("body", Message.error("cause", Message.error("cause of cause")))
      .error().toString());
  }

  @Test void errorShowsCorrectTitle() {
    assertEquals("error: ", Message.error("").toString());
  }
  @Test void errorShowsBody() {
    assertEquals("error: body", Message.error("body").toString());
  }
  @Test void errorShowsCause() {
    assertEquals("""
      error: body Due to:
        error: cause""",
      Message.error("body", Message.error("cause")).toString());
  }
  @Test void errorListsCauses() {
    assertEquals("""
      error: body Due to:
        error: cause1
        error: cause2""",
      Message.error("body", Message.error("cause1"), Message.error("cause2"))
        .toString());
  }
  @Test void errorIndentsNestedCauses() {
    assertEquals("""
      error: body Due to:
        error: cause Due to:
          error: cause of cause""",
      Message
        .error("body", Message.error("cause", Message.error("cause of cause")))
        .toString());
  }

  @Test void warningShowsCorrectTitle() {
    assertEquals("warning: ", Message.warning("").toString());
  }
  @Test void warningShowsBody() {
    assertEquals("warning: body", Message.warning("body").toString());
  }
  @Test void warningShowsCause() {
    assertEquals("""
      warning: body Due to:
        warning: cause""",
      Message.warning("body", Message.warning("cause")).toString());
  }
  @Test void warningListsCauses() {
    assertEquals("""
      warning: body Due to:
        warning: cause1
        warning: cause2""",
      Message
        .warning("body", Message.warning("cause1"), Message.warning("cause2"))
        .toString());
  }
  @Test void warningIndentsNestedCauses() {
    assertEquals("""
      warning: body Due to:
        warning: cause Due to:
          warning: cause of cause""",
      Message
        .warning("body",
          Message.warning("cause", Message.warning("cause of cause")))
        .toString());
  }

  @Test void infoShowsCorrectTitle() {
    assertEquals("info: ", Message.info("").toString());
  }
  @Test void infoShowsBody() {
    assertEquals("info: body", Message.info("body").toString());
  }
  @Test void infoShowsCause() {
    assertEquals("""
      info: body Due to:
        info: cause""", Message.info("body", Message.info("cause")).toString());
  }
  @Test void infoListsCauses() {
    assertEquals("""
      info: body Due to:
        info: cause1
        info: cause2""", Message
      .info("body", Message.info("cause1"), Message.info("cause2")).toString());
  }
  @Test void infoIndentsNestedCauses() {
    assertEquals("""
      info: body Due to:
        info: cause Due to:
          info: cause of cause""",
      Message
        .info("body", Message.info("cause", Message.info("cause of cause")))
        .toString());
  }

  @Test void groupMustHaveMembers() {
    assertThrows(IllegalArgumentException.class, () -> Message.group());
  }
  @Test void groupShowsMember() {
    assertEquals("error: body",
      Message.group(Message.error("body")).toString());
  }
  @Test void groupListsMembers() {
    assertEquals("""
      error: body1
      warning: body2""", Message
      .group(Message.error("body1"), Message.warning("body2")).toString());
  }
  @Test void groupIndentsMemberCauses() {
    assertEquals("""
      error: body1 Due to:
        warning: body2""", Message
      .group(Message.error("body1", Message.warning("body2"))).toString());
  }
}
