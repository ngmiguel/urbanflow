package com.urbanflow.twin.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.twin.application.service.DigitalTwinApplicationService;
import com.urbanflow.twin.presentation.dto.RunSimulationRequest;
import com.urbanflow.twin.presentation.dto.SimulationResponse;
import com.urbanflow.twin.presentation.dto.ZoneStateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/twin")
@Tag(name = "Digital Twin", description = "Zone twin state and what-if urban simulations")
public class DigitalTwinController {

    private final DigitalTwinApplicationService digitalTwinApplicationService;

    public DigitalTwinController(DigitalTwinApplicationService digitalTwinApplicationService) {
        this.digitalTwinApplicationService = digitalTwinApplicationService;
    }

    @GetMapping("/zones/{zoneId}/state")
    @Operation(summary = "Get current digital twin baseline for a zone")
    public ApiResponse<ZoneStateResponse> getZoneState(@PathVariable String zoneId) {
        return ApiResponse.ok(ZoneStateResponse.from(digitalTwinApplicationService.getZoneState(zoneId)));
    }

    @PostMapping("/simulations")
    @PreAuthorize("hasAnyRole('ANALYST', 'OPERATOR', 'ADMIN')")
    @Operation(summary = "Run a what-if simulation against the current zone baseline")
    public ApiResponse<SimulationResponse> runSimulation(@Valid @RequestBody RunSimulationRequest request) {
        return ApiResponse.ok(
                SimulationResponse.from(digitalTwinApplicationService.runWhatIfSimulation(
                        request.zoneId(),
                        request.scenarioType(),
                        request.incidentSeverity(),
                        request.closureDurationMinutes()
                )),
                "What-if simulation completed"
        );
    }

    @GetMapping("/simulations/{id}")
    @Operation(summary = "Get a stored what-if simulation result")
    public ApiResponse<SimulationResponse> getSimulation(@PathVariable UUID id) {
        return ApiResponse.ok(SimulationResponse.from(digitalTwinApplicationService.getSimulation(id)));
    }

    @GetMapping("/simulations")
    @Operation(summary = "List recent simulations for a zone")
    public ApiResponse<List<SimulationResponse>> listSimulations(
            @RequestParam String zoneId,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<SimulationResponse> simulations = digitalTwinApplicationService.listSimulations(zoneId, limit).stream()
                .map(SimulationResponse::from)
                .toList();
        return ApiResponse.ok(simulations);
    }
}
