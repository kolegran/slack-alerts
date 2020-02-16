package slackalerts;

@FunctionalInterface
public interface SlackNotificationSender {
    void send(String context);
}
