param(
    [switch]$Force
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$privateKey = Join-Path $repoRoot "secrets/jwt/private-key.pem"
$publicKey = Join-Path $repoRoot "secrets/jwt/public-key.pem"

if ($Force -or -not ((Test-Path $privateKey) -and (Test-Path $publicKey))) {
    Write-Host "JWT keys missing — generating..." -ForegroundColor Yellow
    $args = @()
    if ($Force) { $args += "-Force" }
    & (Join-Path $PSScriptRoot "generate-jwt-keys.ps1") @args
}
else {
    Write-Host "JWT keys found in secrets/jwt/"
}
