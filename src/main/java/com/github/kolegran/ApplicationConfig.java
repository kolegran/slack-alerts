package com.github.kolegran;

import com.github.kolegran.slackalerts.SlackNotificationSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.kolegran.slackalerts.bottoken.SlackSnippetSender;
import com.github.kolegran.slackalerts.webhook.HttpSlackNotificationSender;

import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {

    @Bean(destroyMethod = "shutdown")
    public SlackNotificationSender<String> httpSlackNotificationSender(
        @Value("${slack.alerts.webhook.url}") String webHookUrl
    ) {
        final HttpClient httpClient = HttpClient.newBuilder()
            .executor(Executors.newSingleThreadExecutor())
            .build();

        return new HttpSlackNotificationSender(webHookUrl, httpClient);
    }

    @Bean
    public SlackNotificationSender<List<StackTraceElement>> slackAlertsSender(
        @Value("${slack.alerts.authorization.token}") String token,
        @Value("${slack.alerts.upload.url}") String uploadUrl,
        @Value("${slack.alerts.channel}") String channel
    ) {
        return new SlackSnippetSender(token, uploadUrl, channel, HttpClient.newHttpClient());
    }
}
