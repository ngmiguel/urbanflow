package com.urbanflow.alert.infrastructure.messaging;

import com.urbanflow.alert.application.port.AlertEventPublisher;
import com.urbanflow.alert.application.port.AnomalyEventPublisher;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class NoOpAlertEventPublisher implements AlertEventPublisher, AnomalyEventPublisher {

    @Override
    public void publish(AlertTriggeredEvent event) {
    }

    @Override
    public void publish(AnomalyDetectedEvent event) {
    }
}
