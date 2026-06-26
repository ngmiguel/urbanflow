package com.urbanflow.simulator;

import com.urbanflow.simulator.infrastructure.config.SimulatorProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SimulatorProperties.class)
public class SensorSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorSimulatorApplication.class, args);
    }
}
