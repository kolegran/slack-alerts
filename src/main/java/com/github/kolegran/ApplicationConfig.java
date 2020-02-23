package com.github.kolegran;

import com.github.kolegran.slackalerts.SlackNotificationSender;
import com.github.kolegran.slackalerts.SlackNotificationSenderStub;
import com.github.kolegran.slackalerts.bottoken.SlackSnippetSender;
import com.github.kolegran.slackalerts.webhook.HttpSlackNotificationSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {

    @Bean(destroyMethod = "shutdown")
    public SlackNotificationSender httpSlackNotificationSender(
        @Value("${slack.alerts.webhook.url:#{null}}") String webHookUrl
    ) {
        if (webHookUrl.isEmpty()) {
            return new SlackNotificationSenderStub();
        }

        final HttpClient httpClient = HttpClient.newBuilder()
            .executor(Executors.newSingleThreadExecutor())
            .build();

        return new HttpSlackNotificationSender(webHookUrl, httpClient);
    }

    @Bean
    public SlackNotificationSender slackAlertsSender(
        @Value("${slack.alerts.authorization.token:#{null}}") String token,
        @Value("${slack.alerts.upload.url}") String uploadUrl,
        @Value("${slack.alerts.channel:#{null}}") String channel
    ) {
        if (token.isEmpty() && channel.isEmpty()) {
            return new SlackNotificationSenderStub();
        }

        return new SlackSnippetSender(token, uploadUrl, channel, HttpClient.newHttpClient());
    }
}
