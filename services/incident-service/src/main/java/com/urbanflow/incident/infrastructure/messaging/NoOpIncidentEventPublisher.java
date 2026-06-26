package com.urbanflow.incident.infrastructure.messaging;

import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.incident.application.port.IncidentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class NoOpIncidentEventPublisher implements IncidentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(NoOpIncidentEventPublisher.class);

    @Override
    public void publish(IncidentEvent event) {
        log.debug("No-op publish incident event for {}", event.incidentId());
    }
}
