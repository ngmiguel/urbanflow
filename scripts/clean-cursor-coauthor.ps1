param(
    [string]$Branch,
    [switch]$AllRefs
)

$ErrorActionPreference = "Stop"

if (-not (git rev-parse --git-dir 2>$null)) {
    throw "Not a git repository."
}

$target = if ($AllRefs) { "-- --all" } elseif ($Branch) { $Branch } else { (git rev-parse --abbrev-ref HEAD) }

Write-Host "Rewriting commit messages on: $target" -ForegroundColor Cyan
Write-Host "Removing Co-authored-by: Cursor and cursoragent@cursor.com trailers." -ForegroundColor Cyan

$env:FILTER_BRANCH_SQUELCH_WARNING = "1"
git filter-branch -f --msg-filter "grep -viE 'co-authored-by: cursor|cursoragent@cursor.com' || true" $target

Write-Host ""
Write-Host "Done. Verify with:" -ForegroundColor Green
Write-Host "  git log --all --format=%B | Select-String -Pattern 'Co-authored-by|cursoragent' -CaseSensitive:`$false"
Write-Host ""
Write-Host "Then update GitHub with:" -ForegroundColor Yellow
Write-Host "  .\scripts\push-clean-history.ps1"
