package com.github.kolegran.slackalerts.webhook;

public class SlackMessage {
    private final String text;

    public SlackMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
