#!/usr/bin/env bash
# Removes Co-authored-by: Cursor trailers from all commits on the current branch.
set -euo pipefail

if ! git rev-parse --git-dir >/dev/null 2>&1; then
  echo "Not a git repository." >&2
  exit 1
fi

branch="$(git rev-parse --abbrev-ref HEAD)"
echo "Rewriting commit messages on branch: $branch"

filter='grep -vi "co-authored-by: cursor" || true'

git filter-branch -f --msg-filter "$filter" HEAD

echo ""
echo "Done. Verify with:"
echo "  git log --format=%B | grep -i cursor || echo 'No Cursor trailers found'"
echo ""
echo "If this branch was already pushed, update remote with:"
echo "  git push --force-with-lease origin $branch"
