package com.urbanflow.websocket.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanflow.common.constant.KafkaTopics;
import com.urbanflow.events.notification.NotificationOutboxEvent;
import com.urbanflow.websocket.application.dto.RealtimeNotificationMessage;
import com.urbanflow.websocket.infrastructure.redis.RedisChannels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class NotificationOutboxListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationOutboxListener.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public NotificationOutboxListener(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = KafkaTopics.NOTIFICATION_OUTBOX,
            groupId = "websocket-service-notifications",
            containerFactory = "notificationOutboxKafkaListenerContainerFactory"
    )
    public void onNotificationOutbox(NotificationOutboxEvent event) {
        RealtimeNotificationMessage message = RealtimeNotificationMessage.from(event);
        try {
            String payload = objectMapper.writeValueAsString(message);
            String redisChannel = event.stompTopic().equals("/topic/alerts")
                    ? RedisChannels.ALERTS
                    : RedisChannels.zoneChannel(event.zoneId());
            redisTemplate.convertAndSend(redisChannel, payload);
            log.debug("Relayed notification {} to Redis channel {}", event.notificationId(), redisChannel);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize notification outbox event {}", event.notificationId(), ex);
        }
    }
}
