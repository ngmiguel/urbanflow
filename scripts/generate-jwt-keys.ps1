param(
    [string]$TargetDir,
    [switch]$Force,
    [switch]$SkipDevResourcesCopy
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
if (-not $TargetDir) {
    $TargetDir = Join-Path $repoRoot "secrets/jwt"
}
$devResourcesDir = Join-Path $repoRoot "services/auth-service/src/main/resources/jwt"

New-Item -ItemType Directory -Force -Path $TargetDir | Out-Null

$privateKeyPath = Join-Path $TargetDir "private-key.pem"
$publicKeyPath = Join-Path $TargetDir "public-key.pem"

if ((Test-Path $privateKeyPath) -or (Test-Path $publicKeyPath)) {
    if (-not $Force) {
        Write-Host "JWT keys already exist in $TargetDir"
        Write-Host "Use -Force to regenerate (invalidates all existing tokens)."
        return
    }
    Write-Host "Regenerating JWT keys (-Force)..." -ForegroundColor Yellow
}

function Write-PemFile {
    param(
        [string]$Path,
        [string]$Label,
        [byte[]]$DerBytes
    )

    $base64 = [Convert]::ToBase64String($DerBytes)
    $lines = for ($i = 0; $i -lt $base64.Length; $i += 64) {
        $base64.Substring($i, [Math]::Min(64, $base64.Length - $i))
    }

    $content = @(
        "-----BEGIN $Label-----"
        $lines
        "-----END $Label-----"
        ""
    ) -join [Environment]::NewLine

    Set-Content -Path $Path -Value $content -Encoding ascii -NoNewline
}

function New-RsaKeyPairWithOpenSsl {
    param(
        [string]$PrivatePath,
        [string]$PublicPath
    )

    $openssl = Get-Command openssl -ErrorAction SilentlyContinue
    if (-not $openssl) {
        $gitOpenSsl = "C:\Program Files\Git\usr\bin\openssl.exe"
        if (Test-Path $gitOpenSsl) {
            $openssl = $gitOpenSsl
        }
    }

    if (-not $openssl) {
        return $false
    }

    & $openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out $PrivatePath | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "openssl genpkey failed"
    }

    & $openssl pkey -in $PrivatePath -pubout -out $PublicPath | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "openssl pkey failed"
    }

    return $true
}

function New-RsaKeyPairWithDotNet {
    param(
        [string]$PrivatePath,
        [string]$PublicPath
    )

    $rsa = [System.Security.Cryptography.RSA]::Create(2048)
    try {
        Write-PemFile -Path $PrivatePath -Label "PRIVATE KEY" -DerBytes ($rsa.ExportPkcs8PrivateKey())
        Write-PemFile -Path $PublicPath -Label "PUBLIC KEY" -DerBytes ($rsa.ExportSubjectPublicKeyInfo())
    }
    finally {
        $rsa.Dispose()
    }
}

Write-Host "Generating RSA-2048 JWT key pair..." -ForegroundColor Cyan

$usedOpenSsl = New-RsaKeyPairWithOpenSsl -PrivatePath $privateKeyPath -PublicPath $publicKeyPath
if (-not $usedOpenSsl) {
    Write-Host "OpenSSL not found - using .NET RSA generator." -ForegroundColor Yellow
    New-RsaKeyPairWithDotNet -PrivatePath $privateKeyPath -PublicPath $publicKeyPath
}

if (-not $SkipDevResourcesCopy) {
    New-Item -ItemType Directory -Force -Path $devResourcesDir | Out-Null
    Copy-Item -Path $privateKeyPath -Destination (Join-Path $devResourcesDir "private-key.pem") -Force
    Copy-Item -Path $publicKeyPath -Destination (Join-Path $devResourcesDir "public-key.pem") -Force
    Write-Host "Copied keys to $devResourcesDir for local Maven runs."
}

Write-Host ""
Write-Host "JWT keys ready:" -ForegroundColor Green
Write-Host "  Private: $privateKeyPath"
Write-Host "  Public:  $publicKeyPath"
Write-Host ""
Write-Host "Never commit these files. CI uses keys in src/test/resources/jwt."
