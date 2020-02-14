package slackalerts;

@FunctionalInterface
public interface SlackNotificationSender {
    void sendNotification() throws Exception;
}
