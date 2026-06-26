# Build all UrbanFlow Docker images for local Kubernetes deployment
$ErrorActionPreference = "Stop"

$Root = Split-Path $PSScriptRoot -Parent
Set-Location $Root

$services = @(
    "auth-service",
    "api-gateway",
    "traffic-service",
    "iot-device-service",
    "incident-service",
    "alert-service",
    "environment-service",
    "notification-service",
    "websocket-service",
    "sensor-simulator",
    "digital-twin-service",
    "analytics-service",
    "event-service"
)

Write-Host "Building UrbanFlow Docker images..." -ForegroundColor Cyan

foreach ($service in $services) {
    $image = "urbanflow/$service`:local"
    Write-Host "  -> $image" -ForegroundColor Yellow
    docker build -f "services/$service/Dockerfile" -t $image .
    if ($LASTEXITCODE -ne 0) { throw "Docker build failed for $service" }
}

Write-Host "All images built successfully." -ForegroundColor Green
Write-Host "Deploy with: kubectl apply -k infra/k8s/overlays/local" -ForegroundColor Cyan
