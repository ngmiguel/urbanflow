#!/bin/bash
set -euo pipefail

psql -v ON_ERROR_STOP=1 --username "${POSTGRES_USER}" --dbname "${POSTGRES_DB}" <<-EOSQL
    CREATE DATABASE auth_db;
    CREATE DATABASE traffic_db;
    GRANT ALL PRIVILEGES ON DATABASE auth_db TO ${POSTGRES_USER};
    GRANT ALL PRIVILEGES ON DATABASE traffic_db TO ${POSTGRES_USER};
EOSQL

psql -v ON_ERROR_STOP=1 --username "${POSTGRES_USER}" --dbname auth_db <<-EOSQL
    GRANT ALL ON SCHEMA public TO ${POSTGRES_USER};
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO ${POSTGRES_USER};
EOSQL

psql -v ON_ERROR_STOP=1 --username "${POSTGRES_USER}" --dbname traffic_db <<-EOSQL
    CREATE EXTENSION IF NOT EXISTS postgis;
    GRANT ALL ON SCHEMA public TO ${POSTGRES_USER};
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO ${POSTGRES_USER};
EOSQL

echo "UrbanFlow databases initialized: auth_db, traffic_db"
