package com.urbanflow.twin.infrastructure.config;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.apache.kafka.clients.admin.NewTopic;
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

    @Bean
    NewTopic trafficUpdatesTopic() {
        return new NewTopic(KafkaTopics.TRAFFIC_UPDATES, 12, (short) 1);
    }

    @Bean
    NewTopic incidentEventsTopic() {
        return new NewTopic(KafkaTopics.INCIDENT_EVENTS, 12, (short) 1);
    }

    @Bean
    ConsumerFactory<String, TrafficUpdateEvent> trafficConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, TrafficUpdateEvent.class);
    }

    @Bean
    ConsumerFactory<String, IncidentEvent> incidentConsumerFactory(KafkaProperties kafkaProperties) {
        return buildConsumerFactory(kafkaProperties, IncidentEvent.class);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TrafficUpdateEvent> trafficKafkaListenerContainerFactory(
            ConsumerFactory<String, TrafficUpdateEvent> trafficConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, TrafficUpdateEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(trafficConsumerFactory);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, IncidentEvent> incidentKafkaListenerContainerFactory(
            ConsumerFactory<String, IncidentEvent> incidentConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, IncidentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(incidentConsumerFactory);
        return factory;
    }

    private <T> ConsumerFactory<String, T> buildConsumerFactory(
            KafkaProperties kafkaProperties,
            Class<T> valueType
    ) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties(null));
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(valueType);
        deserializer.addTrustedPackages("com.urbanflow.events");
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(config, new org.apache.kafka.common.serialization.StringDeserializer(), deserializer);
    }
}
