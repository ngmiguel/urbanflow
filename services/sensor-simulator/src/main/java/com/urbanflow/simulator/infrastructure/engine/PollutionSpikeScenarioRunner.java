package com.urbanflow.simulator.infrastructure.engine;

import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.sensor.SensorRawEvent;
import com.urbanflow.simulator.domain.model.SimulationScenario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PollutionSpikeScenarioRunner implements ScenarioRunner {

    private static final List<String> DEVICE_IDS = List.of("SIM-ENV-001", "SIM-ENV-002", "SIM-ENV-003");

    @Override
    public SimulationScenario scenario() {
        return SimulationScenario.POLLUTION_SPIKE;
    }

    @Override
    public int tick(SimulationTickContext context) {
        double baseValue = 40.0 + (context.tickCount() * 18.0);
        int published = 0;

        for (String deviceId : DEVICE_IDS) {
            double value = Math.min(baseValue + (published * 5.0), 260.0);
            SensorRawEvent event = new SensorRawEvent(
                    EventMetadata.create("SENSOR_READING", "sensor-simulator", context.correlationId()),
                    deviceId,
                    "AIR_QUALITY",
                    value,
                    "ug/m3",
                    context.properties().defaultLatitude(),
                    context.properties().defaultLongitude(),
                    context.zoneId()
            );
            context.publisher().publishSensor(event);
            published++;
        }
        return published;
    }
}
