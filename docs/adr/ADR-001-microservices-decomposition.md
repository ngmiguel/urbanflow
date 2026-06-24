# ADR-001 — Décomposition microservices UrbanFlow

| Attribut | Valeur |
|----------|--------|
| **Statut** | Accepté |
| **Date** | 2026-06-24 |
| **Décideurs** | Miguel Nguema |
| **Contexte** | Initialisation projet portfolio UrbanFlow |

## Contexte

UrbanFlow doit gérer de multiples domaines métier smart city (trafic, IoT, incidents, alertes, environnement, analytics, digital twin, notifications) avec des exigences de scalabilité, temps réel et déploiement indépendant.

Trois options ont été évaluées :

1. **Monolithe modulaire** — simplicité initiale, couplage fort, scaling global
2. **Microservices complets dès le jour 1** — 12+ services immédiatement, risque de sur-ingénierie
3. **MVP-first puis extension** — noyau démoable, croissance progressive

## Décision

Adopter une **décomposition microservices progressive en 3 phases**, avec un service par bounded context DDD et une base de données par service.

### Phase 1 — MVP

| Service | Port | Justification |
|---------|------|---------------|
| `api-gateway` | 8080 | Point d'entrée unique, sécurité centralisée |
| `auth-service` | 8081 | Authentification transversale, prérequis à toute API |
| `traffic-service` | 8082 | Cas d'usage principal smart city, démo visuelle forte |

**Infrastructure MVP :** PostgreSQL (2 bases), Kafka, Docker Compose.

### Phase 2 — Core

| Service | Port | Justification |
|---------|------|---------------|
| `iot-device-service` | 8083 | Registre capteurs, prérequis simulation |
| `incident-service` | 8084 | PostGIS, corrélation avec trafic |
| `alert-service` | 8085 | Valeur métier : alertes intelligentes |
| `environment-service` | 8086 | Capteurs environnementaux |
| `notification-service` | 8087 | Dispatch multi-canal |
| `websocket-service` | 8088 | Temps réel pour opérateurs |

**Infrastructure Phase 2 :** + Redis.

### Phase 3 — Advanced

| Service | Port | Justification |
|---------|------|---------------|
| `sensor-simulator` | 8090 | Démos et tests sans hardware |
| `digital-twin-service` | 8091 | Différenciateur portfolio |
| `analytics-service` | 8092 | KPIs et tableaux de bord |
| `event-service` | 8093 | Événements urbains planifiés |

**Infrastructure Phase 3 :** Kubernetes, Prometheus, Grafana, GitHub Actions CI/CD.

## Communication inter-services

| Type | Mécanisme | Quand |
|------|-----------|-------|
| Synchrone | REST via Gateway | Commandes CRUD, queries |
| Asynchrone | Kafka | Domain events, notifications |
| Temps réel | Redis Pub/Sub + WebSocket | Live updates |

**Règle :** pas d'appels REST directs service-to-service sauf cas exceptionnel documenté. Le Gateway et Kafka sont les points de couplage.

## Modules partagés

| Module | Contenu autorisé | Interdit |
|--------|-----------------|----------|
| `urbanflow-common` | Exceptions, utils, constantes, DTOs génériques | Logique métier |
| `urbanflow-events` | Schémas d'événements Kafka (records Java) | Dépendances Spring |

## Conséquences

### Positives

- Démo recruteur possible dès la Phase 1 (auth + trafic + Kafka)
- Commits atomiques et historique Git lisible
- Scaling indépendant par domaine
- Alignement DDD / Clean Architecture par service

### Négatives

- Complexité opérationnelle (Docker Compose, puis K8s)
- Latence inter-services vs monolithe
- Gestion de la cohérence eventuelle (compensée par outbox pattern)

### Risques mitigés

| Risque | Mitigation |
|--------|-----------|
| Sur-ingénierie | MVP-first, 3 services initiaux |
| Incohérence events | Module `urbanflow-events` partagé |
| Debugging distribué | Correlation ID + OpenTelemetry (Phase 3) |

## Alternatives rejetées

- **Monolithe** : ne démontre pas les compétences microservices attendues pour ce portfolio
- **12 services d'un coup** : historique Git confus, projet jamais "finissable" pour une démo
