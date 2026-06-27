param(
    [string]$Branch
)

$ErrorActionPreference = "Stop"

if (-not (git rev-parse --git-dir 2>$null)) {
    throw "Not a git repository."
}

if (-not $Branch) {
    $Branch = git rev-parse --abbrev-ref HEAD
}

Write-Host "Rewriting commit messages on branch: $Branch" -ForegroundColor Cyan

$filterScript = @'
import sys
lines = sys.stdin.readlines()
filtered = [line for line in lines if "co-authored-by: cursor" not in line.lower()]
sys.stdout.writelines(filtered)
'@

$filterFile = Join-Path $env:TEMP "urbanflow-msg-filter.py"
Set-Content -Path $filterFile -Value $filterScript -Encoding utf8

git filter-branch -f --msg-filter "python `"$filterFile`"" $Branch

Write-Host ""
Write-Host "Done. Verify with:" -ForegroundColor Green
Write-Host "  git log --format=%B | Select-String -Pattern cursor -CaseSensitive:`$false"
Write-Host ""
Write-Host "If this branch was already pushed, update remote with:" -ForegroundColor Yellow
Write-Host "  git push --force-with-lease origin $Branch"
