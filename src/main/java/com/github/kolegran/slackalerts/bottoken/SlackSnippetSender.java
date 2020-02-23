package com.github.kolegran.slackalerts.bottoken;

import com.github.kolegran.slackalertcontroller.CreateSlackMessageCommand;
import com.github.kolegran.slackalerts.SlackNotificationSender;

import java.net.http.HttpClient;
import java.util.List;

public class SlackSnippetSender implements SlackNotificationSender<List<StackTraceElement>> {
    private final String authorizationToken;
    private final String uploadUrl;
    private final String channel;

    public SlackSnippetSender(String authorizationToken, String uploadUrl, String channel, HttpClient httpClient) {
        this.authorizationToken = authorizationToken;
        this.uploadUrl = uploadUrl;
        this.channel = channel;
    }

    @Override
    public void send(CreateSlackMessageCommand command) {
    }

    @Override
    public void shutdown() {
    }
}
