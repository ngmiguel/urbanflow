package com.urbanflow.environment.application.service;

import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.common.exception.ResourceNotFoundException;
import com.urbanflow.environment.application.dto.AirQualityIndexDto;
import com.urbanflow.environment.application.dto.SensorReadingDto;
import com.urbanflow.environment.application.port.EventIdempotencyStore;
import com.urbanflow.environment.domain.model.AirQualityIndex;
import com.urbanflow.environment.domain.model.EnvironmentSensorType;
import com.urbanflow.environment.domain.model.SensorReading;
import com.urbanflow.environment.domain.repository.SensorReadingRepository;
import com.urbanflow.events.sensor.SensorRawEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class EnvironmentApplicationService {

    private final SensorReadingRepository sensorReadingRepository;
    private final EventIdempotencyStore idempotencyStore;

    public EnvironmentApplicationService(
            SensorReadingRepository sensorReadingRepository,
            EventIdempotencyStore idempotencyStore
    ) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.idempotencyStore = idempotencyStore;
    }

    @Transactional
    public void ingestSensorEvent(SensorRawEvent event) {
        UUID eventId = event.metadata().eventId();
        if (idempotencyStore.alreadyProcessed(eventId)) {
            return;
        }
        if (!SensorReading.isEnvironmentSensor(event.sensorType())) {
            idempotencyStore.markProcessed(eventId);
            return;
        }

        sensorReadingRepository.save(SensorReading.fromTelemetry(
                event.deviceId(),
                event.sensorType(),
                event.value(),
                event.unit(),
                event.zoneId(),
                event.latitude(),
                event.longitude(),
                eventId.toString(),
                event.metadata().occurredAt()
        ));
        idempotencyStore.markProcessed(eventId);
    }

    @Transactional(readOnly = true)
    public SensorReadingDto getReading(UUID id) {
        return sensorReadingRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SensorReading", id));
    }

    @Transactional(readOnly = true)
    public List<SensorReadingDto> getReadingsByZone(String zoneId, int page, int size) {
        return sensorReadingRepository.findByZoneId(zoneId, page, size).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedReadings getAllReadings(int page, int size) {
        List<SensorReadingDto> readings = sensorReadingRepository.findAll(page, size).stream()
                .map(this::toDto)
                .toList();
        long total = sensorReadingRepository.count();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PagedReadings(readings, new PageMeta(page, size, total, totalPages));
    }

    @Transactional(readOnly = true)
    public AirQualityIndexDto getZoneAirQuality(String zoneId) {
        Instant since = Instant.now().minus(1, ChronoUnit.HOURS);
        List<SensorReading> readings = sensorReadingRepository.findByZoneIdSince(zoneId, since);
        if (readings.isEmpty()) {
            throw new ResourceNotFoundException("AirQualityIndex", zoneId);
        }

        Double pm25 = averageForType(readings, EnvironmentSensorType.AIR_QUALITY.name());
        Double noiseDb = averageForType(readings, EnvironmentSensorType.NOISE.name());
        Double temperature = averageForType(readings, EnvironmentSensorType.ENVIRONMENT.name());

        int aqiValue = pm25 != null ? AirQualityIndex.fromPm25(pm25) : 50;
        AirQualityIndex index = new AirQualityIndex(
                zoneId,
                aqiValue,
                AirQualityIndex.categorize(aqiValue),
                pm25,
                noiseDb,
                temperature,
                Instant.now()
        );
        return toDto(index);
    }

    private Double averageForType(List<SensorReading> readings, String sensorType) {
        var average = readings.stream()
                .filter(r -> sensorType.equalsIgnoreCase(r.getSensorType()))
                .mapToDouble(SensorReading::getValue)
                .average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    private SensorReadingDto toDto(SensorReading reading) {
        return new SensorReadingDto(
                reading.getId(),
                reading.getDeviceId(),
                reading.getSensorType(),
                reading.getValue(),
                reading.getUnit(),
                reading.getZoneId(),
                reading.getLatitude(),
                reading.getLongitude(),
                reading.getSourceEventId(),
                reading.getRecordedAt()
        );
    }

    private AirQualityIndexDto toDto(AirQualityIndex index) {
        return new AirQualityIndexDto(
                index.getZoneId(),
                index.getAqiValue(),
                index.getCategory(),
                index.getPm25(),
                index.getNoiseDb(),
                index.getTemperatureCelsius(),
                index.getCalculatedAt()
        );
    }

    public record PagedReadings(List<SensorReadingDto> content, PageMeta page) {
    }
}
