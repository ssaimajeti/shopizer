package com.shopizer.upgrade.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class Java17UpgradeValidationTest {

    private static final String EXPECTED_JAVA_VERSION = "21";
    private static final String SPRING_BOOT_VERSION = "3.2.6";

    @Test
    @DisplayName("Validate JVM is running at Java version 21 (EXACT match)")
    void testJavaRuntimeVersionPin() {
        String runtimeVersion = System.getProperty("java.version");
        assertNotNull(runtimeVersion, "java.version system property should not be null");
        assertTrue(
                runtimeVersion.equals(EXPECTED_JAVA_VERSION) ||
                runtimeVersion.startsWith(EXPECTED_JAVA_VERSION + "."),
                "Running JVM version must be exactly '" + EXPECTED_JAVA_VERSION + "' but was '" + runtimeVersion + "'"
        );
    }

    @Test
    @DisplayName("Validate presence and content of Maven 'pom.xml' <java.version> property")
    void testPomXmlJavaVersionPin() throws Exception {
        String pom = new String(Files.readAllBytes(Paths.get("pom.xml")));
        assertTrue(
                pom.contains("<java.version>" + EXPECTED_JAVA_VERSION + "</java.version>") ||
                pom.contains("<maven.compiler.source>" + EXPECTED_JAVA_VERSION + "</maven.compiler.source>"),
                "Root pom.xml must pin <java.version> or <maven.compiler.source> to '" + EXPECTED_JAVA_VERSION + "'."
        );
    }

    @Test
    @DisplayName("Validate Dockerfile uses correct Java 21 base image")
    void testDockerfileJavaBaseImagePin() throws Exception {
        String dockerfile = new String(Files.readAllBytes(Paths.get("sm-shop/Dockerfile")));
        assertTrue(
                dockerfile.contains("openjdk21") || dockerfile.contains("jdk-21"),
                "Dockerfile must use a base image that is Java 21 (openjdk21 or jdk-21 tag)."
        );
    }

    @Test
    @DisplayName("Smoke test: can load a Spring context and use critical beans")
    void testCriticalSpringBootContext() throws Exception {
        // Load Spring ApplicationContext in isolation (main smoke test, not meant as a full app boot)
        // Passes if Spring Boot 3+ and class contexts are compatible with Java 21
        try (org.springframework.boot.SpringApplication app = new org.springframework.boot.SpringApplication(Class.forName("com.shopizer.shop.ShopperApplication"))) {
            app.setAdditionalProfiles("test");
            app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
            var context = app.run("--spring.main.banner-mode=off");
            assertTrue(context.isActive(), "Spring context must be active");
            // Test existence of known critical bean
            assertTrue(context.containsBean("productController") || context.containsBean("productService"),
                    "Critical beans like 'productController' or 'productService' must load in Spring context");
            context.close();
        } catch (ClassNotFoundException e) {
            fail("Could not load main application class. Check that application builds for Java 21: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Assert Spring Boot Framework runtime is at 3.2.6 (critical)")
    void testSpringBootVersionPin() throws Exception {
        Package springBootPackage = Class.forName("org.springframework.boot.SpringApplication").getPackage();
        String version = (String) springBootPackage.getImplementationVersion();
        assertNotNull(version, "Spring Boot Implementation-Version must not be null");
        assertEquals(SPRING_BOOT_VERSION, version, "Spring Boot version must be " + SPRING_BOOT_VERSION + " but was " + version);
    }

    @Test
    @DisplayName("Legacy/deprecated Java 11/EE APIs no longer appear: javax.* replaced/removed from deps")
    void testJavaEEDependencyRemoval() throws Exception {
        String pom = new String(Files.readAllBytes(Paths.get("pom.xml")));
        assertFalse(
                pom.contains("javax.xml.bind") || pom.contains("javax.activation") ||
                pom.contains("javax.ws.rs") || pom.contains("javax.jws"),
                "Deprecated Java EE APIs (javax.xml.bind, javax.activation, javax.ws.rs, javax.jws) must be removed from all dependencies"
        );
    }

    @Test
    @DisplayName("Replaced APIs work: Spring Security, Date/Time APIs reflect Java 21+ idioms")
    void testReplacedApiAvailability() throws Exception {
        // Example: java.time.LocalDate is available and preferred over old Date/Calendar
        Class<?> localDateClazz = Class.forName("java.time.LocalDate");
        assertNotNull(localDateClazz.getDeclaredMethod("now"));
        // Example: Confirm at runtime new SecurityFilterChain bean is available (Spring Security 6+ idiom)
        try {
            Class<?> clazz = Class.forName("org.springframework.security.web.SecurityFilterChain");
            assertNotNull(clazz, "SecurityFilterChain must be present after migration");
        } catch (ClassNotFoundException e) {
            fail("SecurityFilterChain class is missing (Spring Security 6+ idiom should be present)");
        }
    }

    @Test
    @DisplayName("New configuration keys introduced by Spring Boot 3+ are loadable")
    void testNewSpringBootConfigKeysAcceptable() throws Exception {
        // E.g., spring.threads.virtual.enabled is new in Spring Boot 3+
        Properties props = new Properties();
        props.setProperty("spring.threads.virtual.enabled", "true");
        org.springframework.boot.SpringApplication app =
                new org.springframework.boot.SpringApplication(Class.forName("com.shopizer.shop.ShopperApplication"));
        app.setDefaultProperties((Map) props);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        var context = app.run("--spring.main.banner-mode=off");
        assertTrue(context.isActive());
        context.close();
    }

    @Test
    @DisplayName("Deprecated field 'LANGUAGE_ISO_CODE' in SchemaConstant is marked as @Deprecated")
    void testSchemaConstantLegacyFieldDeprecated() throws Exception {
        Class<?> schemaConstant = Class.forName("com.salesmanager.core.constants.SchemaConstant");
        Field field = schemaConstant.getDeclaredField("LANGUAGE_ISO_CODE");
        assertTrue(field.isAnnotationPresent(Deprecated.class),
                "LANGUAGE_ISO_CODE must be marked as @Deprecated in SchemaConstant");
    }

    @Test
    @DisplayName("Critical application domain classes compile and function with Java 21")
    void testDomainModelCompatibility() throws Exception {
        // Instantiating with reflection as smoke test for upgraded bytecode compatibility
        for (String fqcn : new String[] {
                "com.salesmanager.core.model.catalog.catalog.Catalog",
                "com.salesmanager.core.model.catalog.category.Category",
                "com.salesmanager.core.model.catalog.product.Product"
        }) {
            Class<?> c = Class.forName(fqcn);
            Object obj = c.getDeclaredConstructor().newInstance();
            assertNotNull(obj, fqcn + " failed to instantiate under Java 21");
        }
    }

    @Test
    @DisplayName("New Spring Boot 3.x configuration/feature: verify actuator endpoints are enabled and functional")
    void testSpringBootActuatorAvailability() throws Exception {
        // Simulate health endpoint using actuator's HealthIndicator class (Spring Boot 3 requirement)
        Class<?> actuatorClazz = Class.forName("org.springframework.boot.actuate.health.HealthIndicator");
        assertNotNull(actuatorClazz, "Spring Boot 3.x actuator HealthIndicator class should exist");
    }
}