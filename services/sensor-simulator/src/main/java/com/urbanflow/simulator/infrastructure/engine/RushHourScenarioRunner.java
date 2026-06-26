package com.urbanflow.simulator.infrastructure.engine;

import com.urbanflow.common.enums.CongestionLevel;
import com.urbanflow.events.EventMetadata;
import com.urbanflow.events.traffic.TrafficEventType;
import com.urbanflow.events.traffic.TrafficUpdateEvent;
import com.urbanflow.simulator.domain.model.SimulationScenario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RushHourScenarioRunner implements ScenarioRunner {

    private static final List<SegmentTemplate> SEGMENTS = List.of(
            new SegmentTemplate(UUID.fromString("11111111-1111-1111-1111-111111111101"), "Boulevard Zerktouni"),
            new SegmentTemplate(UUID.fromString("11111111-1111-1111-1111-111111111102"), "Avenue des FAR"),
            new SegmentTemplate(UUID.fromString("11111111-1111-1111-1111-111111111103"), "Corniche Ain Diab")
    );

    private static final CongestionLevel[] PROGRESSION = {
            CongestionLevel.FREE_FLOW,
            CongestionLevel.MODERATE,
            CongestionLevel.HEAVY,
            CongestionLevel.GRIDLOCK
    };

    @Override
    public SimulationScenario scenario() {
        return SimulationScenario.RUSH_HOUR;
    }

    @Override
    public int tick(SimulationTickContext context) {
        int published = 0;
        for (SegmentTemplate segment : SEGMENTS) {
            CongestionLevel congestion = PROGRESSION[(context.tickCount() + published) % PROGRESSION.length];
            double averageSpeed = switch (congestion) {
                case FREE_FLOW -> 55.0;
                case MODERATE -> 35.0;
                case HEAVY -> 18.0;
                case GRIDLOCK -> 8.0;
            };
            int vehicleCount = switch (congestion) {
                case FREE_FLOW -> 120;
                case MODERATE -> 280;
                case HEAVY -> 520;
                case GRIDLOCK -> 900;
            };

            TrafficUpdateEvent event = new TrafficUpdateEvent(
                    EventMetadata.create("TRAFFIC_CONGESTION_CHANGED", "sensor-simulator", context.correlationId()),
                    segment.id(),
                    segment.name(),
                    TrafficEventType.CONGESTION_CHANGED,
                    congestion,
                    averageSpeed,
                    vehicleCount,
                    context.zoneId()
            );
            context.publisher().publishTraffic(event);
            published++;
        }
        return published;
    }

    private record SegmentTemplate(UUID id, String name) {
    }
}
