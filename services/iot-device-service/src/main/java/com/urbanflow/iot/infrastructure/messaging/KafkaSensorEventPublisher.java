package com.urbanflow.iot.infrastructure.messaging;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.iot.application.port.SensorEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaSensorEventPublisher implements SensorEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaSensorEventPublisher.class);

    private final KafkaTemplate<String, SensorRawEvent> kafkaTemplate;

    public KafkaSensorEventPublisher(KafkaTemplate<String, SensorRawEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(SensorRawEvent event) {
        kafkaTemplate.send(KafkaTopics.SENSOR_RAW, event.deviceId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish sensor event for device {}", event.deviceId(), ex);
                    } else {
                        log.info("Published sensor event for device {} to {}", event.deviceId(), KafkaTopics.SENSOR_RAW);
                    }
                });
    }
}
