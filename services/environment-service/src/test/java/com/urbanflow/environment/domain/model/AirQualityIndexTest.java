package com.urbanflow.environment.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AirQualityIndexTest {

    @Test
    void shouldCategorizeAqiLevels() {
        assertEquals(AqiCategory.GOOD, AirQualityIndex.categorize(42));
        assertEquals(AqiCategory.MODERATE, AirQualityIndex.categorize(75));
        assertEquals(AqiCategory.UNHEALTHY, AirQualityIndex.categorize(175));
    }

    @Test
    void shouldComputeAqiFromPm25() {
        assertEquals(90, AirQualityIndex.fromPm25(45.0));
    }
}
