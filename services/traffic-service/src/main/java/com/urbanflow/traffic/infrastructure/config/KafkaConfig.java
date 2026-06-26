package com.urbanflow.traffic.infrastructure.config;

import com.urbanflow.events.traffic.TrafficUpdateEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.urbanflow.common.constant.KafkaTopics;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("!test")
public class KafkaConfig {

    @Bean
    NewTopic trafficUpdatesTopic() {
        return new NewTopic(KafkaTopics.TRAFFIC_UPDATES, 6, (short) 1);
    }

    @Bean
    ProducerFactory<String, TrafficUpdateEvent> trafficProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    KafkaTemplate<String, TrafficUpdateEvent> trafficKafkaTemplate(
            ProducerFactory<String, TrafficUpdateEvent> trafficProducerFactory
    ) {
        return new KafkaTemplate<>(trafficProducerFactory);
    }
}
