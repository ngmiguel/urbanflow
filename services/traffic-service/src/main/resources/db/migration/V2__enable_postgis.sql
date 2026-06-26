CREATE EXTENSION IF NOT EXISTS postgis;

ALTER TABLE traffic_segments
    ADD COLUMN IF NOT EXISTS location geometry(Point, 4326);

UPDATE traffic_segments
SET location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
WHERE location IS NULL;

CREATE INDEX IF NOT EXISTS idx_traffic_segments_location ON traffic_segments USING GIST (location);
