/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.start;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.thresholdsign.simulator.Simulator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class Start {

    private final Simulator simulator;

    @Value( "${startup.message}" )
    private String message;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.info(message);

        try {
            simulator.start();
        }
        catch ( Exception e ) {
            log.error( "Error running Similation", e );
        }
    }
}