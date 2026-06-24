# Start UrbanFlow local infrastructure (PostgreSQL, Redis, Kafka)
$ComposeFile = Join-Path $PSScriptRoot "..\infra\docker\docker-compose.yml"

Write-Host "Starting UrbanFlow infrastructure..." -ForegroundColor Cyan
docker compose -f $ComposeFile up -d

Write-Host ""
Write-Host "Services:" -ForegroundColor Green
Write-Host "  PostgreSQL  -> localhost:5432  (auth_db, traffic_db)"
Write-Host "  Redis       -> localhost:6379"
Write-Host "  Kafka       -> localhost:9092"
Write-Host ""
Write-Host "Check status: docker compose -f infra/docker/docker-compose.yml ps"
