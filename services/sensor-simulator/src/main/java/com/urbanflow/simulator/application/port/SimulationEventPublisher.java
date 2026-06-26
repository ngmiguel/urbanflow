package com.urbanflow.simulator.application.port;

import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;

public interface SimulationEventPublisher {

    void publishTraffic(TrafficUpdateEvent event);

    void publishSensor(SensorRawEvent event);
}
