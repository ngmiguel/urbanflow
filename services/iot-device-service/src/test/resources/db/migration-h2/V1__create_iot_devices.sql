CREATE TABLE iot_devices (
    device_id           VARCHAR(100) PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    sensor_type         VARCHAR(50)  NOT NULL,
    zone_id             VARCHAR(100) NOT NULL,
    latitude            DOUBLE PRECISION NOT NULL,
    longitude           DOUBLE PRECISION NOT NULL,
    status              VARCHAR(50)  NOT NULL,
    last_heartbeat_at   TIMESTAMP WITH TIME ZONE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL
);
