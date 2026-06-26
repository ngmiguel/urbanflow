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

CREATE INDEX idx_sensor_readings_zone_id ON sensor_readings (zone_id);
CREATE INDEX idx_sensor_readings_recorded_at ON sensor_readings (recorded_at DESC);
CREATE INDEX idx_sensor_readings_zone_recorded ON sensor_readings (zone_id, recorded_at DESC);
CREATE INDEX idx_sensor_readings_sensor_type ON sensor_readings (sensor_type);
