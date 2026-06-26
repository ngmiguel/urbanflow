package com.urbanflow.alert.application.port;

import com.urbanflow.events.alert.AlertTriggeredEvent;

public interface AlertEventPublisher {

    void publish(AlertTriggeredEvent event);
}
