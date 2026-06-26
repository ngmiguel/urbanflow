package com.urbanflow.analytics.presentation.controller;

import com.urbanflow.analytics.application.service.AnalyticsQueryService;
import com.urbanflow.analytics.domain.model.MetricType;
import com.urbanflow.analytics.presentation.dto.MetricHistoryResponse;
import com.urbanflow.analytics.presentation.dto.ZoneKpiResponse;
import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/analytics")
@Tag(name = "Analytics", description = "Zone KPIs and metric history dashboards")
public class AnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;

    public AnalyticsController(AnalyticsQueryService analyticsQueryService) {
        this.analyticsQueryService = analyticsQueryService;
    }

    @GetMapping("/zones/{zoneId}/kpis")
    @Operation(summary = "Get aggregated KPIs for a zone")
    public ApiResponse<ZoneKpiResponse> getZoneKpis(@PathVariable String zoneId) {
        return ApiResponse.ok(ZoneKpiResponse.from(analyticsQueryService.getZoneKpis(zoneId)));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ANALYST', 'OPERATOR', 'ADMIN')")
    @Operation(summary = "Get network-wide analytics dashboard summary")
    public ApiResponse<Map<String, Object>> getDashboard() {
        Map<String, Object> summary = analyticsQueryService.getDashboardSummary();
        summary.put("zones", analyticsQueryService.getDashboard().stream().map(ZoneKpiResponse::from).toList());
        return ApiResponse.ok(summary);
    }

    @GetMapping("/zones/{zoneId}/history")
    @Operation(summary = "Get recent metric history for a zone")
    public ApiResponse<List<MetricHistoryResponse>> getMetricHistory(
            @PathVariable String zoneId,
            @RequestParam MetricType metricType,
            @RequestParam(defaultValue = "30") int limit
    ) {
        List<MetricHistoryResponse> history = analyticsQueryService
                .getMetricHistory(zoneId, metricType, limit).stream()
                .map(MetricHistoryResponse::from)
                .toList();
        return ApiResponse.ok(history);
    }
}
