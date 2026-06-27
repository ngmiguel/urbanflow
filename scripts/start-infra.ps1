# Start the full UrbanFlow stack (infra + microservices)
$Root = Split-Path $PSScriptRoot -Parent
$ComposeFile = Join-Path $Root "infra\docker\docker-compose.yml"

Write-Host "Checking JWT signing keys..." -ForegroundColor Cyan
& (Join-Path $PSScriptRoot "ensure-jwt-keys.ps1")

Write-Host "Building and starting UrbanFlow..." -ForegroundColor Cyan
Set-Location $Root
docker compose -f $ComposeFile up -d --build

Write-Host ""
Write-Host "UrbanFlow is starting:" -ForegroundColor Green
Write-Host "  API Gateway   -> http://localhost:8080"
Write-Host "  Auth Service  -> http://localhost:8081"
Write-Host "  Traffic Svc   -> http://localhost:8082"
Write-Host "  PostgreSQL    -> localhost:5432"
Write-Host "  Redis         -> localhost:6379"
Write-Host "  Kafka         -> localhost:9092"
Write-Host ""
Write-Host "Swagger (auth)  -> http://localhost:8081/swagger-ui.html"
Write-Host "Register via GW -> POST http://localhost:8080/api/v1/auth/register"
