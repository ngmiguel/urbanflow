package com.urbanflow.incident.application.port;

import com.urbanflow.events.incident.IncidentEvent;

public interface IncidentEventPublisher {

    void publish(IncidentEvent event);
}
