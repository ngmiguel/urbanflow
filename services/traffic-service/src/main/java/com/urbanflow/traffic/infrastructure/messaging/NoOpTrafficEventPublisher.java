package com.urbanflow.traffic.infrastructure.messaging;

import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.traffic.application.port.TrafficEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class NoOpTrafficEventPublisher implements TrafficEventPublisher {

    @Override
    public void publish(TrafficUpdateEvent event) {
        // no-op for tests
    }
}
