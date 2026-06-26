CREATE TABLE incidents (
    id              UUID PRIMARY KEY,
    type            VARCHAR(50)  NOT NULL,
    severity        VARCHAR(50)  NOT NULL,
    status          VARCHAR(50)  NOT NULL,
    description     VARCHAR(1000) NOT NULL,
    zone_id         VARCHAR(100) NOT NULL,
    latitude        DOUBLE PRECISION NOT NULL,
    longitude       DOUBLE PRECISION NOT NULL,
    reported_by     VARCHAR(255),
    resolved_at     TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_incidents_zone_id ON incidents (zone_id);
CREATE INDEX idx_incidents_status ON incidents (status);
CREATE INDEX idx_incidents_severity ON incidents (severity);
CREATE INDEX idx_incidents_created_at ON incidents (created_at DESC);
