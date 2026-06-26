package com.urbanflow.traffic.infrastructure.messaging;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.traffic.application.port.TrafficEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class KafkaTrafficEventPublisher implements TrafficEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaTrafficEventPublisher.class);

    private final KafkaTemplate<String, TrafficUpdateEvent> kafkaTemplate;

    public KafkaTrafficEventPublisher(KafkaTemplate<String, TrafficUpdateEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(TrafficUpdateEvent event) {
        String key = event.segmentId().toString();
        kafkaTemplate.send(KafkaTopics.TRAFFIC_UPDATES, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish traffic event for segment {}", key, ex);
                    } else {
                        log.info("Published traffic event {} to topic {}", event.metadata().eventType(), KafkaTopics.TRAFFIC_UPDATES);
                    }
                });
    }
}
