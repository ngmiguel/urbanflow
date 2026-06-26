package com.urbanflow.incident.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.constant.HttpHeaderConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.incident.application.service.IncidentApplicationService;
import com.urbanflow.incident.domain.model.IncidentStatus;
import com.urbanflow.incident.presentation.dto.IncidentResponse;
import com.urbanflow.incident.presentation.dto.ReportIncidentRequest;
import com.urbanflow.incident.presentation.dto.UpdateIncidentRequest;
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
@RequestMapping(ApiConstants.API_BASE_PATH + "/incidents")
@Tag(name = "Incidents", description = "Road incident reporting and lifecycle management")
public class IncidentController {

    private final IncidentApplicationService incidentApplicationService;

    public IncidentController(IncidentApplicationService incidentApplicationService) {
        this.incidentApplicationService = incidentApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Report a new road incident")
    public ApiResponse<IncidentResponse> report(
            @Valid @RequestBody ReportIncidentRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                IncidentResponse.from(incidentApplicationService.reportIncident(
                        request.type(),
                        request.severity(),
                        request.description(),
                        request.zoneId(),
                        request.latitude(),
                        request.longitude(),
                        request.reportedBy(),
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Incident reported"
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get incident by id")
    public ApiResponse<IncidentResponse> getById(@PathVariable UUID id) {
        return ApiResponse.ok(IncidentResponse.from(incidentApplicationService.getIncident(id)));
    }

    @GetMapping
    @Operation(summary = "List incidents with pagination")
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        IncidentApplicationService.PagedIncidents result = incidentApplicationService.getAllIncidents(page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(IncidentResponse::from).toList(),
                "page", meta
        ));
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "List incidents by zone")
    public ApiResponse<List<IncidentResponse>> getByZone(@PathVariable String zoneId) {
        List<IncidentResponse> incidents = incidentApplicationService.getIncidentsByZone(zoneId).stream()
                .map(IncidentResponse::from)
                .toList();
        return ApiResponse.ok(incidents);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "List incidents by status")
    public ApiResponse<List<IncidentResponse>> getByStatus(@PathVariable IncidentStatus status) {
        List<IncidentResponse> incidents = incidentApplicationService.getIncidentsByStatus(status).stream()
                .map(IncidentResponse::from)
                .toList();
        return ApiResponse.ok(incidents);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an incident")
    public ApiResponse<IncidentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIncidentRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                IncidentResponse.from(incidentApplicationService.updateIncident(
                        id,
                        request.severity(),
                        request.description(),
                        request.status(),
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Incident updated"
        );
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve an incident")
    public ApiResponse<IncidentResponse> resolve(
            @PathVariable UUID id,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                IncidentResponse.from(incidentApplicationService.resolveIncident(
                        id,
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Incident resolved"
        );
    }
}
