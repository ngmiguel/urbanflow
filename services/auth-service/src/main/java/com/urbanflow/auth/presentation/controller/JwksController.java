package com.urbanflow.auth.presentation.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
@Tag(name = "JWKS", description = "Public keys for JWT validation")
public class JwksController {

    private final JWKSet jwkSet;

    public JwksController(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID("urbanflow-auth-key")
                .build();
        this.jwkSet = new JWKSet(rsaKey);
    }

    @GetMapping({"/api/v1/auth/jwks", "/.well-known/jwks.json"})
    @Operation(summary = "Expose JSON Web Key Set for token validation")
    public Map<String, Object> jwks() {
        return jwkSet.toJSONObject(true);
    }
}
