package com.urbanflow.event.infrastructure.messaging;

import com.urbanflow.event.application.port.UrbanEventPublisher;
import com.urbanflow.events.urban.UrbanPlannedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class NoOpUrbanEventPublisher implements UrbanEventPublisher {

    @Override
    public void publish(UrbanPlannedEvent event) {
    }
}
