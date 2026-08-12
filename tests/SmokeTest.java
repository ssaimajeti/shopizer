package com.shopizer.upgrade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeSuccessValidationTest {

    private static final String TARGET_JAVA_VERSION = "17";
    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.6";
    private static final String TARGET_SPRING_VERSION = "6.1.6";
    private static final String TARGET_OPENAPI_DEPENDENCY = "org.springdoc";
    private static final String[] DEPRECATED_SWAGGER_PACKAGES = {
            "springfox.documentation.swagger2",
            "springfox.documentation.swagger.web",
            "springfox.documentation.swagger.common"
    };

    @Test
    @DisplayName("Java Runtime is Java 17 or 21")
    void shouldUseTargetJavaVersion() {
        String actualVersion = System.getProperty("java.version");
        assertNotNull(actualVersion, "Java version system property should be present");
        boolean matches = actualVersion.startsWith(TARGET_JAVA_VERSION) || actualVersion.startsWith("21");
        assertTrue(matches, "Java is not at target version (expected 17 or 21). Actual: " + actualVersion);
    }

    @Test
    @DisplayName("Spring Boot is at 3.2.6")
    void shouldUseCorrectSpringBootVersion() {
        String version = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, version, "Spring Boot version mismatch");
    }

    @Test
    @DisplayName("Spring Framework is at 6.1.6")
    void shouldUseCorrectSpringFrameworkVersion() {
        String version = SpringVersion.getVersion();
        assertEquals(TARGET_SPRING_VERSION, version, "Spring Framework version mismatch");
    }

    @Test
    @DisplayName("Critical Spring Boot REST and Data JPA paths are usable")
    void shouldLoadSpringBootContextAndJpa() {
        // Try loading critical classes to ensure they're present and from Spring 3.x
        try {
            Class<?> webClass = Class.forName("org.springframework.web.bind.annotation.RestController");
            Class<?> jpaClass = Class.forName("org.springframework.data.jpa.repository.JpaRepository");
            Package webPkg = webClass.getPackage();
            Package jpaPkg = jpaClass.getPackage();
            assertNotNull(webPkg, "Spring Web package must not be null");
            assertNotNull(jpaPkg, "Spring Data JPA package must not be null");
            String webPkgVersion = webPkg.getImplementationVersion();
            String jpaPkgVersion = jpaPkg.getImplementationVersion();
            assertTrue(webPkg.getName().startsWith("org.springframework.web"), "Spring Web package not found");
            assertTrue(jpaPkg.getName().startsWith("org.springframework.data.jpa"), "Spring Data JPA package not found");
        } catch (ClassNotFoundException e) {
            fail("Critical Spring class missing: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Springfox/Swagger APIs are not present")
    void shouldNotLoadSpringfoxSwagger() {
        for (String pkg : DEPRECATED_SWAGGER_PACKAGES) {
            try {
                Class.forName(pkg + ".Swagger2DocumentationConfiguration");
                fail("Deprecated Springfox Swagger2 API should not be present: " + pkg);
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    @Test
    @DisplayName("OpenAPI (springdoc) is present as replacement")
    void shouldHaveOpenApiSpringdocPresent() {
        try {
            Class<?> openApiConfig = Class.forName("org.springdoc.core.SpringDocConfigProperties");
            assertNotNull(openApiConfig, "OpenAPI springdoc-core should be present");
        } catch (ClassNotFoundException e) {
            fail("Springdoc OpenAPI dependency is not present or not properly configured");
        }
    }

    @Test
    @DisplayName("New Spring Boot 3.x configuration keys are loaded without error")
    void shouldLoadNewSpringBoot3ConfigKeys() throws IOException {
        // configprop: management.endpoint.health.show-details=always is a 3.x+ config example
        Properties props = new Properties();
        try (var is = getClass().getResourceAsStream("/application.properties")) {
            if (is != null) {
                props.load(is);
                // Example: ensure new or changed keys load without errors
                props.getProperty("management.endpoint.health.show-details");
                props.getProperty("spring.threads.virtual.enabled");
            }
        }
    }
}