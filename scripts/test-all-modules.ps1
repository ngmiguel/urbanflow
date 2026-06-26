$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location $repoRoot

$modules = @(
    "shared/urbanflow-common",
    "shared/urbanflow-events",
    "services/api-gateway",
    "services/auth-service",
    "services/traffic-service",
    "services/iot-device-service",
    "services/incident-service",
    "services/alert-service",
    "services/environment-service",
    "services/notification-service",
    "services/websocket-service",
    "services/sensor-simulator",
    "services/digital-twin-service",
    "services/analytics-service",
    "services/event-service"
)

$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
    throw "Maven (mvn) not found in PATH. Install JDK 21 and Maven, then retry."
}

$failed = @()

foreach ($module in $modules) {
    Write-Host ""
    Write-Host "========================================"
    Write-Host "Testing module: $module"
    Write-Host "========================================"
    & mvn -B -pl $module -am test
    if ($LASTEXITCODE -ne 0) {
        $failed += $module
    }
}

if ($failed.Count -gt 0) {
    Write-Host ""
    Write-Host "Failed modules:"
    $failed | ForEach-Object { Write-Host "  - $_" }
    exit 1
}

Write-Host ""
Write-Host "All $($modules.Count) modules passed."
