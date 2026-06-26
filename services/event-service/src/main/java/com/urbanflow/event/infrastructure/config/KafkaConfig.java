package com.urbanflow.event.infrastructure.config;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.urban.UrbanPlannedEvent;
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

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("!test")
public class KafkaConfig {

    @Bean
    NewTopic urbanEventsTopic() {
        return new NewTopic(KafkaTopics.URBAN_EVENTS, 12, (short) 1);
    }

    @Bean
    ProducerFactory<String, UrbanPlannedEvent> urbanEventProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    KafkaTemplate<String, UrbanPlannedEvent> urbanEventKafkaTemplate(
            ProducerFactory<String, UrbanPlannedEvent> urbanEventProducerFactory
    ) {
        return new KafkaTemplate<>(urbanEventProducerFactory);
    }
}
