# Creates auth_db and traffic_db if PostgreSQL was started without init scripts.
$ErrorActionPreference = "Stop"

Write-Host "Checking UrbanFlow PostgreSQL databases..." -ForegroundColor Cyan

$postgresRunning = docker ps --filter "name=urbanflow-postgres" --filter "status=running" -q
if (-not $postgresRunning) {
    Write-Host "Starting postgres container..." -ForegroundColor Yellow
    $Root = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
    docker compose -f (Join-Path $Root "infra\docker\docker-compose.yml") up -d postgres
    Start-Sleep -Seconds 15
}

$sql = @"
SELECT 'CREATE DATABASE auth_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'auth_db')\gexec
SELECT 'CREATE DATABASE traffic_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'traffic_db')\gexec
GRANT ALL PRIVILEGES ON DATABASE auth_db TO urbanflow;
GRANT ALL PRIVILEGES ON DATABASE traffic_db TO urbanflow;
"@

docker exec -i urbanflow-postgres psql -U urbanflow -d postgres -v ON_ERROR_STOP=1 -c "SELECT 1 FROM pg_database WHERE datname = 'auth_db';" | Out-Null
if ($LASTEXITCODE -ne 0) { throw "Cannot connect to PostgreSQL" }

$authExists = docker exec urbanflow-postgres psql -U urbanflow -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = 'auth_db';"
if ($authExists -ne "1") {
    Write-Host "Creating auth_db..." -ForegroundColor Yellow
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "CREATE DATABASE auth_db;"
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE auth_db TO urbanflow;"
}

$trafficExists = docker exec urbanflow-postgres psql -U urbanflow -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = 'traffic_db';"
if ($trafficExists -ne "1") {
    Write-Host "Creating traffic_db..." -ForegroundColor Yellow
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "CREATE DATABASE traffic_db;"
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE traffic_db TO urbanflow;"
    docker exec urbanflow-postgres psql -U urbanflow -d traffic_db -c "CREATE EXTENSION IF NOT EXISTS postgis;"
}

$iotExists = docker exec urbanflow-postgres psql -U urbanflow -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = 'iot_db';"
if ($iotExists -ne "1") {
    Write-Host "Creating iot_db..." -ForegroundColor Yellow
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "CREATE DATABASE iot_db;"
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE iot_db TO urbanflow;"
}

$incidentExists = docker exec urbanflow-postgres psql -U urbanflow -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = 'incident_db';"
if ($incidentExists -ne "1") {
    Write-Host "Creating incident_db..." -ForegroundColor Yellow
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "CREATE DATABASE incident_db;"
    docker exec urbanflow-postgres psql -U urbanflow -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE incident_db TO urbanflow;"
    docker exec urbanflow-postgres psql -U urbanflow -d incident_db -c "CREATE EXTENSION IF NOT EXISTS postgis;"
}

Write-Host "Databases OK. Restarting application services..." -ForegroundColor Green

$Root = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
docker compose -f (Join-Path $Root "infra\docker\docker-compose.yml") up -d auth-service api-gateway traffic-service iot-device-service incident-service

Write-Host "Done. Check status with: docker compose -f infra/docker/docker-compose.yml ps" -ForegroundColor Green
