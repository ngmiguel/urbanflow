package com.urbanflow.common.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IdGeneratorTest {

    @Test
    void shouldGenerateUuid() {
        UUID id = IdGenerator.newId();

        assertNotNull(id);
    }

    @Test
    void shouldGenerateCorrelationId() {
        assertDoesNotThrow(() -> {
            String correlationId = IdGenerator.newCorrelationId();
            assertNotNull(correlationId);
        });
    }
}
