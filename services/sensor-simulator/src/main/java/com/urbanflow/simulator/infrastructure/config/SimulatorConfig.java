package com.urbanflow.simulator.infrastructure.config;

import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.sensor.SensorRawEvent;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Profile("!test")
public class SimulatorConfig {

    @Bean
    NewTopic trafficUpdatesTopic() {
        return new NewTopic(KafkaTopics.TRAFFIC_UPDATES, 12, (short) 1);
    }

    @Bean
    NewTopic sensorRawTopic() {
        return new NewTopic(KafkaTopics.SENSOR_RAW, 12, (short) 1);
    }

    @Bean
    ProducerFactory<String, TrafficUpdateEvent> trafficProducerFactory(KafkaProperties kafkaProperties) {
        return buildProducerFactory(kafkaProperties);
    }

    @Bean
    ProducerFactory<String, SensorRawEvent> sensorProducerFactory(KafkaProperties kafkaProperties) {
        return buildProducerFactory(kafkaProperties);
    }

    @Bean
    KafkaTemplate<String, TrafficUpdateEvent> trafficKafkaTemplate(
            ProducerFactory<String, TrafficUpdateEvent> trafficProducerFactory
    ) {
        return new KafkaTemplate<>(trafficProducerFactory);
    }

    @Bean
    KafkaTemplate<String, SensorRawEvent> sensorKafkaTemplate(
            ProducerFactory<String, SensorRawEvent> sensorProducerFactory
    ) {
        return new KafkaTemplate<>(sensorProducerFactory);
    }

    @Bean(destroyMethod = "shutdown")
    ScheduledExecutorService simulationScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("simulation-");
        scheduler.initialize();
        return scheduler.getScheduledExecutor();
    }

    private <T> ProducerFactory<String, T> buildProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildProducerProperties(null));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
}
