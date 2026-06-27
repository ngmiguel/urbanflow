# JWT signing keys (local / Docker / K8s)

This directory holds **your environment-specific RSA key pair**. Files here are **never committed** to Git.

## Generate keys

From the repository root:

```powershell
# Windows
.\scripts\generate-jwt-keys.ps1
```

```bash
# Linux / macOS / Git Bash
./scripts/generate-jwt-keys.sh
```

This creates:

- `secrets/jwt/private-key.pem` — **secret**, used only by `auth-service` to sign JWTs
- `secrets/jwt/public-key.pem` — public, exposed via `/api/v1/auth/jwks` for token validation

Keys are also copied to `services/auth-service/src/main/resources/jwt/` for local `mvn spring-boot:run`.

## When to regenerate

- First clone / new developer machine
- Suspected key compromise
- Production key rotation (use a unique pair per environment)

## Production

Do **not** reuse dev keys. Generate a dedicated pair per environment and inject via:

- Docker Compose volume mount (dev stack)
- Kubernetes Secret (see `scripts/apply-k8s-jwt-secret.ps1`)
- Cloud secret manager (AWS Secrets Manager, Azure Key Vault, etc.)

See [docs/JWT-KEYS.md](../../docs/JWT-KEYS.md) for the full security model.
