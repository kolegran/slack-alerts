package com.github.kolegran.slackalertcontroller;

import java.util.Objects;

public class CreateSlackMessageCommand {
    private String emoji;
    private String context;

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateSlackMessageCommand that = (CreateSlackMessageCommand) o;
        return Objects.equals(emoji, that.emoji) &&
                Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emoji, context);
    }

    @Override
    public String toString() {
        return "SlackMessageCommand{" +
                "emoji='" + emoji + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
