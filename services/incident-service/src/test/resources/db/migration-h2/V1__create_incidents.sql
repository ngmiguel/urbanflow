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
