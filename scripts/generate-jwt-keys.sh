#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TARGET_DIR="${TARGET_DIR:-$ROOT/secrets/jwt}"
DEV_RESOURCES_DIR="$ROOT/services/auth-service/src/main/resources/jwt"
FORCE="${FORCE:-0}"
SKIP_DEV_COPY="${SKIP_DEV_COPY:-0}"

mkdir -p "$TARGET_DIR"

PRIVATE_KEY="$TARGET_DIR/private-key.pem"
PUBLIC_KEY="$TARGET_DIR/public-key.pem"

if [[ -f "$PRIVATE_KEY" || -f "$PUBLIC_KEY" ]]; then
  if [[ "$FORCE" != "1" ]]; then
    echo "JWT keys already exist in $TARGET_DIR"
    echo "Set FORCE=1 to regenerate (invalidates all existing tokens)."
    exit 0
  fi
  echo "Regenerating JWT keys (FORCE=1)..."
fi

if ! command -v openssl >/dev/null 2>&1; then
  echo "openssl is required. Install OpenSSL and retry." >&2
  exit 1
fi

echo "Generating RSA-2048 JWT key pair..."
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out "$PRIVATE_KEY"
openssl pkey -in "$PRIVATE_KEY" -pubout -out "$PUBLIC_KEY"
chmod 600 "$PRIVATE_KEY"
chmod 644 "$PUBLIC_KEY"

if [[ "$SKIP_DEV_COPY" != "1" ]]; then
  mkdir -p "$DEV_RESOURCES_DIR"
  cp "$PRIVATE_KEY" "$DEV_RESOURCES_DIR/private-key.pem"
  cp "$PUBLIC_KEY" "$DEV_RESOURCES_DIR/public-key.pem"
  echo "Copied keys to $DEV_RESOURCES_DIR for local Maven runs."
fi

echo ""
echo "JWT keys ready:"
echo "  Private: $PRIVATE_KEY"
echo "  Public:  $PUBLIC_KEY"
echo ""
echo "Never commit these files. Test/CI uses dedicated keys under src/test/resources/jwt/."
