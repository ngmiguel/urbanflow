$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$sourceHook = Join-Path $PSScriptRoot "hooks/prepare-commit-msg"
$targetDir = Join-Path $repoRoot ".git/hooks"
$targetHook = Join-Path $targetDir "prepare-commit-msg"

if (-not (Test-Path (Join-Path $repoRoot ".git"))) {
    throw "Not a git repository: $repoRoot"
}

if (-not (Test-Path $sourceHook)) {
    throw "Missing hook template: $sourceHook"
}

New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
Copy-Item -Path $sourceHook -Destination $targetHook -Force

Write-Host "Installed prepare-commit-msg hook at $targetHook"
Write-Host "Cursor Co-authored-by trailers will be stripped from future commits."
Write-Host "Also disable Cursor commit attribution: Settings > Agents > Attribution > Commit Attribution"
