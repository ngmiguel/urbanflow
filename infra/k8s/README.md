# UrbanFlow — Kubernetes

Manifests [Kustomize](https://kustomize.io/) pour déployer la stack complète sur un cluster Kubernetes (Minikube, Kind, cloud).

## Structure

```
infra/k8s/
├── base/                    # Manifests communs
│   ├── infrastructure/      # PostgreSQL, Redis, Kafka
│   └── apps/                # 14 microservices Spring Boot
└── overlays/
    └── local/               # Images locales + NodePort gateway
```

## Prérequis

- `kubectl` 1.28+
- `kustomize` (intégré à kubectl >= 1.21)
- Cluster Kubernetes local (Minikube / Kind / Docker Desktop)
- Images Docker construites localement
- **Clés JWT générées** (voir [docs/JWT-KEYS.md](../../docs/JWT-KEYS.md))

## Déploiement local

```powershell
# 0. Setup dev (JWT keys + git hooks) — first time only
.\scripts\setup-dev.ps1

# 1. Construire les images
.\scripts\build-images.ps1

# 2. Déployer (crée le secret JWT + postgres secret + stack)
.\scripts\deploy-k8s.ps1

# 4. Vérifier
kubectl get pods -n urbanflow
kubectl get svc -n urbanflow api-gateway
```

Manual JWT secret only:

```powershell
.\scripts\apply-k8s-jwt-secret.ps1
```

## Accès API Gateway

- **LoadBalancer** (overlay `base`) : `http://<EXTERNAL-IP>:8080`
- **NodePort** (overlay `local`) : port exposé via `kubectl get svc api-gateway -n urbanflow`

## CI/CD

Le workflow GitHub Actions `.github/workflows/ci.yml` exécute :

1. Tests par module (matrix parallèle)
2. `mvn verify` sur `main` et `develop`
3. Smoke test Docker (api-gateway, auth-service, iot-device, event-service)

Les tests CI utilisent des clés JWT **dédiées** dans `src/test/resources/jwt/` — pas `secrets/jwt/`.

## Notes production

- Générer une **paire RSA unique par environnement** (`scripts/generate-jwt-keys.ps1`)
- Injecter la clé privée via Kubernetes Secret (`urbanflow-jwt-keys`) ou gestionnaire de secrets (Sealed Secrets, Vault, cloud KMS)
- Ne jamais inclure les clés privées dans les images Docker
- Utiliser des bases managées (RDS, Cloud SQL) plutôt que PostgreSQL in-cluster
- Configurer HPA et resource limits par service
- Publier les images sur GHCR avec tags versionnés
- Planifier la rotation des clés JWT (voir [docs/JWT-KEYS.md](../../docs/JWT-KEYS.md))
