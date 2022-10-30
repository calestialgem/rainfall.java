package rainfall.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Communication from the compiler to the user for errors, warnings and other
 * information.
 *
 * @author calestialgem
 */
public sealed interface Message {
  /**
   * A standalone error without causes.
   *
   * @param  type  Indicator of the message type.
   * @param  value Message value as a string.
   * @author       calestialgem
   */
  record Origin(String type, String value) implements Message {}

  /**
   * A group of messages that are in equal standing.
   *
   * @author calestialgem
   */
  public final class Group implements Message {
    /**
     * Messages in the group.
     */
    private final List<Message> messages;

    /**
     * Constructs a message group.
     *
     * @param messages Messages that will be in the constructed group.
     */
    public Group(final List<Message> messages) {
      this.messages = flatten(messages);
    }
  }

  /**
   * A message that is caused by a group of messages.
   *
   * @author calestialgem
   */
  public final class Outcome implements Message {
    /**
     * Indicator of the message type.
     */
    private final String type;

    /**
     * Message value as a string.
     */
    private final String value;

    /**
     * Messages that caused the message.
     */
    private final List<Message> causes;

    /**
     * Constructs an outcome message.
     *
     * @param type   Type of the constructed outcome message.
     * @param value  Value of the constructed outcome message.
     * @param causes Causes of the constructed outcome message.
     */
    public Outcome(String type, String value, List<Message> causes) {
      this.type   = type;
      this.value  = value;
      this.causes = flatten(causes);
    }
  }

  /**
   * Creates a failure with an error message.
   *
   * @param  <Value>      Type of the value that could have been resulted.
   * @param  messageValue Value of the error message that is resulted.
   * @return              Failure with an error message that has the given
   *                        value.
   */
  static <Value> Result<Value, Message> failure(final String messageValue) {
    return Result.<Value, Message>failure(error(messageValue));
  }

  /**
   * Creates a failure with a group of error messages.
   *
   * @param  <Value> Type of the value that could have been resulted.
   * @param  errors  Group of error messages that resulted.
   * @return         Failure with an error message that has the given value.
   */
  static <Value> Result<Value, Message> failure(final Message... errors) {
    return failure(List.of(errors));
  }

  /**
   * Creates a failure with a group of error messages.
   *
   * @param  <Value> Type of the value that could have been resulted.
   * @param  errors  Group of error messages that resulted.
   * @return         Failure with an error message that has the given value.
   */
  static <Value> Result<Value, Message> failure(final List<Message> errors) {
    return Result.<Value, Message>failure(group(errors));
  }

  /**
   * Creates a failure with an error message.
   *
   * @param  <Value>      Type of the value that could have been resulted.
   * @param  messageValue Value of the error message that is resulted.
   * @param  cause        Messages that explain the cause of the failure.
   * @return              Failure with an error message that has the given value
   *                        and cause.
   */
  static <Value> Result<Value, Message> failure(final String messageValue,
    final Message... cause) {
    return failure(messageValue, List.of(cause));
  }

  /**
   * Creates a failure with an error message.
   *
   * @param  <Value>      Type of the value that could have been resulted.
   * @param  messageValue Value of the error message that is resulted.
   * @param  cause        Messages that explain the cause of the failure.
   * @return              Failure with an error message that has the given value
   *                        and cause.
   */
  static <Value> Result<Value, Message> failure(final String messageValue,
    final List<Message> cause) {
    return Result.<Value, Message>failure(error(messageValue, cause));
  }

  /**
   * Creates an error message from a throwable.
   *
   * @param  throwable Throwable that provides the error message.
   * @return           Message with the type "error" and the localized message
   *                     of the given throwable.
   */
  static Message error(final Throwable throwable) {
    return error(throwable.getLocalizedMessage());
  }

  /**
   * Creates an error message.
   *
   * @param  value Message value as a string.
   * @return       Message with the type "error" and the given value.
   */
  static Message error(final String value) {
    return new Origin("error", value);
  }

  /**
   * Creates an error message.
   *
   * @param  value Message value as a string.
   * @param  cause Messages that caused the error.
   * @return       Message with the type "error", the given value and the given
   *                 cause.
   */
  static Message error(final String value, final Message... cause) {
    return error(value, List.of(cause));
  }

  /**
   * Creates an error message.
   *
   * @param  value Message value as a string.
   * @param  cause Messages that caused the error.
   * @return       Message with the type "error", the given value and the given
   *                 cause.
   */
  static Message error(final String value, final List<Message> cause) {
    return new Outcome("error", value, cause);
  }

  /**
   * Creates a message that is the linear grouping of all the given messages.
   *
   * @param  messages Messages that will be grouped.
   * @return          Message that contains all the given messages.
   */
  static Message group(final Message... messages) {
    return group(List.of(messages));
  }

  /**
   * Creates a message that is the linear grouping of all the given messages.
   *
   * @param  messages Messages that will be grouped.
   * @return          Message that contains all the given messages.
   */
  static Message group(final List<Message> messages) {
    if (messages.size() == 1) return messages.get(0);
    return new Group(messages);
  }

  /**
   * Flattens all the groups in the given messages to a single list of equal
   * level messages.
   *
   * @param  messages Messages that will be flattened together.
   * @return          Given messages with the groups flattened out.
   */
  private static List<Message> flatten(final List<Message> messages) {
    final var result = new ArrayList<Message>();
    for (final var message : messages) switch (message) {
    case Group group -> result.addAll(group.messages);
    default -> result.add(message);
    }
    return result;
  }

  /**
   * Prints the message to the user.
   */
  default void report() { report(0); }

  /**
   * Prints the message to the user after the given level of indentation. Used
   * for reporting causes indented compared to the outcomes.
   *
   * @param indentation Level of indentation to print before the message.
   */
  private void report(int indentation) {
    for (int i = 0; i < indentation; i++) System.out.print("  ");
    switch (this) {
    case Origin origin ->
      System.out.printf("%s: %s%n", origin.type, origin.value);
    case Group group ->
      group.messages.forEach(message -> message.report(indentation));
    case Outcome outcome -> {
      System.out.printf("%s: %s: caused by:%n", outcome.type, outcome.value);
      outcome.causes.forEach(cause -> cause.report(indentation + 1));
    }
    }
  }
}
