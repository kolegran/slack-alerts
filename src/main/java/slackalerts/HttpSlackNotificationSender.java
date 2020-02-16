package slackalerts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpSlackNotificationSender implements SlackNotificationSender {
    private static final String MIME_TYPE = "application/json";
    private static final int TIMEOUT = 1;
    private final String slackWebHookUrl;
    private final HttpClient httpClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HttpSlackNotificationSender(String slackWebHookUrl, HttpClient httpClient) {
        this.slackWebHookUrl = slackWebHookUrl;
        this.httpClient = httpClient;
    }

    @Override
    public void send(String context) {
        try {
            httpClient.send(prepareHttpRequest(context), HttpResponse.BodyHandlers.discarding());
        } catch (JsonProcessingException e) {
            logger.error("Cannot process JSON");
        } catch (IOException e) {
            logger.error("Cannot sending http request");
        } catch (InterruptedException e) {
            logger.error("InterruptedException during request sending");
            Thread.currentThread().interrupt();
        }
    }

    private HttpRequest prepareHttpRequest(String context) throws JsonProcessingException {
        final String jsonRequest = new ObjectMapper().writeValueAsString(new SlackMessage(":fire:" + context));
        return HttpRequest.newBuilder()
                .uri(URI.create(slackWebHookUrl))
                .timeout(Duration.ofMinutes(TIMEOUT))
                .header("Accept", MIME_TYPE)
                .header("Content-Type", MIME_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
    }
}
