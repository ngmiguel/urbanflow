CREATE TABLE alert_rules (
    id                      UUID PRIMARY KEY,
    name                    VARCHAR(255) NOT NULL,
    source_type             VARCHAR(50)  NOT NULL,
    zone_id                 VARCHAR(100),
    output_severity         VARCHAR(50)  NOT NULL,
    enabled                 BOOLEAN      NOT NULL DEFAULT TRUE,
    incident_min_severity   VARCHAR(50),
    min_congestion_level    VARCHAR(50),
    sensor_type             VARCHAR(100),
    sensor_threshold        DOUBLE PRECISION,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE alerts (
    id               UUID PRIMARY KEY,
    rule_id          UUID,
    alert_type       VARCHAR(100) NOT NULL,
    severity         VARCHAR(50)  NOT NULL,
    message          VARCHAR(1000) NOT NULL,
    zone_id          VARCHAR(100) NOT NULL,
    status           VARCHAR(50)  NOT NULL,
    source_event_id  VARCHAR(100),
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_alert_rules_source_type ON alert_rules (source_type, enabled);
CREATE INDEX idx_alerts_zone_id ON alerts (zone_id);
CREATE INDEX idx_alerts_status ON alerts (status);
CREATE INDEX idx_alerts_created_at ON alerts (created_at DESC);
