package com.urbanflow.websocket.infrastructure.redis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedisChannelsTest {

    @Test
    void shouldMapRedisChannelToStompTopic() {
        assertEquals("/topic/alerts", RedisChannels.stompTopicFromRedisChannel(RedisChannels.ALERTS));
        assertEquals(
                "/topic/zone/zone-casa-centre",
                RedisChannels.stompTopicFromRedisChannel(RedisChannels.zoneChannel("zone-casa-centre"))
        );
    }
}
