package com.github.kolegran.slackalerts;

import com.github.kolegran.slackalertcontroller.CreateSlackMessageCommand;

public class SlackNotificationSenderStub implements SlackNotificationSender {
    @Override
    public void send(CreateSlackMessageCommand command) {
        // do nothing
    }

    @Override
    public void shutdown() {
        // do nothing
    }
}
