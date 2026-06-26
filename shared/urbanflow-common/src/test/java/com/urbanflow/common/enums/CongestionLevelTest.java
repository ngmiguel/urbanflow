package com.urbanflow.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CongestionLevelTest {

    @Test
    void shouldExposeOrderedCongestionLevels() {
        assertEquals(4, CongestionLevel.values().length);
        assertEquals(CongestionLevel.FREE_FLOW, CongestionLevel.valueOf("FREE_FLOW"));
        assertEquals(CongestionLevel.GRIDLOCK, CongestionLevel.valueOf("GRIDLOCK"));
    }
}
