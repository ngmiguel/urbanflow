package com.urbanflow.twin.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanflow.twin.application.port.TwinStateStore;
import com.urbanflow.twin.domain.model.TwinZoneState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("!test")
public class RedisTwinStateStore implements TwinStateStore {

    private static final Logger log = LoggerFactory.getLogger(RedisTwinStateStore.class);
    private static final String ZONE_KEY_PREFIX = "twin:zone:";
    private static final String PROJECTION_SUFFIX = ":projection";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisTwinStateStore(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<TwinZoneState> getZoneState(String zoneId) {
        return readState(ZONE_KEY_PREFIX + zoneId);
    }

    @Override
    public void saveZoneState(TwinZoneState state) {
        writeState(ZONE_KEY_PREFIX + state.zoneId(), state);
    }

    @Override
    public void saveProjectedState(String zoneId, TwinZoneState projectedState) {
        writeState(ZONE_KEY_PREFIX + zoneId + PROJECTION_SUFFIX, projectedState);
    }

    private Optional<TwinZoneState> readState(String key) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, TwinZoneState.class));
        } catch (JsonProcessingException exception) {
            log.warn("Failed to deserialize twin state for key {}", key, exception);
            return Optional.empty();
        }
    }

    private void writeState(String key, TwinZoneState state) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(state));
        } catch (JsonProcessingException exception) {
            log.error("Failed to serialize twin state for key {}", key, exception);
        }
    }
}
