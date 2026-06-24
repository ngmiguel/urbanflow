#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="${SCRIPT_DIR}/../infra/docker/docker-compose.yml"

echo "Starting UrbanFlow infrastructure..."
docker compose -f "${COMPOSE_FILE}" up -d

echo ""
echo "Services:"
echo "  PostgreSQL  -> localhost:5432  (auth_db, traffic_db)"
echo "  Redis       -> localhost:6379"
echo "  Kafka       -> localhost:9092"
