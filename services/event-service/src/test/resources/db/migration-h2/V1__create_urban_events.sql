CREATE TABLE urban_events (
    id                  UUID PRIMARY KEY,
    type                VARCHAR(50)  NOT NULL,
    status              VARCHAR(50)  NOT NULL,
    title               VARCHAR(255) NOT NULL,
    description         VARCHAR(1000) NOT NULL,
    zone_id             VARCHAR(100) NOT NULL,
    latitude            DOUBLE PRECISION NOT NULL,
    longitude           DOUBLE PRECISION NOT NULL,
    starts_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    ends_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    expected_attendance INTEGER      NOT NULL,
    organizer           VARCHAR(255),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_urban_events_zone_id ON urban_events (zone_id);
CREATE INDEX idx_urban_events_status ON urban_events (status);
CREATE INDEX idx_urban_events_starts_at ON urban_events (starts_at);
