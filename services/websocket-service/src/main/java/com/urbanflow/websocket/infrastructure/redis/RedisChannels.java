package com.urbanflow.websocket.infrastructure.redis;

public final class RedisChannels {

    public static final String ALERTS = "channel:alerts";
    public static final String ZONE_PREFIX = "channel:zone:";

    private RedisChannels() {
    }

    public static String zoneChannel(String zoneId) {
        return ZONE_PREFIX + zoneId;
    }

    public static String stompTopicFromRedisChannel(String channel) {
        if (ALERTS.equals(channel)) {
            return "/topic/alerts";
        }
        if (channel.startsWith(ZONE_PREFIX)) {
            return "/topic/zone/" + channel.substring(ZONE_PREFIX.length());
        }
        return "/topic/alerts";
    }
}
