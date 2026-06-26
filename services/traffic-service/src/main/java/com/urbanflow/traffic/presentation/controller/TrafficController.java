package com.urbanflow.traffic.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.constant.HttpHeaderConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.traffic.application.service.TrafficApplicationService;
import com.urbanflow.traffic.presentation.dto.CreateTrafficSegmentRequest;
import com.urbanflow.traffic.presentation.dto.TrafficSegmentResponse;
import com.urbanflow.traffic.presentation.dto.UpdateTrafficSegmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/traffic/segments")
@Tag(name = "Traffic", description = "Road segment monitoring and congestion management")
public class TrafficController {

    private final TrafficApplicationService trafficApplicationService;

    public TrafficController(TrafficApplicationService trafficApplicationService) {
        this.trafficApplicationService = trafficApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a traffic segment")
    public ApiResponse<TrafficSegmentResponse> create(
            @Valid @RequestBody CreateTrafficSegmentRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                TrafficSegmentResponse.from(trafficApplicationService.createSegment(
                        request.name(),
                        request.zoneId(),
                        request.congestionLevel(),
                        request.averageSpeedKmh(),
                        request.vehicleCount(),
                        request.latitude(),
                        request.longitude(),
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Traffic segment created"
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a traffic segment by id")
    public ApiResponse<TrafficSegmentResponse> getById(@PathVariable UUID id) {
        return ApiResponse.ok(TrafficSegmentResponse.from(trafficApplicationService.getSegment(id)));
    }

    @GetMapping
    @Operation(summary = "List traffic segments with pagination")
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        TrafficApplicationService.PagedSegments result = trafficApplicationService.getAllSegments(page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(TrafficSegmentResponse::from).toList(),
                "page", meta
        ));
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "List traffic segments by zone")
    public ApiResponse<List<TrafficSegmentResponse>> getByZone(@PathVariable String zoneId) {
        List<TrafficSegmentResponse> segments = trafficApplicationService.getSegmentsByZone(zoneId).stream()
                .map(TrafficSegmentResponse::from)
                .toList();
        return ApiResponse.ok(segments);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update traffic segment metrics")
    public ApiResponse<TrafficSegmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTrafficSegmentRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                TrafficSegmentResponse.from(trafficApplicationService.updateSegmentMetrics(
                        id,
                        request.congestionLevel(),
                        request.averageSpeedKmh(),
                        request.vehicleCount(),
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Traffic segment updated"
        );
    }
}
