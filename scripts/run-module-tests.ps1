param(
    [Parameter(Mandatory = $true)]
    [string]$Module,

    [string]$MavenArgs = "-B test"
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location $repoRoot

$knownModules = @(
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

$match = $knownModules | Where-Object {
    $_ -eq $Module -or $_.EndsWith("/$Module") -or $_.EndsWith("\$Module")
}

if (-not $match) {
    Write-Host "Unknown module: $Module"
    Write-Host "Available modules:"
    $knownModules | ForEach-Object { Write-Host "  $_" }
    exit 1
}

$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
    throw "Maven (mvn) not found in PATH. Install JDK 21 and Maven, then retry."
}

Write-Host "Running tests for $match : mvn -pl $match -am $MavenArgs"
& mvn -pl $match -am @($MavenArgs.Split(" "))
exit $LASTEXITCODE
