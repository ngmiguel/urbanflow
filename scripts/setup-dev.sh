#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

echo "UrbanFlow — developer setup"
echo ""

bash "$ROOT/scripts/install-git-hooks.ps1" 2>/dev/null || true
if [[ -f "$ROOT/scripts/hooks/prepare-commit-msg" ]]; then
  mkdir -p "$ROOT/.git/hooks"
  cp "$ROOT/scripts/hooks/prepare-commit-msg" "$ROOT/.git/hooks/prepare-commit-msg"
  chmod +x "$ROOT/.git/hooks/prepare-commit-msg"
  echo "Installed prepare-commit-msg hook"
fi

bash "$ROOT/scripts/ensure-jwt-keys.sh"
echo ""
echo "Setup complete."
echo ""
echo "Next steps:"
echo "  mvn verify"
echo "  ./scripts/start-infra.sh"
echo "  ./scripts/build-images.ps1 && ./scripts/deploy-k8s.ps1"
