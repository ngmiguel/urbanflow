# Remove Cursor co-author trailers from Git history

UrbanFlow commits must not include `Co-authored-by: Cursor <cursoragent@cursor.com>`.

GitHub lists **cursoragent** in the repo **Contributors** sidebar whenever that trailer appears in commits on the default branch (or cached history). Removing the trailer and force-pushing cleaned history removes Cursor from Contributors after GitHub recalculates (usually 24–72 hours).

## Prevent future trailers

1. Run `.\scripts\install-git-hooks.ps1` (also done by `setup-dev.ps1`)
2. In **Cursor**: Settings → Agents → Attribution → disable **Commit Attribution**
3. Optional CLI config (`%USERPROFILE%\.cursor\cli-config.json`):

```json
{
  "attribution": {
    "attributeCommitsToAgent": false,
    "attributePRsToAgent": false
  }
}
```

## Clean entire repository history

```powershell
.\scripts\clean-cursor-coauthor.ps1 -AllRefs
.\scripts\push-clean-history.ps1
```

Linux/macOS:

```bash
ALL_REFS=1 ./scripts/clean-cursor-coauthor.sh
git push --force-with-lease origin main develop
# repeat for feature branches or use push-clean-history.ps1
```

## Clean a single branch

```powershell
git checkout <branch>
.\scripts\clean-cursor-coauthor.ps1
git push --force-with-lease origin <branch>
```

## Verify locally

```powershell
git log --all --format=%B | Select-String -Pattern "Co-authored-by.*Cursor|cursoragent" -CaseSensitive:$false
```

No output means Git history is clean.

## Verify on GitHub

- Contributors API: https://api.github.com/repos/ngmiguel/urbanflow/contributors  
  Should list **only** `ngmiguel`.
- Graph: https://github.com/ngmiguel/urbanflow/graphs/contributors

If the sidebar still shows **cursoragent** after a clean history push, hard-refresh the page (Ctrl+F5) and wait up to 72 hours for GitHub cache refresh.
