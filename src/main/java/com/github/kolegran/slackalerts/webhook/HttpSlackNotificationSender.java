package com.github.kolegran.slackalerts.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kolegran.slackalertcontroller.CreateSlackMessageCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.kolegran.slackalerts.SlackNotificationSender;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpSlackNotificationSender implements SlackNotificationSender {
    private static final int REQUEST_TIMEOUT = 2;
    private static final int TERMINATION_TIMEOUT = 5;
    private static final String CONTENT_TYPE = "application/json";
    private static final String ACCEPT = "application/json";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HttpClient httpClient;
    private final String webHookUrl;

    public HttpSlackNotificationSender(String webHookUrl, HttpClient httpClient) {
        this.webHookUrl = webHookUrl;
        this.httpClient = httpClient;
    }

    @Override
    public void send(CreateSlackMessageCommand command) {
        try {
            HttpResponse<String> response = httpClient.send(prepareHttpRequest(command), HttpResponse.BodyHandlers.ofString());
            logger.info("The response from the Slack: {}", response.body());
        } catch (JsonProcessingException e) {
            logger.error("Cannot process JSON");
        } catch (IOException e) {
            logger.error("Cannot sending http request");
        } catch (InterruptedException e) {
            logger.error("InterruptedException during request sending");
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void shutdown() {
        final Optional<Executor> executor = httpClient.executor();
        if (executor.isPresent()) {
            final ExecutorService executorService = (ExecutorService) executor.get();
            executorService.shutdown();

            try {
                executorService.awaitTermination(TERMINATION_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("InterruptedException during awaitTermination() method calling");
            } finally {
                executorService.shutdownNow();
            }
        }
    }

    private HttpRequest prepareHttpRequest(CreateSlackMessageCommand cmd) throws JsonProcessingException {
        final SlackMessage slackMessage = new SlackMessage(cmd.getEmoji() + cmd.getContext());
        final String jsonRequest = new ObjectMapper().writeValueAsString(slackMessage);
        return HttpRequest.newBuilder()
                .uri(URI.create(webHookUrl))
                .timeout(Duration.ofMinutes(REQUEST_TIMEOUT))
                .header("Accept", ACCEPT)
                .header("Content-Type", CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
    }
}
