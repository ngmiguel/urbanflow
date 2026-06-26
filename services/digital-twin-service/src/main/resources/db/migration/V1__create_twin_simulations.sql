CREATE TABLE twin_simulations (
    id                      UUID PRIMARY KEY,
    zone_id                 VARCHAR(100) NOT NULL,
    scenario_type           VARCHAR(50)  NOT NULL,
    status                  VARCHAR(50)  NOT NULL,
    baseline_congestion     VARCHAR(50)  NOT NULL,
    baseline_speed_kmh      DOUBLE PRECISION NOT NULL,
    projected_congestion    VARCHAR(50)  NOT NULL,
    projected_speed_kmh     DOUBLE PRECISION NOT NULL,
    estimated_delay_minutes INTEGER      NOT NULL,
    impact_summary          VARCHAR(500) NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_twin_simulations_zone_id ON twin_simulations (zone_id);
CREATE INDEX idx_twin_simulations_created_at ON twin_simulations (created_at DESC);
