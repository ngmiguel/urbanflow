package com.urbanflow.environment.domain.model;

import java.time.Instant;

public class AirQualityIndex {

    private String zoneId;
    private int aqiValue;
    private AqiCategory category;
    private Double pm25;
    private Double noiseDb;
    private Double temperatureCelsius;
    private Instant calculatedAt;

    public AirQualityIndex() {
    }

    public AirQualityIndex(
            String zoneId,
            int aqiValue,
            AqiCategory category,
            Double pm25,
            Double noiseDb,
            Double temperatureCelsius,
            Instant calculatedAt
    ) {
        this.zoneId = zoneId;
        this.aqiValue = aqiValue;
        this.category = category;
        this.pm25 = pm25;
        this.noiseDb = noiseDb;
        this.temperatureCelsius = temperatureCelsius;
        this.calculatedAt = calculatedAt;
    }

    public static AqiCategory categorize(int aqiValue) {
        if (aqiValue <= 50) {
            return AqiCategory.GOOD;
        }
        if (aqiValue <= 100) {
            return AqiCategory.MODERATE;
        }
        if (aqiValue <= 150) {
            return AqiCategory.UNHEALTHY_FOR_SENSITIVE;
        }
        if (aqiValue <= 200) {
            return AqiCategory.UNHEALTHY;
        }
        if (aqiValue <= 300) {
            return AqiCategory.VERY_UNHEALTHY;
        }
        return AqiCategory.HAZARDOUS;
    }

    public static int fromPm25(double pm25) {
        return (int) Math.round(Math.min(pm25 * 2.0, 500.0));
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public int getAqiValue() {
        return aqiValue;
    }

    public void setAqiValue(int aqiValue) {
        this.aqiValue = aqiValue;
    }

    public AqiCategory getCategory() {
        return category;
    }

    public void setCategory(AqiCategory category) {
        this.category = category;
    }

    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getNoiseDb() {
        return noiseDb;
    }

    public void setNoiseDb(Double noiseDb) {
        this.noiseDb = noiseDb;
    }

    public Double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(Double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public Instant getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Instant calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
