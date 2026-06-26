package com.urbanflow.websocket.infrastructure.config;

import com.urbanflow.websocket.infrastructure.redis.RedisChannels;
import com.urbanflow.websocket.infrastructure.redis.RedisNotificationRelayListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
@Profile("!test")
public class RedisListenerConfig {

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            RedisNotificationRelayListener relayListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(relayListener, new ChannelTopic(RedisChannels.ALERTS));
        container.addMessageListener(relayListener, new PatternTopic(RedisChannels.ZONE_PREFIX + "*"));
        return container;
    }
}
