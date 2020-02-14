package application;

import slackalerts.SlackNotificationSenderImpl;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {
    private static final int TERMINATION_TIMEOUT = 1;
    private static final String SLACK_URL = "secretUrl";

    public static void main(String[] args) {
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .executor(Executors.newSingleThreadExecutor())
                    .build();

            SlackNotificationSenderImpl slackNotificationSender = new SlackNotificationSenderImpl("Something went wrong", SLACK_URL, httpClient);
            slackNotificationSender.sendNotification();

            ExecutorService executor = (ExecutorService) httpClient.executor().get();
            executor.shutdown();
            executor.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            System.err.println("Something went wrong");
        }
    }
}
