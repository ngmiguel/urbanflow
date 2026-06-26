package com.urbanflow.iot.infrastructure.messaging;

import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.iot.application.port.SensorEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class NoOpSensorEventPublisher implements SensorEventPublisher {

    @Override
    public void publish(SensorRawEvent event) {
        // no-op for tests
    }
}
