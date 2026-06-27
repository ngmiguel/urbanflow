# Deploy UrbanFlow to local Kubernetes cluster
$ErrorActionPreference = "Stop"

$Root = Split-Path $PSScriptRoot -Parent
Set-Location $Root

$secretPath = Join-Path $Root "infra\k8s\base\secret.yaml"
if (-not (Test-Path $secretPath)) {
    Write-Host "Creating secret from secret.example.yaml..." -ForegroundColor Yellow
    Copy-Item (Join-Path $Root "infra\k8s\base\secret.example.yaml") $secretPath
}

Write-Host "Applying JWT signing keys secret..." -ForegroundColor Cyan
& (Join-Path $PSScriptRoot "apply-k8s-jwt-secret.ps1")

Write-Host "Applying Kubernetes manifests..." -ForegroundColor Cyan
kubectl apply -f $secretPath
kubectl apply -k infra/k8s/overlays/local

Write-Host "Waiting for core pods..." -ForegroundColor Cyan
kubectl rollout status deployment/postgres -n urbanflow --timeout=120s
kubectl rollout status deployment/auth-service -n urbanflow --timeout=180s
kubectl rollout status deployment/api-gateway -n urbanflow --timeout=180s

Write-Host "Done. Check status:" -ForegroundColor Green
kubectl get pods -n urbanflow
kubectl get svc -n urbanflow api-gateway
