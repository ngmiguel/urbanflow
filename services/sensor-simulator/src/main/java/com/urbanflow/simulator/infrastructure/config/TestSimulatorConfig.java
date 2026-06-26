package com.urbanflow.simulator.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Profile("test")
public class TestSimulatorConfig {

    @Bean(destroyMethod = "shutdown")
    ScheduledExecutorService simulationScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("simulation-test-");
        scheduler.initialize();
        return scheduler.getScheduledExecutor();
    }
}
