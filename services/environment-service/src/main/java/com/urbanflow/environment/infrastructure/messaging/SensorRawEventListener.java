package com.urbanflow.environment.infrastructure.messaging;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.environment.application.service.EnvironmentApplicationService;
import com.urbanflow.events.sensor.SensorRawEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class SensorRawEventListener {

    private static final Logger log = LoggerFactory.getLogger(SensorRawEventListener.class);

    private final EnvironmentApplicationService environmentApplicationService;

    public SensorRawEventListener(EnvironmentApplicationService environmentApplicationService) {
        this.environmentApplicationService = environmentApplicationService;
    }

    @KafkaListener(
            topics = KafkaTopics.SENSOR_RAW,
            groupId = "environment-service-sensors",
            containerFactory = "sensorKafkaListenerContainerFactory"
    )
    public void onSensorRawEvent(SensorRawEvent event) {
        log.debug("Received sensor event from device {}", event.deviceId());
        environmentApplicationService.ingestSensorEvent(event);
    }
}
