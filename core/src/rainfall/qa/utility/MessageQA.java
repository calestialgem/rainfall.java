package rainfall.qa.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import rainfall.utility.List;
import rainfall.utility.Message;

final class MessageQA {
  @Test void aFailureMessageResultIsFailure() {
    assertTrue(Message.failure("").isFailure());
  }
  @Test void aFailureMessageResultIsTitledAsError() {
    assertEquals("error: ", Message.failure("").error().toString());
  }
  @Test void aFailureMessageResultShowsTheGivenBody() {
    var body = "b";
    assertEquals("error: %s".formatted(body),
      Message.failure(body).error().toString());
  }
  @Test void aFailureMessageResultShowsTheGivenCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      error: %s Due to:
        error: %s""".formatted(bodies[0], bodies[1]),
      Message.failure(bodies[0], Message.error(bodies[1])).error().toString());
  }
  @Test void aFailureMessageResultShowsTheGivenCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      error: %s Due to:
        error: %s
        error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .failure(bodies[0], Message.error(bodies[1]), Message.error(bodies[2]))
        .error().toString());
  }
  @Test void aFailureMessageResultsIndentsTheGivenNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      error: %s Due to:
        error: %s Due to:
          error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .failure(bodies[0], Message.error(bodies[1], Message.error(bodies[2])))
        .error().toString());
  }

  @Test void anErrorMessageIsTitledAsError() {
    assertEquals("error: ", Message.error("").toString());
  }
  @Test void anErrorMessageShowsTheGivenBody() {
    var body = "b";
    assertEquals("error: %s".formatted(body), Message.error(body).toString());
  }
  @Test void anErrorMessageShowsTheGivenCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      error: %s Due to:
        error: %s""".formatted(bodies[0], bodies[1]),
      Message.error(bodies[0], Message.error(bodies[1])).toString());
  }
  @Test void anErrorMessageShowsTheGivenCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      error: %s Due to:
        error: %s
        error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .error(bodies[0], Message.error(bodies[1]), Message.error(bodies[2]))
        .toString());
  }
  @Test void anErrorMessageIndentsTheGivenNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      error: %s Due to:
        error: %s Due to:
          error: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message
        .error(bodies[0], Message.error(bodies[1], Message.error(bodies[2])))
        .toString());
  }

  @Test void aWarningMessageIsTitledAsWarning() {
    assertEquals("warning: ", Message.warning("").toString());
  }
  @Test void aWarningMessageShowsTheGivenBody() {
    var body = "b";
    assertEquals("warning: %s".formatted(body),
      Message.warning(body).toString());
  }
  @Test void aWarningMessageShowsTheGivenCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      warning: %s Due to:
        warning: %s""".formatted(bodies[0], bodies[1]),
      Message.warning(bodies[0], Message.warning(bodies[1])).toString());
  }
  @Test void aWarningMessageShowsTheGivenCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      warning: %s Due to:
        warning: %s
        warning: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.warning(bodies[0], Message.warning(bodies[1]),
        Message.warning(bodies[2])).toString());
  }
  @Test void aWarningMessageIndentsTheGivenNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      warning: %s Due to:
        warning: %s Due to:
          warning: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.warning(bodies[0],
        Message.warning(bodies[1], Message.warning(bodies[2]))).toString());
  }

  @Test void anInfoMessageIsTitledAsInfo() {
    assertEquals("info: ", Message.info("").toString());
  }
  @Test void anInfoMessageShowsTheGivenBody() {
    var body = "b";
    assertEquals("info: %s".formatted(body), Message.info(body).toString());
  }
  @Test void anInfoMessageShowsTheGivenCause() {
    var bodies = new String[] { "b", "c" };
    assertEquals("""
      info: %s Due to:
        info: %s""".formatted(bodies[0], bodies[1]),
      Message.info(bodies[0], Message.info(bodies[1])).toString());
  }
  @Test void anInfoMessageShowsTheGivenCauses() {
    var bodies = new String[] { "b", "c0", "c1" };
    assertEquals("""
      info: %s Due to:
        info: %s
        info: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.info(bodies[0], Message.info(bodies[1]), Message.info(bodies[2]))
        .toString());
  }
  @Test void anInfoMessageIndentsTheGivenNestedCauses() {
    var bodies = new String[] { "b", "c", "cc" };
    assertEquals("""
      info: %s Due to:
        info: %s Due to:
          info: %s""".formatted(bodies[0], bodies[1], bodies[2]),
      Message.info(bodies[0], Message.info(bodies[1], Message.info(bodies[2])))
        .toString());
  }

  @Test void creatingAGroupWithoutMembersThrows() {
    assertThrows(IllegalArgumentException.class, () -> Message.group());
  }
  @Test void aGroupShowsItsMember() {
    var body = "b";
    assertEquals("error: %s".formatted(body),
      Message.group(Message.error(body)).toString());
  }
  @Test void aGroupShowsItsMembers() {
    var bodies = new String[] { "b0", "b1" };
    assertEquals("""
      error: %s
      warning: %s""".formatted(bodies[0], bodies[1]), Message
      .group(Message.error(bodies[0]), Message.warning(bodies[1])).toString());
  }
  @Test void aGroupIndentsTheCausesOfItsMembers() {
    var bodies = new String[] { "b0", "b1" };
    assertEquals("""
      error: %s Due to:
        warning: %s""".formatted(bodies[0], bodies[1]), Message
      .group(Message.error(bodies[0], Message.warning(bodies[1]))).toString());
  }

  @Test void creatingAGroupWithEmptyMemberListThrows() {
    assertThrows(IllegalArgumentException.class,
      () -> Message.group(List.of()));
  }
  @Test void aGroupThatIsCreatedFromAListShowsTheMemberGivenInThatList() {
    var body = "b";
    assertEquals("error: %s".formatted(body),
      Message.group(List.of(Message.error(body))).toString());
  }
  @Test void aGroupThatIsCreatedFromAListShowsTheMembersGivenInThatList() {
    var bodies = new String[] { "b0", "b1" };
    assertEquals("""
      error: %s
      warning: %s""".formatted(bodies[0], bodies[1]),
      Message
        .group(List.of(Message.error(bodies[0]), Message.warning(bodies[1])))
        .toString());
  }
  @Test void aGroupThatIsCreatedFromAListIndentsTheCausesOfItsMembers() {
    var bodies = new String[] { "b0", "b1" };
    assertEquals("""
      error: %s Due to:
        warning: %s""".formatted(bodies[0], bodies[1]),
      Message
        .group(List.of(Message.error(bodies[0], Message.warning(bodies[1]))))
        .toString());
  }
}
