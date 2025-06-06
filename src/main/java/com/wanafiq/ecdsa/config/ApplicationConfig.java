package com.wanafiq.ecdsa.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
@Getter
public class ApplicationConfig {
    private final Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        private String privateKey;
        private String publicKey;
        private String issuer;
        private int expiryInHours;
    }
}
