$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location $repoRoot

Write-Host "UrbanFlow — developer setup" -ForegroundColor Cyan
Write-Host ""

& (Join-Path $PSScriptRoot "install-git-hooks.ps1")
Write-Host ""

& (Join-Path $PSScriptRoot "ensure-jwt-keys.ps1")
Write-Host ""

Write-Host "Setup complete." -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:"
Write-Host "  mvn verify                          # run tests"
Write-Host "  .\scripts\start-infra.ps1           # Docker Compose stack"
Write-Host "  .\scripts\build-images.ps1          # K8s local images"
Write-Host "  .\scripts\deploy-k8s.ps1            # Kubernetes deploy"
