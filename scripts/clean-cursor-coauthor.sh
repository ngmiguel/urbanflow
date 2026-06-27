#!/usr/bin/env bash
set -euo pipefail

ALL_REFS="${ALL_REFS:-0}"
BRANCH="${BRANCH:-}"

if [[ "$ALL_REFS" == "1" ]]; then
  TARGET="--all"
elif [[ -n "$BRANCH" ]]; then
  TARGET="$BRANCH"
else
  TARGET="$(git rev-parse --abbrev-ref HEAD)"
fi

export FILTER_BRANCH_SQUELCH_WARNING=1

echo "Rewriting commit messages on: $TARGET"
git filter-branch -f --msg-filter "grep -viE 'co-authored-by: cursor|cursoragent@cursor.com' || true" "$TARGET"

echo ""
echo "Verify:"
echo "  git log --all --format=%B | grep -iE 'co-authored-by: cursor|cursoragent@cursor.com' || echo 'Clean'"
