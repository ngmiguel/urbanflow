package com.urbanflow.alert.application.port;

import com.urbanflow.events.alert.AnomalyDetectedEvent;

public interface AnomalyEventPublisher {

    void publish(AnomalyDetectedEvent event);
}
