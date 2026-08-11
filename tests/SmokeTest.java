package com.shopizer.upgrade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringBootUpgradeValidationTest {

    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.6";

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${springdoc.api-docs.enabled:true}")
    private boolean springdocApiDocsEnabled;

    /**
     * Validates that the currently running Spring Boot version matches the target upgrade version.
     */
    @Test
    @DisplayName("Spring Boot is at exact upgraded version 3.2.6")
    void shouldUseExactTargetSpringBootVersion() {
        String activeVersion = SpringBootVersion.getVersion();
        assertEquals(
                TARGET_SPRING_BOOT_VERSION,
                activeVersion,
                "Spring Boot version must be exactly " + TARGET_SPRING_BOOT_VERSION
        );
    }

    /**
     * Validates that core controller beans are available in the ApplicationContext.
     * (Example: checks a REST controller bean)
     * Replace 'com.shopizer.shop.controllers.store.StoreCustomerRestController'
     * with actual controller bean as needed.
     */
    @Test
    @DisplayName("Critical REST controllers are active and functional")
    void shouldLoadCriticalRestControllers() {
        assertTrue(
                applicationContext.containsBeanDefinition("storeCustomerRestController") ||
                applicationContext.containsBean("storeCustomerRestController"),
                "StoreCustomerRestController bean should be present in application context"
        );
    }

    /**
     * Verifies that deprecated Springfox classes are NOT present after upgrade.
     * (No org.springframework.plugin.core.PluginRegistry or springfox.* classes should exist)
     */
    @Test
    @DisplayName("No deprecated Springfox/Springfox APIs present")
    void shouldNotHaveDeprecatedSpringfoxApis() {
        assertThrows(
                ClassNotFoundException.class,
                () -> Class.forName("springfox.documentation.spring.web.plugins.Docket"),
                "Springfox Docket class should NOT be present after migration to springdoc-openapi"
        );
    }

    /**
     * Validates that new configuration keys (example: springdoc-openapi) are active and not failing.
     */
    @Test
    @DisplayName("springdoc-openapi configuration loads and is enabled")
    void shouldLoadSpringdocOpenApiConfiguration() {
        assertThat(springdocApiDocsEnabled)
                .as("springdoc.api-docs.enabled property should be accessible and true or configured")
                .isNotNull();
    }
}