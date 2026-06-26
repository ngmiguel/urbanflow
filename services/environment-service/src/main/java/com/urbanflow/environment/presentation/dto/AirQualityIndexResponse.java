package com.urbanflow.environment.presentation.dto;

import com.urbanflow.environment.application.dto.AirQualityIndexDto;
import com.urbanflow.environment.domain.model.AqiCategory;

import java.time.Instant;

public record AirQualityIndexResponse(
        String zoneId,
        int aqiValue,
        AqiCategory category,
        Double pm25,
        Double noiseDb,
        Double temperatureCelsius,
        Instant calculatedAt
) {

    public static AirQualityIndexResponse from(AirQualityIndexDto dto) {
        return new AirQualityIndexResponse(
                dto.zoneId(),
                dto.aqiValue(),
                dto.category(),
                dto.pm25(),
                dto.noiseDb(),
                dto.temperatureCelsius(),
                dto.calculatedAt()
        );
    }
}
