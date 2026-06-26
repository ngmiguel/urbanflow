package com.urbanflow.event.application.port;

import com.urbanflow.events.urban.UrbanPlannedEvent;

public interface UrbanEventPublisher {

    void publish(UrbanPlannedEvent event);
}
