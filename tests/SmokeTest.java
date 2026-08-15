package com.salesmanager.core.upgrade.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ActiveProfiles("test")
public class UpgradeValidationTests {

    @Value("${java.runtime.version}")
    private String javaVersion;

    @Value("${spring-boot.version}")
    private String springBootVersion;

    private RestTemplate restTemplate;
    
    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void verifyJvmVersion() {
        assertTrue(javaVersion.startsWith("17"), "Expected JVM version to be 17 or later as latest LTS");
    }

    @Test
    public void verifySpringBootVersion() {
        assertEquals("2.7.13", springBootVersion, "Expected Spring Boot version to be 2.7.13 after upgrade");
    }

    @Test
    public void testCriticalPathAccessibility() {
        String response = restTemplate.getForObject("http://localhost:8080/api/catalog", String.class);
        assertNotNull(response, "The catalog API should return a valid response");
    }

    @Test
    public void verifyDeprecatedApis() {
        Exception exception = assertThrows(ClassNotFoundException.class, () -> {
            Class.forName("com.google.common.collect.ImmutableList");
        });
        assertNotNull(exception.getMessage().contains("ImmutableList"), "Deprecated ImmutableList should not be present");
    }

    @Test
    public void verifyNewConfigurationKeys() {
        Environment env = TestContext.getApplicationContext().getEnvironment();
        assertNotNull(env.getProperty("shopizer.new.config.key"), "The new configuration key should be loaded without errors");
    }
}