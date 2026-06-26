package com.urbanflow.notification.infrastructure.config;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.alert.AlertTriggeredEvent;
import com.urbanflow.events.incident.IncidentEvent;
import com.urbanflow.events.notification.NotificationOutboxEvent;
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

    @Bean
    NewTopic notificationOutboxTopic() {
        return new NewTopic(KafkaTopics.NOTIFICATION_OUTBOX, 12, (short) 1);
    }

    @Bean
    ProducerFactory<String, NotificationOutboxEvent> notificationOutboxProducerFactory(KafkaProperties props) {
        Map<String, Object> config = new HashMap<>(props.buildProducerProperties(null));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    KafkaTemplate<String, NotificationOutboxEvent> notificationOutboxKafkaTemplate(
            ProducerFactory<String, NotificationOutboxEvent> notificationOutboxProducerFactory
    ) {
        return new KafkaTemplate<>(notificationOutboxProducerFactory);
    }

    @Bean
    ConsumerFactory<String, AlertTriggeredEvent> alertConsumerFactory(KafkaProperties props) {
        return buildConsumerFactory(props, AlertTriggeredEvent.class);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AlertTriggeredEvent> alertKafkaListenerContainerFactory(
            ConsumerFactory<String, AlertTriggeredEvent> alertConsumerFactory
    ) {
        return buildListenerFactory(alertConsumerFactory);
    }

    @Bean
    ConsumerFactory<String, IncidentEvent> incidentConsumerFactory(KafkaProperties props) {
        return buildConsumerFactory(props, IncidentEvent.class);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, IncidentEvent> incidentKafkaListenerContainerFactory(
            ConsumerFactory<String, IncidentEvent> incidentConsumerFactory
    ) {
        return buildListenerFactory(incidentConsumerFactory);
    }

    private <T> ConsumerFactory<String, T> buildConsumerFactory(KafkaProperties props, Class<T> type) {
        Map<String, Object> config = new HashMap<>(props.buildConsumerProperties(null));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.urbanflow.events");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, type.getName());
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> buildListenerFactory(
            ConsumerFactory<String, T> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
