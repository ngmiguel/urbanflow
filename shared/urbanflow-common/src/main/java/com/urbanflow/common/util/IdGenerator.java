package com.urbanflow.common.util;

import java.util.UUID;

/**
 * Utility methods for generating identifiers.
 */
public final class IdGenerator {

    private IdGenerator() {
    }

    public static UUID newId() {
        return UUID.randomUUID();
    }

    public static String newCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
