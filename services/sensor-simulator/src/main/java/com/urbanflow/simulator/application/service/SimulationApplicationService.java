package com.urbanflow.simulator.application.service;

import com.urbanflow.common.exception.BusinessException;
import com.urbanflow.common.util.IdGenerator;
import com.urbanflow.simulator.application.port.SimulationEventPublisher;
import com.urbanflow.simulator.domain.model.SimulationScenario;
import com.urbanflow.simulator.domain.model.SimulationState;
import com.urbanflow.simulator.domain.model.SimulationStatus;
import com.urbanflow.simulator.infrastructure.config.SimulatorProperties;
import com.urbanflow.simulator.infrastructure.engine.ScenarioRunner;
import com.urbanflow.simulator.infrastructure.engine.SimulationTickContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SimulationApplicationService {

    private static final Logger log = LoggerFactory.getLogger(SimulationApplicationService.class);

    private final SimulatorProperties properties;
    private final SimulationEventPublisher eventPublisher;
    private final ScheduledExecutorService scheduler;
    private final Map<SimulationScenario, ScenarioRunner> runners;

    private volatile SimulationState state = SimulationState.idle();
    private volatile ScheduledFuture<?> activeTask;

    public SimulationApplicationService(
            SimulatorProperties properties,
            SimulationEventPublisher eventPublisher,
            ScheduledExecutorService scheduler,
            List<ScenarioRunner> scenarioRunners
    ) {
        this.properties = properties;
        this.eventPublisher = eventPublisher;
        this.scheduler = scheduler;
        this.runners = scenarioRunners.stream()
                .collect(Collectors.toMap(ScenarioRunner::scenario, Function.identity()));
    }

    public List<SimulationScenario> listScenarios() {
        return Arrays.asList(SimulationScenario.values());
    }

    public SimulationState getStatus() {
        return state;
    }

    public synchronized SimulationState startScenario(
            SimulationScenario scenario,
            String zoneId,
            Integer durationSeconds
    ) {
        if (state.status() == SimulationStatus.RUNNING) {
            throw new BusinessException(
                    "SIMULATION_ALREADY_RUNNING",
                    "Scenario %s is already running".formatted(state.scenario().slug())
            );
        }

        String resolvedZoneId = zoneId != null && !zoneId.isBlank()
                ? zoneId
                : properties.defaultZoneId();
        int resolvedDuration = durationSeconds != null && durationSeconds > 0
                ? durationSeconds
                : properties.defaultDurationSeconds();
        String correlationId = IdGenerator.newId().toString();
        Instant startedAt = Instant.now();
        Instant endsAt = startedAt.plusSeconds(resolvedDuration);
        AtomicInteger eventsPublished = new AtomicInteger();

        ScenarioRunner runner = runners.get(scenario);
        state = new SimulationState(
                SimulationStatus.RUNNING,
                scenario,
                resolvedZoneId,
                correlationId,
                0,
                startedAt,
                endsAt
        );

        activeTask = scheduler.scheduleAtFixedRate(
                () -> tick(runner, resolvedZoneId, correlationId, eventsPublished, endsAt),
                0,
                properties.tickIntervalMs(),
                TimeUnit.MILLISECONDS
        );

        log.info("Started scenario {} for zone {} (duration={}s)", scenario.slug(), resolvedZoneId, resolvedDuration);
        return state;
    }

    public synchronized SimulationState stopScenario() {
        cancelActiveTask();
        state = SimulationState.idle();
        log.info("Simulation stopped");
        return state;
    }

    private void tick(
            ScenarioRunner runner,
            String zoneId,
            String correlationId,
            AtomicInteger eventsPublished,
            Instant endsAt
    ) {
        if (Instant.now().isAfter(endsAt)) {
            stopScenario();
            return;
        }

        int tickCount = eventsPublished.get();
        SimulationTickContext context = new SimulationTickContext(
                zoneId,
                correlationId,
                tickCount,
                eventPublisher,
                properties
        );
        int published = runner.tick(context);
        eventsPublished.addAndGet(published);
        state = new SimulationState(
                SimulationStatus.RUNNING,
                state.scenario(),
                state.zoneId(),
                state.correlationId(),
                eventsPublished.get(),
                state.startedAt(),
                state.endsAt()
        );
    }

    private void cancelActiveTask() {
        if (activeTask != null) {
            activeTask.cancel(false);
            activeTask = null;
        }
    }
}
