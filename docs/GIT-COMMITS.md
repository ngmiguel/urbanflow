# Remove Cursor co-author trailers from Git history

UrbanFlow commits must not include `Co-authored-by: Cursor`.

## Prevent future trailers

1. Run `.\scripts\install-git-hooks.ps1` (also done by `setup-dev.ps1`)
2. In **Cursor**: Settings → Agents → Attribution → disable **Commit Attribution**

## Clean existing history

If old commits still contain Cursor co-author lines, rewrite the affected branch:

```powershell
git checkout <branch>
.\scripts\clean-cursor-coauthor.ps1
git push --force-with-lease origin <branch>
```

Linux/macOS:

```bash
git checkout <branch>
./scripts/clean-cursor-coauthor.sh
git push --force-with-lease origin <branch>
```

**Note:** `main` and `develop` are already clean. The legacy trailer exists only on `feature/infra-docker-compose` (commit from initial LICENSE merge).

## Verify

```powershell
git log --format=%B | Select-String -Pattern "Co-authored-by.*Cursor" -CaseSensitive:$false
```

No output means the branch is clean.
