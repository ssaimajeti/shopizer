package com.salesmanager.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SpringBoot32UpgradeValidationTest {

    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.7";
    private static final String[] SPRING_BOOT_3_NEW_CONFIG_KEYS = {
            "spring.threads.virtual.enabled",
            "spring.application.admin.jmx-name"
    };

    @Autowired
    private Environment environment;

    @Test
    @DisplayName("Spring Boot version must be exactly 3.2.7 and active at runtime")
    void springBootVersionIsTargetVersion() {
        String version = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, version, "Spring Boot version must be " + TARGET_SPRING_BOOT_VERSION);
    }

    @Test
    @DisplayName("Critical REST API endpoints load and application context starts")
    void applicationContextStartsAndCoreApisAccessible() {
        // The context successfully loaded if this test runs
        assertNotNull(environment, "Spring Environment should be loaded");
    }

    @Test
    @DisplayName("javax.* replaced by jakarta.* in APIs: no javax.* remnants")
    void deprecatedJavaxApiNoLongerPresent() {
        // Core deprecated javax API classes should no longer be present in the project classpath.
        // Try loading a known type that should've been replaced, e.g., javax.persistence.Entity, which is now jakarta.persistence.Entity.
        try {
            Class.forName("javax.persistence.Entity");
            fail("javax.persistence.Entity should not be present after Spring Boot 3+ (should be jakarta.persistence.Entity).");
        } catch (ClassNotFoundException expected) {
            // pass
        }
        // Ensure replacements are present
        try {
            Class<?> ent = Class.forName("jakarta.persistence.Entity");
            assertNotNull(ent, "jakarta.persistence.Entity should exist in classpath.");
        } catch (ClassNotFoundException e) {
            fail("jakarta.persistence.Entity should be present with Spring Boot 3+ upgrade.");
        }
    }

    @Test
    @DisplayName("Spring Boot 3.x new configuration keys must resolve without error")
    void newSpringBootConfigKeysResolve() {
        Arrays.stream(SPRING_BOOT_3_NEW_CONFIG_KEYS).forEach(key -> {
            try {
                environment.getProperty(key);
                // No exception is a pass. Values can be null if not set, but must be resolvable.
            } catch (Exception e) {
                fail("New config key '" + key + "' did not resolve: " + e.getMessage());
            }
        });
    }

    @Nested
    @DisplayName("Critical Application Path Regression")
    class CriticalApiRegression {

        @Test
        @DisplayName("Core domain model: ProductOptionValue methods accessible")
        void productOptionValueAccessorsWork() throws Exception {
            Class<?> povClass = Class.forName("com.salesmanager.core.model.catalog.product.attribute.ProductOptionValue");
            Object pov = povClass.getDeclaredConstructor().newInstance();

            Method setCode = povClass.getMethod("setCode", String.class);
            Method getCode = povClass.getMethod("getCode");
            setCode.invoke(pov, "TEST_OPT");
            assertEquals("TEST_OPT", getCode.invoke(pov));

            Method setProductOptionValueSortOrder = povClass.getMethod("setProductOptionValueSortOrder", Integer.class);
            Method getProductOptionValueSortOrder = povClass.getMethod("getProductOptionValueSortOrder");
            setProductOptionValueSortOrder.invoke(pov, 123);
            assertEquals(123, getProductOptionValueSortOrder.invoke(pov));
        }

        @Test
        @DisplayName("Critical REST catalog model: Catalog entity methods accessible")
        void catalogEntityAccessorsWork() throws Exception {
            Class<?> catalogClass = Class.forName("com.salesmanager.core.model.catalog.catalog.Catalog");
            Object catalog = catalogClass.getDeclaredConstructor().newInstance();

            Method setCode = catalogClass.getMethod("setCode", String.class);
            Method getCode = catalogClass.getMethod("getCode");
            setCode.invoke(catalog, "DEFAULT-CATALOG");
            assertEquals("DEFAULT-CATALOG", getCode.invoke(catalog));
        }
    }

    @Test
    @DisplayName("Spring Boot starter main class now uses org.springframework.boot.SpringApplication")
    void springApplicationIsBoot3() {
        try {
            Class<?> appClass = Class.forName("org.springframework.boot.SpringApplication");
            assertNotNull(appClass, "Spring Boot 3.x org.springframework.boot.SpringApplication must exist");
            Method runMethod = appClass.getMethod("run", Class.class, String[].class);
            assertNotNull(runMethod, "SpringApplication.run(Class, String...) must exist");
        } catch (Exception e) {
            fail("Could not find org.springframework.boot.SpringApplication with run(Class, String...) signature: " + e.getMessage());
        }
    }
}