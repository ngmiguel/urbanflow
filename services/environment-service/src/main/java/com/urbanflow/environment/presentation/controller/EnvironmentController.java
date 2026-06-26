package com.urbanflow.environment.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.environment.application.service.EnvironmentApplicationService;
import com.urbanflow.environment.presentation.dto.AirQualityIndexResponse;
import com.urbanflow.environment.presentation.dto.SensorReadingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/environment")
@Tag(name = "Environment", description = "Environmental sensor readings and air quality indices")
public class EnvironmentController {

    private final EnvironmentApplicationService environmentApplicationService;

    public EnvironmentController(EnvironmentApplicationService environmentApplicationService) {
        this.environmentApplicationService = environmentApplicationService;
    }

    @GetMapping("/readings/{id}")
    @Operation(summary = "Get sensor reading by id")
    public ApiResponse<SensorReadingResponse> getReading(@PathVariable UUID id) {
        return ApiResponse.ok(SensorReadingResponse.from(environmentApplicationService.getReading(id)));
    }

    @GetMapping("/readings")
    @Operation(summary = "List sensor readings with pagination")
    public ApiResponse<Map<String, Object>> listReadings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        EnvironmentApplicationService.PagedReadings result =
                environmentApplicationService.getAllReadings(page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(SensorReadingResponse::from).toList(),
                "page", meta
        ));
    }

    @GetMapping("/readings/zone/{zoneId}")
    @Operation(summary = "List sensor readings by zone")
    public ApiResponse<List<SensorReadingResponse>> getReadingsByZone(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<SensorReadingResponse> readings = environmentApplicationService
                .getReadingsByZone(zoneId, page, size).stream()
                .map(SensorReadingResponse::from)
                .toList();
        return ApiResponse.ok(readings);
    }

    @GetMapping("/zones/{zoneId}/aqi")
    @Operation(summary = "Get air quality index for a zone (last hour)")
    public ApiResponse<AirQualityIndexResponse> getZoneAqi(@PathVariable String zoneId) {
        return ApiResponse.ok(
                AirQualityIndexResponse.from(environmentApplicationService.getZoneAirQuality(zoneId))
        );
    }
}
