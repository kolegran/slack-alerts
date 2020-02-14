package slackalerts;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class SlackNotificationSenderImpl implements SlackNotificationSender {
    private static final String HEADER_VALUE = "application/json";
    private static final int TIMEOUT = 1;
    private final String slackWebHookUrl;
    private final HttpClient httpClient;
    private final String context;

    public SlackNotificationSenderImpl(String context, String slackWebHookUrl, HttpClient httpClient) {
        this.slackWebHookUrl = slackWebHookUrl;
        this.httpClient = httpClient;
        this.context = context;
    }

    @Override
    public void sendNotification() throws Exception {
        try {
            String jsonRequest = new ObjectMapper().writeValueAsString(new SlackMessage(":fire:" + context));
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(slackWebHookUrl))
                    .timeout(Duration.ofMinutes(TIMEOUT))
                    .header("Accept", HEADER_VALUE)
                    .header("Content-Type", HEADER_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new Exception();
        }
    }
}
