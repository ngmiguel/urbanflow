package com.urbanflow.alert.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.alert.application.service.AlertRuleApplicationService;
import com.urbanflow.alert.presentation.dto.AlertRuleResponse;
import com.urbanflow.alert.presentation.dto.CreateAlertRuleRequest;
import com.urbanflow.alert.presentation.dto.UpdateAlertRuleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/alerts/rules")
@Tag(name = "Alert Rules", description = "Alert rule configuration for multi-source correlation")
public class AlertRuleController {

    private final AlertRuleApplicationService alertRuleApplicationService;

    public AlertRuleController(AlertRuleApplicationService alertRuleApplicationService) {
        this.alertRuleApplicationService = alertRuleApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an alert rule")
    public ApiResponse<AlertRuleResponse> create(@Valid @RequestBody CreateAlertRuleRequest request) {
        return ApiResponse.ok(
                AlertRuleResponse.from(alertRuleApplicationService.createRule(
                        request.name(),
                        request.sourceType(),
                        request.zoneId(),
                        request.outputSeverity(),
                        request.incidentMinSeverity(),
                        request.minCongestionLevel(),
                        request.sensorType(),
                        request.sensorThreshold()
                )),
                "Alert rule created"
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert rule by id")
    public ApiResponse<AlertRuleResponse> getById(@PathVariable UUID id) {
        return ApiResponse.ok(AlertRuleResponse.from(alertRuleApplicationService.getRule(id)));
    }

    @GetMapping
    @Operation(summary = "List alert rules with pagination")
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        AlertRuleApplicationService.PagedRules result = alertRuleApplicationService.getAllRules(page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(AlertRuleResponse::from).toList(),
                "page", meta
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an alert rule")
    public ApiResponse<AlertRuleResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAlertRuleRequest request
    ) {
        return ApiResponse.ok(
                AlertRuleResponse.from(alertRuleApplicationService.updateRule(
                        id,
                        request.name(),
                        request.zoneId(),
                        request.outputSeverity(),
                        request.enabled(),
                        request.incidentMinSeverity(),
                        request.minCongestionLevel(),
                        request.sensorType(),
                        request.sensorThreshold()
                )),
                "Alert rule updated"
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an alert rule")
    public void delete(@PathVariable UUID id) {
        alertRuleApplicationService.deleteRule(id);
    }
}
