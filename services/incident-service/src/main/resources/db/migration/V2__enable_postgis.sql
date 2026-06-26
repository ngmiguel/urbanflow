CREATE EXTENSION IF NOT EXISTS postgis;

ALTER TABLE incidents
    ADD COLUMN IF NOT EXISTS location geometry(Point, 4326);

UPDATE incidents
SET location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
WHERE location IS NULL;

CREATE INDEX IF NOT EXISTS idx_incidents_location ON incidents USING GIST (location);
