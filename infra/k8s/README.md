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

## Déploiement local

```powershell
# 1. Construire les images
.\scripts\build-images.ps1

# 2. Créer le secret (copier l'exemple)
copy infra\k8s\base\secret.example.yaml infra\k8s\base\secret.yaml
kubectl apply -f infra\k8s\base\secret.yaml

# 3. Déployer
kubectl apply -k infra/k8s/overlays/local

# 4. Vérifier
kubectl get pods -n urbanflow
kubectl get svc -n urbanflow api-gateway
```

## Accès API Gateway

- **LoadBalancer** (overlay `base`) : `http://<EXTERNAL-IP>:8080`
- **NodePort** (overlay `local`) : port exposé via `kubectl get svc api-gateway -n urbanflow`

## CI/CD

Le workflow GitHub Actions `.github/workflows/ci.yml` exécute :

1. `mvn verify` sur `main` et `develop`
2. Smoke test Docker (api-gateway + auth-service)

## Notes production

- Remplacer `secret.example.yaml` par un gestionnaire de secrets (Sealed Secrets, Vault, cloud KMS)
- Utiliser des bases managées (RDS, Cloud SQL) plutôt que PostgreSQL in-cluster
- Configurer HPA et resource limits par service
- Publier les images sur GHCR avec tags versionnés
