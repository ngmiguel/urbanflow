# UrbanFlow — API Documentation (Swagger)

All REST routes from every microservice are exposed through a **single Swagger UI** at the API Gateway.

## Access

| Environment | Swagger UI | Unified OpenAPI JSON |
|-------------|------------|----------------------|
| Local (gateway) | http://localhost:8080/swagger-ui.html | http://localhost:8080/v3/api-docs/urbanflow |
| Docker Compose | http://localhost:8080/swagger-ui.html | http://localhost:8080/v3/api-docs/urbanflow |

## How it works

1. Each microservice publishes its own OpenAPI spec at `/v3/api-docs` (springdoc).
2. The API Gateway aggregates those specs into one document at `/v3/api-docs/urbanflow`.
3. Swagger UI on the gateway loads that unified document and lists **all routes** from:
   - Auth, Traffic, IoT Devices, Incidents, Alerts
   - Environment, Notifications, Sensor Simulator
   - Digital Twin, Analytics, Urban Events

WebSocket endpoints (`/ws/**`) are not included — they are not REST/OpenAPI.

## Authentication in Swagger UI

1. Call `POST /api/v1/auth/register` or `POST /api/v1/auth/login` from the **Auth** section.
2. Copy the `accessToken` from the response.
3. Click **Authorize** in Swagger UI and paste: `Bearer <accessToken>`.

Public routes (register, login, refresh, jwks) work without a token.

## Per-service Swagger (optional)

Individual services still expose their own UI when running standalone:

| Service | Port | Swagger |
|---------|------|---------|
| auth-service | 8081 | http://localhost:8081/swagger-ui.html |
| traffic-service | 8082 | http://localhost:8082/swagger-ui.html |
| … | … | `http://localhost:<port>/swagger-ui.html` |

Prefer the **gateway unified UI** for exploring the full platform.

## Configuration

Service URLs for aggregation are configured in:

- `services/api-gateway/src/main/resources/application.yml` (local)
- `services/api-gateway/src/main/resources/application-docker.yml` (Docker/K8s)

```yaml
urbanflow:
  openapi:
    services:
      - name: auth-service
        display-name: Auth
        base-uri: http://localhost:8081
```

If a service is down during aggregation, its routes are skipped and a warning is logged — other services still appear in the unified doc.
