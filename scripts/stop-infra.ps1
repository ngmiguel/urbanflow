$ComposeFile = Join-Path $PSScriptRoot "..\infra\docker\docker-compose.yml"
docker compose -f $ComposeFile down
