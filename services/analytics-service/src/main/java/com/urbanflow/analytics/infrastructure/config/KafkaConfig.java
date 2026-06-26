package com.urbanflow.analytics.infrastructure.config;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("!test")
public class KafkaConfig {

    private static final String TRUSTED_PACKAGES = "com.urbanflow.events";

    @Bean
    NewTopic alertEventsTopic() {
        return new NewTopic(KafkaTopics.ALERT_EVENTS, 12, (short) 1);
    }

    @Bean
    NewTopic anomalyDetectedTopic() {
        return new NewTopic(KafkaTopics.ANOMALY_DETECTED, 12, (short) 1);
    }

    @Bean
    ConsumerFactory<String, TrafficUpdateEvent> trafficConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, TrafficUpdateEvent.class);
    }

    @Bean
    ConsumerFactory<String, AlertTriggeredEvent> alertConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, AlertTriggeredEvent.class);
    }

    @Bean
    ConsumerFactory<String, AnomalyDetectedEvent> anomalyConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, AnomalyDetectedEvent.class);
    }

    @Bean
    ConsumerFactory<String, IncidentEvent> incidentConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, IncidentEvent.class);
    }

    @Bean
    ConsumerFactory<String, SensorRawEvent> sensorConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, SensorRawEvent.class);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TrafficUpdateEvent> trafficKafkaListenerContainerFactory(
            ConsumerFactory<String, TrafficUpdateEvent> trafficConsumerFactory
    ) {
        return buildListenerFactory(trafficConsumerFactory);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AlertTriggeredEvent> alertKafkaListenerContainerFactory(
            ConsumerFactory<String, AlertTriggeredEvent> alertConsumerFactory
    ) {
        return buildListenerFactory(alertConsumerFactory);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AnomalyDetectedEvent> anomalyKafkaListenerContainerFactory(
            ConsumerFactory<String, AnomalyDetectedEvent> anomalyConsumerFactory
    ) {
        return buildListenerFactory(anomalyConsumerFactory);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, IncidentEvent> incidentKafkaListenerContainerFactory(
            ConsumerFactory<String, IncidentEvent> incidentConsumerFactory
    ) {
        return buildListenerFactory(incidentConsumerFactory);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, SensorRawEvent> sensorKafkaListenerContainerFactory(
            ConsumerFactory<String, SensorRawEvent> sensorConsumerFactory
    ) {
        return buildListenerFactory(sensorConsumerFactory);
    }

    private <T> ConsumerFactory<String, T> buildConsumerFactory(
            KafkaProperties kafkaProperties,
            Class<T> valueType
    ) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueType.getName());
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> buildListenerFactory(
            ConsumerFactory<String, T> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
