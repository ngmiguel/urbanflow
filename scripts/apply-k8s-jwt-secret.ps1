param(
    [string]$Namespace = "urbanflow",
    [string]$SecretName = "urbanflow-jwt-keys"
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
& (Join-Path $PSScriptRoot "ensure-jwt-keys.ps1")

$privateKey = Join-Path $repoRoot "secrets/jwt/private-key.pem"
$publicKey = Join-Path $repoRoot "secrets/jwt/public-key.pem"

if (-not (Test-Path $privateKey) -or -not (Test-Path $publicKey)) {
    throw "JWT keys not found. Run scripts/generate-jwt-keys.ps1 first."
}

Write-Host "Applying Kubernetes secret '$SecretName' in namespace '$Namespace'..." -ForegroundColor Cyan

kubectl create namespace $Namespace --dry-run=client -o yaml | kubectl apply -f -

kubectl create secret generic $SecretName `
    --from-file=private-key.pem=$privateKey `
    --from-file=public-key.pem=$publicKey `
    -n $Namespace `
    --dry-run=client -o yaml | kubectl apply -f -

Write-Host "Secret '$SecretName' applied." -ForegroundColor Green
