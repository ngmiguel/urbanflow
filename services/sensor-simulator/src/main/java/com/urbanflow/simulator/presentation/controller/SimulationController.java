package com.urbanflow.simulator.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.simulator.application.service.SimulationApplicationService;
import com.urbanflow.simulator.domain.model.SimulationScenario;
import com.urbanflow.simulator.presentation.dto.ScenarioResponse;
import com.urbanflow.simulator.presentation.dto.SimulationStatusResponse;
import com.urbanflow.simulator.presentation.dto.StartScenarioRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/simulator")
@Tag(name = "Simulator", description = "Demo scenario simulation for traffic and sensor events")
public class SimulationController {

    private final SimulationApplicationService simulationApplicationService;

    public SimulationController(SimulationApplicationService simulationApplicationService) {
        this.simulationApplicationService = simulationApplicationService;
    }

    @GetMapping("/scenarios")
    @Operation(summary = "List available demo scenarios")
    public ApiResponse<List<ScenarioResponse>> listScenarios() {
        List<ScenarioResponse> scenarios = simulationApplicationService.listScenarios().stream()
                .map(ScenarioResponse::from)
                .toList();
        return ApiResponse.ok(scenarios);
    }

    @GetMapping("/status")
    @Operation(summary = "Get current simulation status")
    public ApiResponse<SimulationStatusResponse> getStatus() {
        return ApiResponse.ok(SimulationStatusResponse.from(simulationApplicationService.getStatus()));
    }

    @PostMapping("/scenarios/{slug}/start")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @Operation(summary = "Start a named demo scenario")
    public ApiResponse<SimulationStatusResponse> startScenario(
            @PathVariable String slug,
            @Valid @RequestBody(required = false) StartScenarioRequest request
    ) {
        SimulationScenario scenario = SimulationScenario.fromSlug(slug);
        String zoneId = request != null ? request.zoneId() : null;
        Integer durationSeconds = request != null ? request.durationSeconds() : null;
        return ApiResponse.ok(
                SimulationStatusResponse.from(
                        simulationApplicationService.startScenario(scenario, zoneId, durationSeconds)
                ),
                "Scenario %s started".formatted(scenario.slug())
        );
    }

    @PostMapping("/stop")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @Operation(summary = "Stop the running simulation")
    public ApiResponse<SimulationStatusResponse> stopScenario() {
        return ApiResponse.ok(
                SimulationStatusResponse.from(simulationApplicationService.stopScenario()),
                "Simulation stopped"
        );
    }
}
