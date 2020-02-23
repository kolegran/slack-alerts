package com.github.kolegran;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SlackAlertsApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SlackAlertsApplication.class, ApplicationConfig.class)
                .application()
                .run(args);
    }
}

