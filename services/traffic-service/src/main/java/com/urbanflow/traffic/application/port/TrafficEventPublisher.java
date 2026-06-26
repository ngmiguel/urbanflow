package com.urbanflow.traffic.application.port;

import com.urbanflow.events.traffic.TrafficUpdateEvent;

public interface TrafficEventPublisher {

    void publish(TrafficUpdateEvent event);
}
