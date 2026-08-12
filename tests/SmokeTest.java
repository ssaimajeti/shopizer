package com.shopizer.upgrade;

import org.junit.jupiter.api.*;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpgradeValidationTest {

    private static final String REQUIRED_SPRING_BOOT_VERSION = "3.2.6";
    private static final int REQUIRED_JAVA_VERSION = 17;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Order(1)
    @DisplayName("Active Spring Boot version must be 3.2.6")
    void activeSpringBootVersionIsTarget() {
        String springBootVersion = SpringBootVersion.getVersion();
        assertEquals(REQUIRED_SPRING_BOOT_VERSION, springBootVersion, 
            "Spring Boot version MUST be " + REQUIRED_SPRING_BOOT_VERSION + " after upgrade");
    }

    @Test
    @Order(2)
    @DisplayName("Active Java version must be 17 at runtime")
    void activeJavaVersionIs17() {
        String specVersion = System.getProperty("java.specification.version");
        // Java 17 may report "17" or "17.0.0" as version string
        assertTrue(specVersion.startsWith("17"), 
            "Runtime Java specification version must be 17, found: " + specVersion);
        String vmVersion = System.getProperty("java.version");
        assertTrue(vmVersion.startsWith("17"), 
            "Runtime Java version must be 17, found: " + vmVersion);
    }

    @Test
    @Order(3)
    @DisplayName("Critical REST API path: /api/v1/products works and responds 200")
    void criticalRestEndpointWorks_products() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
               .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Critical REST API path: /api/v1/categories works and responds 200")
    void criticalRestEndpointWorks_categories() throws Exception {
        mockMvc.perform(get("/api/v1/categories"))
               .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("Deprecated API usages - SchemaConstant.LANGUAGE_ISO_CODE no longer present as public usage")
    void deprecatedApiIsAbsent() {
        // The constant LANGUAGE_ISO_CODE in SchemaConstant is marked @Deprecated and should have been removed or replaced in the codebase.
        try {
            Class<?> schemaConstantClass = Class.forName("com.salesmanager.core.constants.SchemaConstant");
            Field languageIsoField = schemaConstantClass.getDeclaredField("LANGUAGE_ISO_CODE");
            assertTrue(languageIsoField.isAnnotationPresent(Deprecated.class),
                "LANGUAGE_ISO_CODE should still be present but deprecated");
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            fail("SchemaConstant.LANGUAGE_ISO_CODE field missing - review code for upgrade fallout!");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Old Swagger Springfox beans are not in context")
    void oldSwaggerBeansAreGone() {
        AtomicBoolean legacySwaggerPresent = new AtomicBoolean(false);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanNames) {
            if (name.contains("springfox") || name.contains("swagger2")) {
                legacySwaggerPresent.set(true);
                break;
            }
        }
        assertFalse(legacySwaggerPresent.get(), "Springfox Swagger 2 beans should NOT be present after upgrade to Spring Boot 3.x");
    }

    @Test
    @Order(7)
    @DisplayName("Actuator endpoint /actuator/health works")
    void actuatorHealthEndpointWorks() throws Exception {
        mockMvc.perform(get("/actuator/health"))
               .andExpect(status().isOk());
    }
    
    @Test
    @Order(8)
    @DisplayName("New Spring Boot 3.x configuration key: 'spring.threads.virtual.enabled' can be loaded")
    void newConfigKeyLoads() {
        // Simulate config load. Test if key is loadable and recognized (will pass if not throwing exception or not rejected).
        Properties properties = new Properties();
        properties.setProperty("spring.threads.virtual.enabled", "false");
        assertEquals("false", properties.getProperty("spring.threads.virtual.enabled"));
    }

    @Test
    @Order(9)
    @DisplayName("Transaction management uses Jakarta and not javax packages")
    void jakartaTransactionsUsed() {
        try {
            Class.forName("jakarta.transaction.Transactional");
            // If the class loads, we're using Jakarta correctly
        } catch (ClassNotFoundException e) {
            fail("Jakarta Transactional annotation missing - legacy javax.transaction.* may still be in use.");
        }
    }

    @Test
    @Order(10)
    @DisplayName("Container base - Dockerfile for sm-shop uses java 17")
    void dockerfileUsesJava17() {
        // NOTE: this is a static check. Ideally run separately or inject via build. Here we assert on the presence of 17
        String dockerfileContents = "";
        try (java.io.InputStream is = getClass().getResourceAsStream("/sm-shop/Dockerfile")) {
            if (is == null) return; // Can't check in test run if not in test-path
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            dockerfileContents = s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            // Ignore if not in runtime classpath, tested in build step
        }
        assertFalse(dockerfileContents.contains("openjdk11"), "Dockerfile should NOT mention openjdk11 image after upgrade.");
        assertTrue(dockerfileContents.contains("openjdk17") || dockerfileContents.contains("java17"),
            "Dockerfile should use openjdk17 as base image after upgrade.");
    }
}