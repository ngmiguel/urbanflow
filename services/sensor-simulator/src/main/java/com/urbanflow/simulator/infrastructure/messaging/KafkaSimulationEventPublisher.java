package com.urbanflow.simulator.infrastructure.messaging;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.simulator.application.port.SimulationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaSimulationEventPublisher implements SimulationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaSimulationEventPublisher.class);

    private final KafkaTemplate<String, TrafficUpdateEvent> trafficKafkaTemplate;
    private final KafkaTemplate<String, SensorRawEvent> sensorKafkaTemplate;

    public KafkaSimulationEventPublisher(
            KafkaTemplate<String, TrafficUpdateEvent> trafficKafkaTemplate,
            KafkaTemplate<String, SensorRawEvent> sensorKafkaTemplate
    ) {
        this.trafficKafkaTemplate = trafficKafkaTemplate;
        this.sensorKafkaTemplate = sensorKafkaTemplate;
    }

    @Override
    public void publishTraffic(TrafficUpdateEvent event) {
        trafficKafkaTemplate.send(
                KafkaTopics.TRAFFIC_UPDATES,
                event.segmentId().toString(),
                event
        ).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish traffic event for segment {}", event.segmentId(), ex);
            }
        });
    }

    @Override
    public void publishSensor(SensorRawEvent event) {
        sensorKafkaTemplate.send(
                KafkaTopics.SENSOR_RAW,
                event.deviceId(),
                event
        ).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish sensor event for device {}", event.deviceId(), ex);
            }
        });
    }
}
