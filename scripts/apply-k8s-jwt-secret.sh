#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
NAMESPACE="${NAMESPACE:-urbanflow}"
SECRET_NAME="${SECRET_NAME:-urbanflow-jwt-keys}"

bash "$ROOT/scripts/ensure-jwt-keys.sh" 2>/dev/null || bash "$ROOT/scripts/generate-jwt-keys.sh"

PRIVATE_KEY="$ROOT/secrets/jwt/private-key.pem"
PUBLIC_KEY="$ROOT/secrets/jwt/public-key.pem"

if [[ ! -f "$PRIVATE_KEY" || ! -f "$PUBLIC_KEY" ]]; then
  echo "JWT keys not found. Run scripts/generate-jwt-keys.sh first." >&2
  exit 1
fi

echo "Applying Kubernetes secret '$SECRET_NAME' in namespace '$NAMESPACE'..."

kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -

kubectl create secret generic "$SECRET_NAME" \
  --from-file=private-key.pem="$PRIVATE_KEY" \
  --from-file=public-key.pem="$PUBLIC_KEY" \
  -n "$NAMESPACE" \
  --dry-run=client -o yaml | kubectl apply -f -

echo "Secret '$SECRET_NAME' applied."
