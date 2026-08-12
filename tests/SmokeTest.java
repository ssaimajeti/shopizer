package com.shopizer.upgrade.validation;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.springdoc.core.SpringDocConfiguration;
import io.swagger.v3.oas.models.OpenAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlatformUpgradeValidationTest {

    private static final String TARGET_JAVA_VERSION = "17"; // or "21" depending on selected LTS
    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.6";
    private static final String TARGET_SPRING_FRAMEWORK_VERSION = "6.1.5";
    private static final String EXPECTED_SPRINGDOC_CLASS = "org.springdoc.core.SpringDocConfiguration";
    private static final String LEGACY_SWAGGERFOX_CLASS = "springfox.documentation.spring.web.plugins.Docket";

    @Test
    @Order(1)
    void shouldUseTargetJavaVersion() {
        String runtimeVersion = System.getProperty("java.version");
        assertNotNull(runtimeVersion, "Java version system property not found");
        assertTrue(runtimeVersion.startsWith(TARGET_JAVA_VERSION),
                "Running JVM is not at expected major version: expected " + TARGET_JAVA_VERSION + ", found: " + runtimeVersion);
    }

    @Test
    @Order(2)
    void shouldUseTargetSpringBootVersion() {
        String springBootVersion = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, springBootVersion,
                "Application is not running with Spring Boot " + TARGET_SPRING_BOOT_VERSION);
    }

    @Test
    @Order(3)
    void shouldUseTargetSpringFrameworkVersion() {
        String springFrameworkVersion = SpringVersion.getVersion();
        assertEquals(TARGET_SPRING_FRAMEWORK_VERSION, springFrameworkVersion,
                "Spring Framework version mismatch: expected " + TARGET_SPRING_FRAMEWORK_VERSION + " but was " + springFrameworkVersion);
    }

    @Test
    @Order(4)
    void criticalRestEndpointsShouldWorkCorrectly(@Value("${server.port:8080}") int port) {
        // Example: Replace with actual REST path(s) relevant to Shopizer
        // E.g., /api/v1/store or /api/products
        HttpResponse response;
        try {
            response = HttpUtil.get("http://localhost:" + port + "/api/v1/store");
        } catch (Exception e) {
            fail("REST API critical endpoint did not respond as expected", e);
            return;
        }
        assertEquals(200, response.getStatusCode(), "Critical REST API endpoint (/api/v1/store) not available post-upgrade");
    }

    @Test
    @Order(5)
    void shouldNotContainDeprecatedSpringfoxApi() {
        assertThrows(ClassNotFoundException.class, () -> Class.forName(LEGACY_SWAGGERFOX_CLASS), 
            "Legacy Springfox classes should not be present after upgrade");
    }

    @Test
    @Order(6)
    void shouldContainSpringDocOpenApiCore() {
        assertDoesNotThrow(() -> Class.forName(EXPECTED_SPRINGDOC_CLASS),
            "Springdoc OpenAPI classes should be on classpath after upgrade");
    }

    @Test
    @Order(7)
    void openApiBeanLoadsAndBasicConfigWorks(org.springframework.context.ApplicationContext context) {
        OpenAPI openAPI = context.getBean(OpenAPI.class);
        assertNotNull(openAPI, "OpenAPI bean should exist after upgrade to springdoc-openapi");
        
        // Optionally, check a new config property known to be introduced in OpenAPI 3.x/springdoc 2.x
        // For demonstration, check springdoc.api-docs.enabled (Spring Boot property binding)
        boolean apiDocsEnabled = context.getEnvironment().getProperty("springdoc.api-docs.enabled", Boolean.class, true);
        assertTrue(apiDocsEnabled, "Config key 'springdoc.api-docs.enabled' (new in springdoc 2.x) should be loadable and true by default");
    }

    @Test
    @Order(8)
    void shouldNotHaveLegacyJava11Apis() {
        // Example: check that java.util.Optional.isEmpty (Java 11-) is not being depended on;
        // instead, ensure Java 17+ APIs work (e.g., Pattern.matches as static method)
        Method isEmptyMethod = null;
        try {
            isEmptyMethod = Optional.class.getDeclaredMethod("isEmpty");
        } catch (NoSuchMethodException e) {
            fail("Optional.isEmpty() missing: not running on at least Java 11+, which is required for upgrade");
        }
        assertNotNull(isEmptyMethod, "Optional.isEmpty() should exist in Java 11+, available in 17/21");
        
        // As a negative test, check for removed APIs (ex: SecurityManager is deprecated and unavailable)
        assertThrows(ClassNotFoundException.class, () -> Class.forName("java.lang.SecurityManager"),
                "SecurityManager should not exist after Java 17+ (removed in 17)");
    }

    // Util inner class for simple HTTP GET in @SpringBootTest context
    private static class HttpUtil {
        static HttpResponse get(String url) throws Exception {
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.setRequestMethod("GET");
            int status = conn.getResponseCode();
            String body = new String(conn.getInputStream().readAllBytes());
            return new HttpResponse(status, body);
        }
    }

    private static class HttpResponse {
        private final int statusCode;
        private final String body;

        HttpResponse(int code, String body) {
            this.statusCode = code;
            this.body = body;
        }

        int getStatusCode() { return statusCode; }
        String getBody() { return body; }
    }
}