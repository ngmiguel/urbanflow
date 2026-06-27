# UrbanFlow

> Plateforme smart city event-driven pour le monitoring urbain en temps réel.

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-multi--module-blue?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![CI](https://github.com/ngmiguel/urbanflow/actions/workflows/ci.yml/badge.svg)](https://github.com/ngmiguel/urbanflow/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

UrbanFlow est une plateforme microservices conçue pour une infrastructure smart city réelle : monitoring du trafic, capteurs IoT, incidents routiers, alertes intelligentes, capteurs environnementaux et notifications temps réel.

## Stratégie de delivery

Le projet suit une approche **MVP-first** : un noyau fonctionnel, démoable et déployable en premier, puis extension progressive vers l'architecture complète. Cette approche reflète une pratique professionnelle reconnue en entreprise.

| Phase | Périmètre | Statut |
|-------|-----------|--------|
| **Phase 1 — MVP** | Auth, Gateway, Traffic, Kafka, Docker Compose | Terminé |
| **Phase 2 — Core** | IoT, Incidents, Alerts, Environment, WebSocket | Terminé |
| **Phase 3 — Advanced** | Digital Twin, Analytics, Simulator, Events, K8s, CI/CD | Terminé |

## Stack technique

| Couche | Technologies |
|--------|-------------|
| Backend | Spring Boot 3, Spring Cloud Gateway, Spring Security, Spring Data JPA |
| Messaging | Apache Kafka |
| Cache & temps réel | Redis, WebSocket (STOMP) |
| Base de données | PostgreSQL, PostGIS, Flyway |
| API | REST, OpenAPI 3 (springdoc) |
| Sécurité | JWT (OAuth2 Resource Server) |
| Observabilité | Prometheus, Grafana, Micrometer, OpenTelemetry |
| Conteneurisation | Docker, Docker Compose, Kubernetes |
| CI/CD | GitHub Actions |

## Architecture (vue d'ensemble)

```
                    ┌─────────────────┐
                    │   API Gateway   │
                    │  (JWT, routing) │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        ▼                    ▼                    ▼
 ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
 │ auth-service│     │traffic-svc  │     │  (Phase 2+) │
 └──────┬──────┘     └──────┬──────┘     └─────────────┘
        │                   │
        ▼                   ▼
 ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
 │ PostgreSQL  │     │ PostgreSQL  │     │    Redis    │
 └─────────────┘     └──────┬──────┘     └─────────────┘
                            │
                            ▼
                     ┌─────────────┐
                     │    Kafka    │
                     └─────────────┘
```

## Structure du projet

```
urbanflow/
├── docs/                    # Documentation & ADR
├── infra/                   # Docker, K8s, monitoring
├── services/                # Microservices
├── shared/                  # Modules partagés (common, events)
├── scripts/                 # Scripts utilitaires
└── pom.xml                  # Parent Maven multi-module
```

## Microservices

| Service | Description | Phase |
|---------|-------------|-------|
| `api-gateway` | Point d'entrée, routage, rate limiting, JWT | MVP |
| `auth-service` | Authentification, JWT, RBAC | MVP |
| `traffic-service` | Monitoring trafic, segments routiers | MVP |
| `iot-device-service` | Registre et heartbeat capteurs IoT | Phase 2 |
| `incident-service` | Gestion incidents routiers | Phase 2 |
| `alert-service` | Règles d'alerte et corrélation | Phase 2 |
| `environment-service` | Capteurs environnementaux | Phase 2 |
| `notification-service` | Notifications push WebSocket | Phase 2 |
| `websocket-service` | Hub temps réel | Phase 2 |
| `sensor-simulator` | Simulation événements capteurs | Phase 3 |
| `digital-twin-service` | Simulation urbaine what-if | Phase 3 |
| `analytics-service` | KPIs et agrégations | Phase 3 |
| `event-service` | Événements urbains (concerts, marchés) | Phase 3 |

## Prérequis

- Java 21+
- Maven 3.9+
- Docker & Docker Compose
- Git

## Configuration initiale

```powershell
# Génère les clés JWT locales + installe les hooks Git
.\scripts\setup-dev.ps1
```

Voir [docs/JWT-KEYS.md](docs/JWT-KEYS.md) pour le modèle de sécurité JWT (dev / Docker / K8s / prod).

## Démarrage rapide

### Docker Compose (recommandé)

```bash
git clone https://github.com/ngmiguel/urbanflow.git
cd urbanflow

# Première fois — clés JWT + hooks
.\scripts\setup-dev.ps1

# Windows — créer les bases si Postgres déjà démarré
.\scripts\fix-databases.ps1

# Lancer toute la stack (14 microservices + infra)
docker compose -f infra/docker/docker-compose.yml up -d --build
# ou: .\scripts\start-infra.ps1

# API Gateway
# http://localhost:8080
```

### Build Maven

```bash
mvn clean verify
```

### Kubernetes (local)

```powershell
.\scripts\build-images.ps1
.\scripts\deploy-k8s.ps1
```

Voir [infra/k8s/README.md](infra/k8s/README.md) pour le détail du déploiement K8s.

## Documentation

- [Architecture complète](docs/ARCHITECTURE.md)
- [Clés JWT — génération et déploiement](docs/JWT-KEYS.md)
- [ADR-001 — Décomposition microservices](docs/adr/ADR-001-microservices-decomposition.md)
- [Déploiement Kubernetes](infra/k8s/README.md)

## Conventions

- **Commits** : [Conventional Commits](https://www.conventionalcommits.org/) en anglais
- **Branches** : `main` (stable), `develop`, `feature/*`
- **Code** : Clean Architecture + DDD par microservice

## Auteur

**Miguel Nguema** — [@ngmiguel](https://github.com/ngmiguel)

## Licence

Ce projet est sous licence [MIT](LICENSE).
