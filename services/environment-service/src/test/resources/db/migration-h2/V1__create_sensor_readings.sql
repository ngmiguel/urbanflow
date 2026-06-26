CREATE TABLE sensor_readings (
    id               UUID PRIMARY KEY,
    device_id        VARCHAR(100) NOT NULL,
    sensor_type      VARCHAR(50)  NOT NULL,
    reading_value        DOUBLE PRECISION NOT NULL,
    unit             VARCHAR(50)  NOT NULL,
    zone_id          VARCHAR(100) NOT NULL,
    latitude         DOUBLE PRECISION NOT NULL,
    longitude        DOUBLE PRECISION NOT NULL,
    source_event_id  VARCHAR(100),
    recorded_at      TIMESTAMP WITH TIME ZONE NOT NULL
);
