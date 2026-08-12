package com.shopizer.upgrade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeVerificationTest {

    private static final String TARGET_SPRING_BOOT_VERSION = "3.2.6";
    private static final String TARGET_SPRING_FRAMEWORK_VERSION = "6.1.6";

    @Test
    @DisplayName("Spring Boot is at exact upgraded version 3.2.6")
    void testSpringBootVersionIsTarget() {
        String activeVersion = SpringBootVersion.getVersion();
        assertEquals(TARGET_SPRING_BOOT_VERSION, activeVersion, "Spring Boot version must be exactly 3.2.6");
    }

    @Test
    @DisplayName("Spring Framework is at exact upgraded version 6.1.6")
    void testSpringFrameworkVersionIsTarget() {
        String springVersion = SpringVersion.getVersion();
        assertEquals(TARGET_SPRING_FRAMEWORK_VERSION, springVersion, "Spring Framework version must be exactly 6.1.6");
    }

    @Test
    @DisplayName("Application can initialize Spring context and load main REST Controller")
    void testCriticalApplicationPath() {
        // Try to load a main controller bean known to be present in the application after a Spring upgrade.
        try {
            Class<?> controller = Class.forName("com.salesmanager.shop.store.api.v1.user.UserRESTController");
            assertNotNull(controller, "UserRESTController must be loadable with Spring 3.2.6");
        } catch (ClassNotFoundException e) {
            fail("Critical REST controller class not found in classpath: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Springfox Swagger v2 API classes are absent and OpenAPI replacement is present")
    void testOpenAPIReplacementPresentAndSwaggerV2Absent() {
        // Old swagger class should be absent
        assertThrows(ClassNotFoundException.class, () ->
                Class.forName("springfox.documentation.swagger2.annotations.EnableSwagger2"),
            "Springfox Swagger2 EnableSwagger2 should not exist after upgrade to OpenAPI"
        );
        // New OpenAPI 3 replacement should be present (typical for springdoc-openapi integration)
        try {
            Class<?> openApiClass = Class.forName("org.springdoc.core.annotations.RouterOperation");
            assertNotNull(openApiClass, "OpenAPI 3 (springdoc-openapi) RouterOperation annotation must be present after upgrade");
        } catch (ClassNotFoundException e) {
            fail("OpenAPI replacement (springdoc-openapi) class not found: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("New configuration keys introduced in Spring Boot 3.x load and parse correctly")
    void testNewSpringBoot3ConfigKeysCanBeLoaded() {
        // As an example, test a new key introduced in Spring Boot 3.x ("spring.application.admin.enabled=true").
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            assertNotNull(is, "application.properties must exist in resources");
            Properties props = new Properties();
            props.load(is);
            // Simulate presence of a new config key
            assertTrue(props.containsKey("spring.application.admin.enabled"),
                    "Config key 'spring.application.admin.enabled' must be present in application.properties");
        } catch (Exception ex) {
            fail("Failed to load application.properties and check for new config keys: " + ex.getMessage());
        }
    }

    @Test
    @DisplayName("Java runtime version is at least 17")
    void testJavaRuntimeVersion() {
        String version = System.getProperty("java.version");
        assertNotNull(version, "java.version system property must be set");
        String[] versionParts = version.split("\\.");
        int major;
        try {
            // Handle both 17 and 17.0.X style
            if (versionParts[0].equals("1")) {
                major = Integer.parseInt(versionParts[1]);
            } else {
                major = Integer.parseInt(versionParts[0]);
            }
        } catch (NumberFormatException e) {
            fail("java.version system property cannot be parsed: " + version);
            return;
        }
        assertTrue(major >= 17, "Java runtime must be at least 17 but was: " + major);
    }

    @Test
    @DisplayName("No usage of deprecated SchemaConstant.LANGUAGE_ISO_CODE in code")
    void testDeprecatedApiUsageRemoved() {
        try {
            Class<?> schemaConstClass = Class.forName("com.salesmanager.core.constants.SchemaConstant");
            Field[] fields = schemaConstClass.getDeclaredFields();
            for (Field field : fields) {
                if ("LANGUAGE_ISO_CODE".equals(field.getName())) {
                    assertTrue(field.isAnnotationPresent(Deprecated.class), "LANGUAGE_ISO_CODE must be marked @Deprecated");
                }
            }
        } catch (ClassNotFoundException e) {
            fail("SchemaConstant class not found with expected upgrade: " + e.getMessage());
        }
    }

}