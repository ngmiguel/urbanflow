package com.urbanflow.auth.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class AppConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    KeyPair jwtKeyPair(JwtProperties jwtProperties, ResourceLoader resourceLoader) throws Exception {
        Resource privateKeyResource = resourceLoader.getResource(jwtProperties.getPrivateKey());
        Resource publicKeyResource = resourceLoader.getResource(jwtProperties.getPublicKey());

        assertKeyResourceExists(privateKeyResource, "private");
        assertKeyResourceExists(publicKeyResource, "public");

        RSAPrivateKey privateKey = loadPrivateKey(privateKeyResource);
        RSAPublicKey publicKey = loadPublicKey(publicKeyResource);
        return new KeyPair(publicKey, privateKey);
    }

    private void assertKeyResourceExists(Resource resource, String keyType) {
        if (resource.exists()) {
            return;
        }

        throw new IllegalStateException("""
                JWT %s key not found at '%s'.
                Generate a unique key pair for this environment:
                  Windows:  .\\scripts\\generate-jwt-keys.ps1
                  Linux:    ./scripts/generate-jwt-keys.sh
                See docs/JWT-KEYS.md for Docker and Kubernetes injection.
                """.formatted(keyType, resource.getDescription()).trim());
    }

    private RSAPrivateKey loadPrivateKey(Resource resource) throws Exception {
        try (InputStream inputStream = resource.getInputStream()) {
            String pem = new String(inputStream.readAllBytes())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
    }

    private RSAPublicKey loadPublicKey(Resource resource) throws Exception {
        try (InputStream inputStream = resource.getInputStream()) {
            String pem = new String(inputStream.readAllBytes())
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        }
    }
}
