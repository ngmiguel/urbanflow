package com.urbanflow.environment.application.dto;

import com.urbanflow.environment.domain.model.AqiCategory;

import java.time.Instant;

public record AirQualityIndexDto(
        String zoneId,
        int aqiValue,
        AqiCategory category,
        Double pm25,
        Double noiseDb,
        Double temperatureCelsius,
        Instant calculatedAt
) {
}
