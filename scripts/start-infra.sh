#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
COMPOSE_FILE="${ROOT_DIR}/infra/docker/docker-compose.yml"

echo "Checking JWT signing keys..."
bash "${SCRIPT_DIR}/ensure-jwt-keys.sh"

echo "Building and starting UrbanFlow..."
cd "${ROOT_DIR}"
docker compose -f "${COMPOSE_FILE}" up -d --build

echo ""
echo "UrbanFlow is starting:"
echo "  API Gateway   -> http://localhost:8080"
echo "  Auth Service  -> http://localhost:8081"
echo "  Traffic Svc   -> http://localhost:8082"
