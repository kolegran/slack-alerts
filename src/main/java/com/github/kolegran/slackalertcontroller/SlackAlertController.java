package com.github.kolegran.slackalertcontroller;

import com.github.kolegran.slackalerts.SlackNotificationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SlackAlertController {
    private final SlackNotificationSender<String> httpSlackNotificationSender;

    @Autowired
    public SlackAlertController(SlackNotificationSender<String> httpSlackNotificationSender) {
        this.httpSlackNotificationSender = httpSlackNotificationSender;
    }

    @RequestMapping("/sendByWebHook")
    public ResponseEntity<Void> sendSlackAlertByWebHook(@RequestBody CreateSlackMessageCommand command) {
        httpSlackNotificationSender.send(command);
        return ResponseEntity.ok().build();
    }
}
