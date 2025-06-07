package com.wanafiq.ecdsa.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
@Getter
public class ApplicationProperties {
    private final Jwt jwt = new Jwt();
    private final Cors cors = new Cors();

    @Data
    public static class Jwt {
        private String privateKey;
        private String publicKey;
        private String issuer;
        private int expiryInHours;
    }

    @Data
    public static class Cors {
        private List<String> allowedOrigins;
    }
}
