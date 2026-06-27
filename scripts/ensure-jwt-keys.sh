#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PRIVATE_KEY="$ROOT/secrets/jwt/private-key.pem"
PUBLIC_KEY="$ROOT/secrets/jwt/public-key.pem"

if [[ ! -f "$PRIVATE_KEY" || ! -f "$PUBLIC_KEY" ]]; then
  echo "JWT keys missing — generating..."
  bash "$ROOT/scripts/generate-jwt-keys.sh"
else
  echo "JWT keys found in secrets/jwt/"
fi
