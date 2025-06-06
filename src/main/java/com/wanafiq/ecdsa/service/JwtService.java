package com.wanafiq.ecdsa.service;

import com.wanafiq.ecdsa.config.ApplicationProperties;
import com.wanafiq.ecdsa.model.Role;
import com.wanafiq.ecdsa.model.User;
import com.wanafiq.ecdsa.repository.UserRepository;
import com.wanafiq.ecdsa.web.exception.AppException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final String JWT_ALGORITHM = "EC";
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private final ApplicationProperties properties;
    private final UserRepository userRepo;

    @PostConstruct
    public void init() {
        log.info("Initializing JWT Service");

        try {
            this.privateKey = loadPrivateKey(properties.getJwt().getPrivateKey());
            this.publicKey = loadPublicKey(properties.getJwt().getPublicKey());

            log.info("JWT Service initialized");
        } catch (Exception e) {
            log.error("Unexpected error during JWT Service initialization", e);
            throw new AppException();
        }
    }

    public String generateToken(String principalName) {
        log.debug("Generating JWT token for principalName: {}", principalName);

        try {
            User user = userRepo.findByEmail(principalName)
                    .orElseThrow(() -> {
                        log.error("User with principalName {} not found", principalName);
                        return new AppException();
                    });

            String sub = user.getId();
            Instant now = Instant.now();
            Instant expiration = now.plus(properties.getJwt().getExpiryInHours(), ChronoUnit.HOURS);

            JwtBuilder builder = Jwts.builder()
                    .subject(sub)
                    .issuer(properties.getJwt().getIssuer())
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(expiration))
                    .signWith(privateKey);

            List<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();

            Map<String, Object> additionalClaims = new HashMap<>();
            additionalClaims.put("roles", roles);

            builder.claims(additionalClaims);

            return builder.compact();
        } catch (JwtException e) {
            log.error("Failed to generate JWT token for subject: {}, {}", principalName, e.getMessage());
            throw new AppException();
        } catch (Exception e) {
            log.error("Unexpected error during token generation: {}", e.getMessage());
            throw new AppException();
        }
    }

    public boolean isValidToken(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        log.debug("Extracting claims from token");

        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .requireIssuer(properties.getJwt().getIssuer())
                    .clockSkewSeconds(30)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: {}", e.getMessage());
            throw new AppException();
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw new AppException();
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
            throw new AppException();
        } catch (SecurityException e) {
            log.error("JWT signature validation failed: {}", e.getMessage());
            throw new AppException();
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT token argument: {}", e.getMessage());
            throw new AppException();
        } catch (JwtException e) {
            log.error("JwtException: {}", e.getMessage());
            throw new AppException();
        } catch (Exception e) {
            log.error("Unexpected error during token verification: {}", e.getMessage());
            throw new AppException();
        }
    }

    private PrivateKey loadPrivateKey(String base64PrivateKey) {
        log.debug("Loading private key");

        try {
            String privateKeyPEM = base64PrivateKey
                    .replace("-----BEGIN EC PRIVATE KEY-----", "")
                    .replace("-----END EC PRIVATE KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);

            try {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
                KeyFactory keyFactory = KeyFactory.getInstance(JWT_ALGORITHM);

                return keyFactory.generatePrivate(keySpec);
            } catch (Exception e) {
                log.error("Failed to load private key: {}", e.getMessage());
                throw new AppException();
            }
        } catch (Exception e) {
            log.error("Unexpected error during loading private key: {}", e.getMessage());
            throw new AppException();
        }
    }

    private PublicKey loadPublicKey(String base64PublicKey) {
        log.debug("Loading public key");

        try {
            String publicKeyPEM = base64PublicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance(JWT_ALGORITHM);

            return keyFactory.generatePublic(keySpec);

        } catch (Exception e) {
            log.error("Unexpected error during loading public key: {}", e.getMessage());
            throw new AppException();
        }
    }

}
