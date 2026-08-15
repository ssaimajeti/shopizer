package com.shopizer.upgrade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class UpgradeValidationTests {

    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.3";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void testSpringBootVersion() {
        String activeVersion = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, activeVersion, "Spring Boot version must be " + TARGET_SPRING_BOOT_VERSION);
    }

    @Test
    void testRestApiEndpoint() {
        String url = "http://localhost:8080/api/v1/auth/products";
        String response = restTemplate.getForObject(url, String.class);
        assertNotNull(response, "API should return a non-null response");
    }

    @Test
    void testDeprecatedApiRemoval() {
        assertFalse(dependencyExists("javax.persistence"), "javax.persistence should have been replaced with jakarta.persistence");
        assertTrue(dependencyExists("jakarta.persistence"), "jakarta.persistence should be the active package");
    }

    @Test
    void testNewConfigurationKeys() {
        assertNotNull(environment.getProperty("spring.config.activate.on-profile"), "New configuration key should be loaded without errors");
    }

    private boolean dependencyExists(String packageName) {
        try {
            Class.forName(packageName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}