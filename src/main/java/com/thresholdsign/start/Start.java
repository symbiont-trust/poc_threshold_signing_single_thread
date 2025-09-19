package com.thresholdsign.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Start {

    @Value("${message}")
    private String message;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.info(message);
    }
}