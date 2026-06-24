# UrbanFlow

> Plateforme smart city event-driven pour le monitoring urbain en temps réel.

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-multi--module-blue?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

UrbanFlow est une plateforme microservices conçue pour une infrastructure smart city réelle : monitoring du trafic, capteurs IoT, incidents routiers, alertes intelligentes, capteurs environnementaux et notifications temps réel.

## Stratégie de delivery

Le projet suit une approche **MVP-first** : un noyau fonctionnel, démoable et déployable en premier, puis extension progressive vers l'architecture complète. Cette approche reflète une pratique professionnelle reconnue en entreprise.

| Phase | Périmètre | Statut |
|-------|-----------|--------|
| **Phase 1 — MVP** | Auth, Gateway, Traffic, Kafka, Docker Compose | En cours |
| **Phase 2 — Core** | IoT, Incidents, Alerts, Environment, WebSocket | Planifié |
| **Phase 3 — Advanced** | Digital Twin, Analytics, Simulator, K8s, CI/CD | Planifié |

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

## Démarrage rapide

> Disponible à partir de l'étape 5 (Docker Compose) et de l'étape 4 (auth-service).

```bash
# Cloner le projet
git clone https://github.com/ngmiguel/urbanflow.git
cd urbanflow

# Lancer l'infrastructure locale (à venir)
docker compose -f infra/docker/docker-compose.yml up -d

# Builder le projet (à venir)
mvn clean install
```

## Documentation

- [Architecture complète](docs/ARCHITECTURE.md)
- [ADR-001 — Décomposition microservices](docs/adr/ADR-001-microservices-decomposition.md)

## Conventions

- **Commits** : [Conventional Commits](https://www.conventionalcommits.org/) en anglais
- **Branches** : `main` (stable), `develop`, `feature/*`
- **Code** : Clean Architecture + DDD par microservice

## Auteur

**Miguel Nguema** — [@ngmiguel](https://github.com/ngmiguel)

## Licence

Ce projet est sous licence [MIT](LICENSE).
