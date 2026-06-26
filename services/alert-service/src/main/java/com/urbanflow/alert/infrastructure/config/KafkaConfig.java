package com.urbanflow.alert.infrastructure.config;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.alert.AnomalyDetectedEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

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
    ProducerFactory<String, AlertTriggeredEvent> alertProducerFactory(KafkaProperties kafkaProperties) {
        return buildProducerFactory(kafkaProperties);
    }

    @Bean
    KafkaTemplate<String, AlertTriggeredEvent> alertKafkaTemplate(
            ProducerFactory<String, AlertTriggeredEvent> alertProducerFactory
    ) {
        return new KafkaTemplate<>(alertProducerFactory);
    }

    @Bean
    ProducerFactory<String, AnomalyDetectedEvent> anomalyProducerFactory(KafkaProperties kafkaProperties) {
        return buildProducerFactory(kafkaProperties);
    }

    @Bean
    KafkaTemplate<String, AnomalyDetectedEvent> anomalyKafkaTemplate(
            ProducerFactory<String, AnomalyDetectedEvent> anomalyProducerFactory
    ) {
        return new KafkaTemplate<>(anomalyProducerFactory);
    }

    @Bean
    ConsumerFactory<String, IncidentEvent> incidentConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, IncidentEvent.class);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, IncidentEvent> incidentKafkaListenerContainerFactory(
            ConsumerFactory<String, IncidentEvent> incidentConsumerFactory
    ) {
        return buildListenerFactory(incidentConsumerFactory);
    }

    @Bean
    ConsumerFactory<String, TrafficUpdateEvent> trafficConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, TrafficUpdateEvent.class);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TrafficUpdateEvent> trafficKafkaListenerContainerFactory(
            ConsumerFactory<String, TrafficUpdateEvent> trafficConsumerFactory
    ) {
        return buildListenerFactory(trafficConsumerFactory);
    }

    @Bean
    ConsumerFactory<String, SensorRawEvent> sensorConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, SensorRawEvent.class);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, SensorRawEvent> sensorKafkaListenerContainerFactory(
            ConsumerFactory<String, SensorRawEvent> sensorConsumerFactory
    ) {
        return buildListenerFactory(sensorConsumerFactory);
    }

    private <T> ProducerFactory<String, T> buildProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
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
