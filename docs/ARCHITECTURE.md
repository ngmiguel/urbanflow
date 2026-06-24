# UrbanFlow — Architecture

Document de référence pour la plateforme smart city UrbanFlow.

## 1. Vision

UrbanFlow est une plateforme event-driven, temps réel et cloud-native destinée à une infrastructure smart city réelle. Elle agrège des flux IoT, du trafic routier, des incidents, des données environnementales et des alertes corrélées, avec diffusion temps réel vers opérateurs et citoyens.

## 2. Principes architecturaux

| Principe | Application |
|----------|-------------|
| **Microservices** | Un service = un bounded context DDD, une base de données |
| **Event-driven** | Communication asynchrone via Kafka pour les domain events |
| **Clean Architecture** | domain → application → infrastructure → presentation |
| **Database per service** | Isolation des données, pas de jointures cross-service |
| **API-first** | Contrats OpenAPI, versioning `/api/v1` |
| **Security by design** | JWT, RBAC, secrets externalisés |
| **Observability** | Metrics, logs structurés, tracing distribué |
| **MVP-first delivery** | Noyau démoable d'abord, extension progressive |

## 3. Décomposition microservices

### Phase 1 — MVP (priorité recruteur : démo fonctionnelle)

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ api-gateway  │────▶│ auth-service │     │traffic-service│
│   :8080      │     │    :8081     │     │    :8082      │
└──────────────┘     └──────┬───────┘     └──────┬────────┘
                            │                    │
                     ┌──────▼───────┐     ┌───────▼───────┐
                     │ PostgreSQL   │     │ PostgreSQL    │
                     │  (auth_db)   │     │ (traffic_db)  │
                     └──────────────┘     └───────┬───────┘
                                                  │
                                           ┌──────▼───────┐
                                           │    Kafka     │
                                           └──────────────┘
```

**Objectif MVP :** un recruteur peut cloner, lancer Docker Compose, s'authentifier, consulter/créer des segments de trafic, voir des événements Kafka.

### Phase 2 — Core services

- `iot-device-service` — registre capteurs, heartbeat, statut
- `incident-service` — accidents, travaux (PostGIS)
- `alert-service` — règles, corrélation multi-sources
- `environment-service` — qualité air, bruit, température
- `notification-service` — dispatch notifications
- `websocket-service` — hub STOMP temps réel via Redis Pub/Sub

### Phase 3 — Advanced

- `sensor-simulator` — scénarios démo (`rush-hour`, `pollution-spike`)
- `digital-twin-service` — simulation what-if
- `analytics-service` — KPIs, agrégations batch
- `event-service` — événements urbains planifiés
- Kubernetes, CI/CD complet, tests Testcontainers

## 4. Architecture event-driven

### Flux principal

1. Un capteur ou service publie un **domain event** sur Kafka
2. Les consumers concernés traitent l'événement (alert, analytics, notification)
3. Le **outbox pattern** garantit la cohérence DB + Kafka
4. Les échecs vont vers `urbanflow.dead-letter`

### Topics Kafka

| Topic | Producteur | Consommateur(s) | Clé partition |
|-------|-----------|-----------------|---------------|
| `urbanflow.traffic.updates` | traffic-service | alert, analytics | `segmentId` |
| `urbanflow.sensor.raw` | iot, simulator | environment, alert | `deviceId` |
| `urbanflow.incident.events` | incident-service | alert, notification | `incidentId` |
| `urbanflow.alert.events` | alert-service | notification, websocket | `alertType` |
| `urbanflow.notification.outbox` | notification-service | websocket-service | `userId` |
| `urbanflow.anomaly.detected` | alert-service (ML rules) | notification, analytics | `zoneId` |
| `urbanflow.dead-letter` | tous | ops/replay | — |

## 5. Stratégie Redis

| Cas d'usage | Clé / pattern | TTL |
|-------------|---------------|-----|
| Cache segments trafic | `traffic:segment:{id}` | 60s |
| Rate limiting gateway | `ratelimit:{ip}:{endpoint}` | 60s |
| Sessions WebSocket | `ws:session:{userId}` | 24h |
| Pub/Sub temps réel | `channel:zone:{zoneId}` | — |
| Idempotence Kafka | `idempotency:{eventId}` | 24h |
| État Digital Twin | `twin:zone:{zoneId}` | — |
| Index geo capteurs | GEO `sensors:geo` | — |
| Ranking congestion | ZSET `congestion:ranking` | — |

## 6. WebSocket temps réel

```
Client ──▶ API Gateway (/ws) ──▶ websocket-service
                                       │
                                  Redis Pub/Sub
                                       ▲
                          notification / alert services
```

- Protocole : **STOMP** over WebSocket (fallback SockJS)
- Topics : `/topic/zone/{zoneId}`, `/topic/alerts`, `/topic/traffic/live`
- Auth : JWT au handshake WebSocket
- Scale : N instances websocket-service + Redis Pub/Sub

## 7. Clean Architecture (par service)

```
service/
├── domain/           # Entités, Value Objects, Domain Events, Repository interfaces
├── application/      # Use Cases, Commands, Queries, Ports
├── infrastructure/   # JPA, Kafka, Redis, adapters
└── presentation/     # REST Controllers, WebSocket handlers, OpenAPI
```

**Règle de dépendance :** les couches internes ne connaissent pas les couches externes.

## 8. Bounded Contexts (DDD)

| Context | Service | Agrégats principaux |
|---------|---------|---------------------|
| Identity & Access | auth-service | User, Role, RefreshToken |
| Traffic Management | traffic-service | TrafficSegment, CongestionLevel |
| IoT Registry | iot-device-service | Device, DeviceHeartbeat |
| Incident Response | incident-service | Incident, RoadClosure |
| Alerting | alert-service | AlertRule, Alert |
| Environmental Monitoring | environment-service | SensorReading, AirQualityIndex |
| Urban Analytics | analytics-service | Metric, Dashboard |
| Digital Twin | digital-twin-service | Simulation, Scenario |
| Notifications | notification-service | Notification, Subscription |

## 9. PostgreSQL

- **1 base par service** : `auth_db`, `traffic_db`, `incident_db`, etc.
- **PostGIS** : traffic-service, incident-service (géospatial)
- **TimescaleDB** (Phase 3) : environment-service (séries temporelles)
- **Flyway** : migrations versionnées dans chaque service
- **Partitionnement** : tables d'événements par mois
- **Index** : GIST (geo), BRIN (timestamps), composite `(zone_id, created_at)`

## 10. API Gateway

| Route | Service cible |
|-------|--------------|
| `/api/v1/auth/**` | auth-service |
| `/api/v1/traffic/**` | traffic-service |
| `/api/v1/incidents/**` | incident-service |
| `/api/v1/devices/**` | iot-device-service |
| `/api/v1/alerts/**` | alert-service |
| `/api/v1/environment/**` | environment-service |
| `/ws/**` | websocket-service |

**Filtres :** JWT validation, rate limiting, CORS, X-Request-Id injection.

## 11. Sécurité

| Élément | Détail |
|---------|--------|
| Authentification | JWT RS256, access token 15 min, refresh 7 j |
| Autorisation | RBAC : `CITIZEN`, `OPERATOR`, `ANALYST`, `ADMIN` |
| Gateway | OAuth2 Resource Server, routes publiques : `/auth/login`, `/auth/register` |
| Secrets | Variables d'environnement / K8s Secrets (jamais en repo) |
| Transport | HTTPS en production (TLS termination Ingress) |

## 12. Observabilité

| Pilier | Outil |
|--------|-------|
| Metrics | Micrometer → Prometheus → Grafana |
| Logs | JSON structuré (Logback), correlation ID |
| Tracing | OpenTelemetry → Jaeger |
| Alerting | Prometheus Alertmanager → notification-service |
| Health | Spring Actuator `/actuator/health`, `/actuator/prometheus` |

## 13. Résilience

- **Circuit Breaker** : Resilience4j sur appels inter-services
- **Retry** : backoff exponentiel (Kafka consumers)
- **Timeout** : 3s gateway → service, 30s consumers
- **Bulkhead** : pools threads isolés par domaine
- **Dead Letter Queue** : topic Kafka dédié + replay manuel

## 14. CI/CD (Phase 3)

```
Push → GitHub Actions
  ├── mvn verify (tests unit + integration Testcontainers)
  ├── Docker build & push (GHCR)
  ├── SonarQube analysis (optionnel)
  └── Deploy staging (K8s)
```

## 15. Stratégie de branches

```
main          ← production-ready, tags releases
develop       ← intégration continue
feature/*     ← nouvelles fonctionnalités
fix/*         ← corrections
release/*     ← préparation release
```

## 16. Roadmap commits

| Étape | Livrable | Commit |
|-------|----------|--------|
| 1 | Docs, README, LICENSE | `docs: initialize project with architecture documentation` |
| 2 | Parent Maven multi-module | `chore: initialize Maven multi-module parent project` |
| 3 | Modules shared (common, events) | `feat(shared): add common and events shared modules` |
| 4 | auth-service | `feat(auth-service): implement JWT authentication` |
| 5 | docker-compose dev | `infra(docker): add development docker-compose stack` |
| 6 | api-gateway | `feat(api-gateway): add routing and JWT security filters` |
| 7 | traffic-service | `feat(traffic-service): add traffic monitoring domain and API` |
| 8+ | Services Phase 2 & 3 | voir README |

## 17. Innovations

- **Scénarios simulateur nommés** : démos recruteur en un clic
- **Détection d'anomalies** : règles + Z-score sur fenêtres glissantes Redis
- **Digital Twin what-if** : simulation impact incident sur trafic
- **Event replay** : rejeu historique Kafka pour debug et analytics
- **Correlation ID end-to-end** : traçabilité Gateway → Kafka → WebSocket

## Références

- [ADR-001 — Microservices decomposition](adr/ADR-001-microservices-decomposition.md)
