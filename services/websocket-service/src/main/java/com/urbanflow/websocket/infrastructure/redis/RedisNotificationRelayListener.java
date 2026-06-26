package com.urbanflow.websocket.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanflow.websocket.application.dto.RealtimeNotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class RedisNotificationRelayListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(RedisNotificationRelayListener.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisNotificationRelayListener(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            RealtimeNotificationMessage payload = objectMapper.readValue(
                    message.getBody(),
                    RealtimeNotificationMessage.class
            );
            String stompTopic = RedisChannels.stompTopicFromRedisChannel(channel);
            messagingTemplate.convertAndSend(stompTopic, payload);
            log.debug("Pushed notification to STOMP topic {}", stompTopic);
        } catch (Exception ex) {
            log.error("Failed to relay Redis message to STOMP", ex);
        }
    }
}
