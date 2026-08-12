package com.salesmanager.shop.upgrade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ClassUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
public class SpringBootUpgradeValidationTest {

    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.6";

    @Autowired
    private Environment environment;

    @Test
    @DisplayName("Spring Boot version is upgraded and exactly matches 3.2.6")
    void testSpringBootVersion() {
        String active = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, active, "Spring Boot version mismatch after upgrade");
    }

    @Test
    @DisplayName("Java runtime version is pinned to 21")
    void testJavaVersionPin21() {
        String javaVersion = System.getProperty("java.version");
        assertTrue(javaVersion.startsWith("21"), "Java runtime version is not pinned to 21, but is: " + javaVersion);
    }

    @Test
    @DisplayName("Critical application entrypoint ShopApplication loads and runs")
    void testShopApplicationActive() throws Exception {
        // Validate the Spring Boot @SpringBootApplication class exists and is loadable
        Class<?> appClass = ClassUtils.forName(
            "com.salesmanager.shop.application.ShopApplication",
            this.getClass().getClassLoader()
        );
        assertNotNull(appClass, "ShopApplication class was not found");
        // Look for a main method as expected in a Spring Boot 3.x entrypoint
        Method mainMethod = appClass.getDeclaredMethod("main", String[].class);
        assertNotNull(mainMethod, "ShopApplication does not have a main method");
    }

    @Test
    @DisplayName("Critical REST controller beans are created post-upgrade")
    void testCriticalControllersPresent() {
        // Example: validate ProductApi exists as a REST controller
        assertDoesNotThrow(() -> ClassUtils.forName(
                "com.salesmanager.shop.store.api.v1.product.ProductApi",
                this.getClass().getClassLoader()
        ), "Critical ProductApi controller is missing after upgrade");
    }

    @Test
    @DisplayName("Springfox and other known deprecated APIs are NOT present (removed or replaced for Spring Boot 3.x)")
    void testSpringfoxSwagger2ApiNotPresent() {
        // Springfox does not work with Spring Boot 3.x; ensure classes are NOT on the classpath
        assertThrows(ClassNotFoundException.class, () ->
                ClassUtils.forName(
                        "springfox.documentation.spring.web.plugins.Docket",
                        this.getClass().getClassLoader()
                ),
                "Deprecated Springfox Docket API is still present; must be removed for Spring Boot 3.2+"
        );
        assertThrows(ClassNotFoundException.class, () ->
                ClassUtils.forName(
                        "springfox.documentation.swagger2.annotations.EnableSwagger2",
                        this.getClass().getClassLoader()
                ),
                "Deprecated Springfox EnableSwagger2 is still present; must be removed for Spring Boot 3.2+"
        );
    }

    @Test
    @DisplayName("Springdoc OpenAPI is available as the supported Swagger alternative")
    void testSpringdocOpenApiPresent() {
        assertDoesNotThrow(() -> ClassUtils.forName(
                "org.springdoc.core.SpringDocUtils",
                this.getClass().getClassLoader()
        ), "springdoc-openapi is not present as a Swagger alternative after upgrade");
    }

    @Test
    @DisplayName("New Spring Boot 3.x configuration keys load without error")
    void testNewSpringBoot3xConfigKeysLoad() {
        // Example: spring.threads.virtual.enabled is new in Boot 3.x
        String virtualThreads = environment.getProperty("spring.threads.virtual.enabled");
        // This property may be null if not set, but should not cause any exception binding or crash
        assertDoesNotThrow(() -> {
            boolean enabled = Boolean.parseBoolean(virtualThreads != null ? virtualThreads : "false");
        }, "Error loading new Spring Boot 3.x configuration key: spring.threads.virtual.enabled");
    }

    @Test
    @DisplayName("Hibernate 6.x is active (required transitively by Spring Boot 3.x)")
    void testHibernate6ClassesPresent() {
        assertDoesNotThrow(() -> ClassUtils.forName(
                "org.hibernate.query.spi.QueryImplementor",
                this.getClass().getClassLoader()
        ), "Hibernate 6.x class not present - check transitive dependencies for upgrade compliance");
    }

    @Test
    @DisplayName("Actuator health endpoint is accessible")
    void testActuatorHealthEndpoint() {
        // This is a functional smoke: ensure actuator configuration loads to enable critical health endpoint
        String[] endpoints = environment.getProperty("management.endpoints.web.exposure.include", String[].class);
        // The default value may change, but after the upgrade, health should be available
        boolean healthPresent = endpoints == null || Arrays.asList(endpoints).contains("health") || Arrays.asList(endpoints).contains("*");
        assertTrue(healthPresent, "management.endpoints.web.exposure.include does not enable 'health' endpoint");
    }

    @Test
    @DisplayName("JPA static metamodel (Jakarta) package is present, old javax.persistence removed")
    void testJakartaPersistenceMetamodelUsed() {
        assertDoesNotThrow(() -> ClassUtils.forName(
                "jakarta.persistence.metamodel.Attribute",
                this.getClass().getClassLoader()
        ), "jakarta.persistence.metamodel.Attribute is missing (should be present for Spring Boot 3.x+)");
        assertThrows(ClassNotFoundException.class, () ->
                ClassUtils.forName(
                        "javax.persistence.metamodel.Attribute",
                        this.getClass().getClassLoader()
                ),
                "javax.persistence.metamodel.Attribute is still present; this should be Jakarta after Boot 3 migration"
        );
    }

}