param(
    [switch]$SkipInstall,
    [string]$MavenArgs = "-B verify"
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location $repoRoot

if (-not $SkipInstall) {
    & (Join-Path $PSScriptRoot "install-git-hooks.ps1")
}

$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
    throw "Maven (mvn) not found in PATH. Install JDK 21 and Maven, then retry."
}

Write-Host "Running full Maven test suite: mvn $MavenArgs"
& mvn @($MavenArgs.Split(" "))
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

Write-Host "All module tests passed."
