#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

if [[ "${SKIP_HOOKS:-0}" != "1" ]]; then
  bash "$ROOT/scripts/hooks/prepare-commit-msg" >/dev/null 2>&1 || true
  mkdir -p "$ROOT/.git/hooks"
  cp "$ROOT/scripts/hooks/prepare-commit-msg" "$ROOT/.git/hooks/prepare-commit-msg"
  chmod +x "$ROOT/.git/hooks/prepare-commit-msg"
fi

MODULES=(
  "shared/urbanflow-common"
  "shared/urbanflow-events"
  "services/api-gateway"
  "services/auth-service"
  "services/traffic-service"
  "services/iot-device-service"
  "services/incident-service"
  "services/alert-service"
  "services/environment-service"
  "services/notification-service"
  "services/websocket-service"
  "services/sensor-simulator"
  "services/digital-twin-service"
  "services/analytics-service"
  "services/event-service"
)

if [[ "${1:-}" == "--module" ]]; then
  shift
  module="$1"
  echo "Running tests for module: $module"
  mvn -B -pl "$module" -am test
  exit 0
fi

echo "Running full Maven verify suite"
mvn -B verify

echo "Running per-module smoke checks"
for module in "${MODULES[@]}"; do
  echo "==> $module"
  mvn -B -pl "$module" -am test -DskipITs
done

echo "All module tests passed."
