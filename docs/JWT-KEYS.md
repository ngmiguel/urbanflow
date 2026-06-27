# JWT signing keys — UrbanFlow

UrbanFlow uses **RSA-2048 (RS256)** to sign and verify access tokens. This document describes how keys are generated, stored, and injected per environment.

## Security model

| Key | Who holds it | Purpose |
|-----|--------------|---------|
| **Private key** | `auth-service` only | Signs JWTs after login/register |
| **Public key** | All resource servers + API Gateway | Verifies JWT signature via JWKS |

The private key must **never** be committed to Git or shared between production environments.

Static keys under `src/test/resources/jwt/` exist **only for unit tests and CI**. They do not protect real users.

## Key locations by environment

| Environment | Private key location | How it gets there |
|-------------|---------------------|-------------------|
| **Tests / CI** | `services/auth-service/src/test/resources/jwt/` | Committed (test-only) |
| **Local Maven** | `services/auth-service/src/main/resources/jwt/` | Generated, gitignored |
| **Local Docker** | `secrets/jwt/` mounted at `/run/secrets/jwt` | Generated, gitignored |
| **Kubernetes** | Secret `urbanflow-jwt-keys` | `apply-k8s-jwt-secret` script |
| **Production** | Cloud secret manager / K8s Secret | Unique pair per env, rotated periodically |

## First-time setup (developer)

From the repository root:

```powershell
# Recommended — hooks + JWT keys
.\scripts\setup-dev.ps1
```

Or step by step:

```powershell
.\scripts\generate-jwt-keys.ps1
```

This creates:

```
secrets/jwt/private-key.pem   # master copy (gitignored)
secrets/jwt/public-key.pem
services/auth-service/src/main/resources/jwt/   # copy for mvn spring-boot:run
```

### Regenerate keys

```powershell
.\scripts\generate-jwt-keys.ps1 -Force
```

**Warning:** invalidates all existing access tokens signed with the previous key.

## Docker Compose

`start-infra.ps1` ensures keys exist, then mounts them:

```yaml
volumes:
  - ../../secrets/jwt:/run/secrets/jwt:ro
```

`auth-service` profile `docker` reads:

```yaml
urbanflow:
  jwt:
    private-key: file:/run/secrets/jwt/private-key.pem
    public-key: file:/run/secrets/jwt/public-key.pem
```

Other services validate tokens via JWKS (`http://auth-service:8081/api/v1/auth/jwks`) — they never need the private key.

## Kubernetes

```powershell
.\scripts\build-images.ps1
.\scripts\deploy-k8s.ps1   # applies JWT secret + stack
```

Manual secret apply:

```powershell
.\scripts\apply-k8s-jwt-secret.ps1
```

The secret is mounted in `auth-service` at `/run/secrets/jwt`.

## Production checklist

- [ ] Generate a **unique** RSA pair per environment (dev / staging / prod)
- [ ] Store private key in a secret manager (Vault, AWS Secrets Manager, Azure Key Vault)
- [ ] Inject via Kubernetes Secret or mounted volume — never bake into Docker images
- [ ] Enable TLS (HTTPS) for all external traffic
- [ ] Plan key rotation: publish new public key via JWKS, roll auth-service, revoke old tokens via short TTL
- [ ] Restrict file permissions on private key (`chmod 600` on Linux)
- [ ] Audit access to secret stores

## Troubleshooting

### `JWT private key not found`

Run `.\scripts\generate-jwt-keys.ps1` then restart the service.

### Docker auth-service fails to start

Ensure `secrets/jwt/private-key.pem` exists on the host before `docker compose up`.

### CI fails on AuthServiceApplicationTests

CI uses committed test keys in `src/test/resources/jwt/`. Do not point tests at `secrets/jwt/`.

## Manual generation (OpenSSL)

```bash
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out private-key.pem
openssl pkey -in private-key.pem -pubout -out public-key.pem
chmod 600 private-key.pem
```

Keys must be **PKCS#8** private key and **SPKI** public key (PEM format expected by `AppConfig`).

## Related endpoints

- `POST /api/v1/auth/login` — returns signed access token
- `GET /api/v1/auth/jwks` — public keys for validators
- `GET /.well-known/jwks.json` — alias for JWKS
