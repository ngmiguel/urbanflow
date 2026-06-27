$ErrorActionPreference = "Stop"

$branches = @(
    "main",
    "develop",
    "feature/alert-service",
    "feature/analytics-service",
    "feature/digital-twin-service",
    "feature/docker-services",
    "feature/environment-service",
    "feature/event-service",
    "feature/infra-docker-compose",
    "feature/k8s-cicd",
    "feature/notification-websocket",
    "feature/sensor-simulator"
)

foreach ($branch in $branches) {
    Write-Host "Force pushing $branch..." -ForegroundColor Cyan
    git push --force-with-lease origin "${branch}:${branch}"
}

Write-Host ""
Write-Host "All branches pushed. GitHub Contributors may take up to 24-72h to refresh." -ForegroundColor Green
Write-Host "Check: https://github.com/ngmiguel/urbanflow/graphs/contributors"
