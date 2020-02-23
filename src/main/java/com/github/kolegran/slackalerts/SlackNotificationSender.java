package com.github.kolegran.slackalerts;

import com.github.kolegran.slackalertcontroller.CreateSlackMessageCommand;

public interface SlackNotificationSender<T> {
    void send(CreateSlackMessageCommand command);
    void shutdown();
}
