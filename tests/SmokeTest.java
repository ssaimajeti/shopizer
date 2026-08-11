package com.shopizer.upgrade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeValidationTest {

    private static final String TARGET_JAVA_VERSION_1 = "17";
    private static final String TARGET_JAVA_VERSION_2 = "21";
    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.6";
    private static final String TARGET_SPRING_FRAMEWORK_VERSION = "6.1.5";
    private static final String TARGET_SPRINGDOC_OPENAPI_VERSION = "2.";
    private static final String DEPRECATED_SWAGGER_PACKAGE = "springfox";
    private static final String MIGRATED_JAKARTA_NAMESPACE = "jakarta.";

    @Test
    @DisplayName("1. Java version is 17 or 21 (LTS)")
    void activeJavaVersionIsTarget() {
        String javaVersion = System.getProperty("java.version");
        assertTrue(
                javaVersion.startsWith(TARGET_JAVA_VERSION_1) || javaVersion.startsWith(TARGET_JAVA_VERSION_2),
                "Active Java version should be 17 or 21, found: " + javaVersion);
    }

    @Test
    @DisplayName("2. Spring Boot runtime version is exactly 3.2.6")
    void springBootVersionIsExact() {
        String springBootVersion = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, springBootVersion,
                "Spring Boot version must be exactly " + TARGET_SPRING_BOOT_VERSION);
    }

    @Test
    @DisplayName("3. Spring Framework version is exactly 6.1.5")
    void springFrameworkVersionIsExact() {
        String springVersion = SpringVersion.getVersion();
        assertEquals(TARGET_SPRING_FRAMEWORK_VERSION, springVersion,
                "Spring Framework version must be exactly " + TARGET_SPRING_FRAMEWORK_VERSION);
    }

    @Test
    @DisplayName("4. Jakarta EE migration: critical paths use 'jakarta.' instead of 'javax.'")
    void jakartaNamespaceIsActive() {
        // Check for presence of jakarta.servlet.* (not javax.*) in classpath
        try {
            Class<?> jakartaServlet = Class.forName("jakarta.servlet.http.HttpServletRequest");
            assertNotNull(jakartaServlet, "jakarta.servlet.HttpServletRequest should be present");
        } catch (ClassNotFoundException e) {
            fail("jakarta.servlet.HttpServletRequest not found; Jakarta EE migration may not be complete");
        }
        assertThrows(ClassNotFoundException.class, () -> {
            Class.forName("javax.servlet.http.HttpServletRequest");
        }, "javax.servlet should not be present after Jakarta EE migration");
    }

    @Test
    @DisplayName("5. Deprecated Swagger/Springfox classes are not present and new OpenAPI is available")
    void swaggerSpringfoxRemovedAndOpenApiPresent() {
        // Confirm 'springfox' packages/classes are NOT in runtime
        assertThrows(ClassNotFoundException.class, () -> {
            Class.forName("springfox.documentation.spring.web.plugins.Docket");
        }, "springfox documentation API should not be present after upgrade");
        // Confirm springdoc-openapi (v2.x) is present
        try {
            Class<?> openApiClass = Class.forName("org.springdoc.core.SpringDocUtils");
            assertNotNull(openApiClass, "Expected springdoc-openapi (org.springdoc.core.SpringDocUtils) to be present");
        } catch (ClassNotFoundException e) {
            fail("springdoc-openapi 2.x core class not found; OpenAPI migration not effective");
        }
    }

    @Test
    @DisplayName("6. OpenAPI UI new configuration property is recognized")
    void openApiUiNewConfigKeyLoads() {
        // springdoc 2.x introduces new config key: springdoc.api-docs.enabled (or similar)
        try {
            Properties props = new Properties();
            props.load(this.getClass().getResourceAsStream("/application.properties"));

            // Try accessing a new config key introduced in springdoc-openapi 2.x
            String key = "springdoc.api-docs.enabled";
            assertTrue(props.containsKey(key), "New config key '" + key + "' must be defined for OpenAPI UI");
        } catch (Exception e) {
            fail("Could not load application.properties or config key missing: " + e.getMessage());
        }
    }

    @Nested
    @DisplayName("7. Critical application REST endpoint path tests")
    class CriticalApplicationPaths {

        @Test
        @DisplayName("Critical endpoint '/api/v1/store' is present and responds under Spring Boot 3")
        void storeEndpointMapped() {
            // This is a basic test that the controller is mapped and can be loaded
            try {
                Class<?> controller = Class.forName("com.shopizer.shop.api.v1.store.StoreRestController");
                assertNotNull(controller, "StoreRestController not found, mapping may be broken");
                assertTrue(hasRequestMapping(controller, "/api/v1/store"), "Endpoint '/api/v1/store' not mapped");
            } catch (ClassNotFoundException e) {
                fail("Critical REST controller missing: " + e.getMessage());
            }
        }

        private boolean hasRequestMapping(Class<?> controllerClass, String path) {
            return java.util.Arrays.stream(controllerClass.getAnnotations())
                    .anyMatch(a -> a.annotationType().getSimpleName().contains("RequestMapping") &&
                            (annotationPathValue(a).contains(path)));
        }

        private String annotationPathValue(java.lang.annotation.Annotation annotation) {
            try {
                Method valueMethod = annotation.annotationType().getMethod("value");
                String[] values = (String[]) valueMethod.invoke(annotation);
                if (values.length > 0) {
                    return values[0];
                }
            } catch (Exception e) {
                // ignore
            }
            return "";
        }
    }
}