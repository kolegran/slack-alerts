package application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slackalerts.HttpSlackNotificationSender;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {
    private static final int TERMINATION_TIMEOUT = 1;
    private static final String SLACK_URL = "secretUrl";
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        final HttpClient httpClient = buildHttpClient();
        final HttpSlackNotificationSender slackNotificationSender = new HttpSlackNotificationSender(SLACK_URL, httpClient);
        slackNotificationSender.send("Something went wrong");
        shutdown(httpClient);
    }

    private static HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .executor(Executors.newSingleThreadExecutor())
                .build();
    }

    private static void shutdown(HttpClient httpClient) {
        final ExecutorService executor = (ExecutorService) httpClient.executor().get();
        executor.shutdown();
        try {
            executor.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("InterruptedException during awaitTermination() method calling");
        } finally {
            executor.shutdownNow();
        }
    }
}
