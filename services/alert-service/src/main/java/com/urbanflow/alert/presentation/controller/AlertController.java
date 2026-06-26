package com.urbanflow.alert.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.alert.application.service.AlertApplicationService;
import com.urbanflow.alert.domain.model.AlertStatus;
import com.urbanflow.alert.presentation.dto.AlertResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/alerts")
@Tag(name = "Alerts", description = "Triggered alerts and lifecycle management")
public class AlertController {

    private final AlertApplicationService alertApplicationService;

    public AlertController(AlertApplicationService alertApplicationService) {
        this.alertApplicationService = alertApplicationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert by id")
    public ApiResponse<AlertResponse> getById(@PathVariable UUID id) {
        return ApiResponse.ok(AlertResponse.from(alertApplicationService.getAlert(id)));
    }

    @GetMapping
    @Operation(summary = "List alerts with pagination")
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        AlertApplicationService.PagedAlerts result = alertApplicationService.getAllAlerts(page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(AlertResponse::from).toList(),
                "page", meta
        ));
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "List alerts by zone")
    public ApiResponse<List<AlertResponse>> getByZone(@PathVariable String zoneId) {
        List<AlertResponse> alerts = alertApplicationService.getAlertsByZone(zoneId).stream()
                .map(AlertResponse::from)
                .toList();
        return ApiResponse.ok(alerts);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "List alerts by status")
    public ApiResponse<List<AlertResponse>> getByStatus(@PathVariable AlertStatus status) {
        List<AlertResponse> alerts = alertApplicationService.getAlertsByStatus(status).stream()
                .map(AlertResponse::from)
                .toList();
        return ApiResponse.ok(alerts);
    }

    @PutMapping("/{id}/acknowledge")
    @Operation(summary = "Acknowledge an alert")
    public ApiResponse<AlertResponse> acknowledge(@PathVariable UUID id) {
        return ApiResponse.ok(
                AlertResponse.from(alertApplicationService.acknowledgeAlert(id)),
                "Alert acknowledged"
        );
    }

    @PutMapping("/{id}/dismiss")
    @Operation(summary = "Dismiss an alert")
    public ApiResponse<AlertResponse> dismiss(@PathVariable UUID id) {
        return ApiResponse.ok(
                AlertResponse.from(alertApplicationService.dismissAlert(id)),
                "Alert dismissed"
        );
    }
}
