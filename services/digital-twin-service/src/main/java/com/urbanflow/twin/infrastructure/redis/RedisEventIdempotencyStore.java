package com.urbanflow.twin.infrastructure.redis;

import com.urbanflow.twin.application.port.EventIdempotencyStore;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@Profile("!test")
public class RedisEventIdempotencyStore implements EventIdempotencyStore {

    private static final String KEY_PREFIX = "idempotency:";
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;

    public RedisEventIdempotencyStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean alreadyProcessed(UUID eventId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + eventId));
    }

    @Override
    public void markProcessed(UUID eventId) {
        redisTemplate.opsForValue().set(KEY_PREFIX + eventId, "1", TTL);
    }
}
