package com.urbanflow.iot.application.port;

import com.urbanflow.events.sensor.SensorRawEvent;

public interface SensorEventPublisher {

    void publish(SensorRawEvent event);
}
