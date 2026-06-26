package com.urbanflow.simulator.infrastructure.messaging;

import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.simulator.application.port.SimulationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class NoOpSimulationEventPublisher implements SimulationEventPublisher {

    @Override
    public void publishTraffic(TrafficUpdateEvent event) {
    }

    @Override
    public void publishSensor(SensorRawEvent event) {
    }
}
