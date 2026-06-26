CREATE TABLE traffic_segments (
    id                  UUID PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    zone_id             VARCHAR(100) NOT NULL,
    congestion_level    VARCHAR(50)  NOT NULL,
    average_speed_kmh   DOUBLE PRECISION NOT NULL,
    vehicle_count       INTEGER NOT NULL,
    latitude            DOUBLE PRECISION NOT NULL,
    longitude           DOUBLE PRECISION NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_traffic_segments_zone_id ON traffic_segments (zone_id);
CREATE INDEX idx_traffic_segments_congestion ON traffic_segments (congestion_level);
