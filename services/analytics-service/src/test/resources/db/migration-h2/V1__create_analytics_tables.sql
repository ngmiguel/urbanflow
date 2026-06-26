CREATE TABLE zone_kpis (
    zone_id                 VARCHAR(100) PRIMARY KEY,
    congestion_level        VARCHAR(50)  NOT NULL,
    average_speed_kmh       DOUBLE PRECISION NOT NULL DEFAULT 0,
    alert_count             BIGINT       NOT NULL DEFAULT 0,
    incident_count          BIGINT       NOT NULL DEFAULT 0,
    anomaly_count           BIGINT       NOT NULL DEFAULT 0,
    traffic_update_count    BIGINT       NOT NULL DEFAULT 0,
    air_quality_sum         DOUBLE PRECISION NOT NULL DEFAULT 0,
    air_quality_samples     BIGINT       NOT NULL DEFAULT 0,
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE metric_history (
    id              UUID PRIMARY KEY,
    zone_id         VARCHAR(100) NOT NULL,
    metric_type     VARCHAR(50)  NOT NULL,
    metric_value    DOUBLE PRECISION NOT NULL,
    recorded_at     TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_metric_history_zone_type ON metric_history (zone_id, metric_type, recorded_at DESC);
