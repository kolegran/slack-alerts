package com.github.kolegran.slackalerts;

import com.github.kolegran.slackalertcontroller.CreateSlackMessageCommand;

public interface SlackNotificationSender {
    void send(CreateSlackMessageCommand command);
    void shutdown();
}
