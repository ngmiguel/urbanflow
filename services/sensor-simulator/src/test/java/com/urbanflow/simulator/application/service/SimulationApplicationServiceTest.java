package com.urbanflow.simulator.application.service;

import com.urbanflow.common.exception.BusinessException;
import com.urbanflow.simulator.application.port.SimulationEventPublisher;
import com.urbanflow.simulator.domain.model.SimulationScenario;
import com.urbanflow.simulator.domain.model.SimulationStatus;
import com.urbanflow.simulator.infrastructure.config.SimulatorProperties;
import com.urbanflow.simulator.infrastructure.engine.PollutionSpikeScenarioRunner;
import com.urbanflow.simulator.infrastructure.engine.RushHourScenarioRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimulationApplicationServiceTest {

    private final SimulationEventPublisher publisher = new SimulationEventPublisher() {
        @Override
        public void publishTraffic(com.urbanflow.events.traffic.TrafficUpdateEvent event) {
        }

        @Override
        public void publishSensor(com.urbanflow.events.sensor.SensorRawEvent event) {
        }
    };

    private final SimulatorProperties properties = new SimulatorProperties(
            50,
            30,
            "zone-casa-centre",
            33.5731,
            -7.5898
    );

    private ScheduledExecutorService scheduler;
    private SimulationApplicationService service;

    @AfterEach
    void tearDown() {
        if (service != null) {
            service.stopScenario();
        }
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    @Test
    void shouldStartAndStopScenario() throws InterruptedException {
        service = createService();
        var state = service.startScenario(SimulationScenario.RUSH_HOUR, null, 10);

        assertEquals(SimulationStatus.RUNNING, state.status());
        assertEquals(SimulationScenario.RUSH_HOUR, state.scenario());

        TimeUnit.MILLISECONDS.sleep(120);

        var stopped = service.stopScenario();
        assertEquals(SimulationStatus.IDLE, stopped.status());
    }

    @Test
    void shouldRejectParallelScenarios() {
        service = createService();
        service.startScenario(SimulationScenario.POLLUTION_SPIKE, "zone-test", 60);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.startScenario(SimulationScenario.RUSH_HOUR, null, 60)
        );

        assertEquals("SIMULATION_ALREADY_RUNNING", exception.getErrorCode());
    }

    private SimulationApplicationService createService() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.initialize();
        scheduler = taskScheduler.getScheduledExecutor();

        return new SimulationApplicationService(
                properties,
                publisher,
                scheduler,
                List.of(new RushHourScenarioRunner(), new PollutionSpikeScenarioRunner())
        );
    }
}
