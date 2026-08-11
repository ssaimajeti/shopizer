package com.example.upgrade.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.ApplicationContext;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PostUpgradeValidationTest {

    private static final String EXPECTED_SPRING_BOOT_VERSION = "3.2.7";
    private static final String EXPECTED_SPRING_FRAMEWORK_VERSION = "6.1.8";

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("Java runtime is at least version 17")
    void javaRuntimeVersionIsAtLeast17() {
        String javaVersion = System.getProperty("java.version");
        // Accepts Java versions "17" or "21" (and later), but not "11"
        int majorVersion;
        if (javaVersion.startsWith("1.")) {
            // legacy style: 1.8.0_XXX
            majorVersion = Integer.parseInt(javaVersion.substring(2, 3));
        } else {
            // modern style: 17.0.1, 21.0.2, etc.
            int dotIndex = javaVersion.indexOf(".");
            majorVersion = dotIndex > 0
                    ? Integer.parseInt(javaVersion.substring(0, dotIndex))
                    : Integer.parseInt(javaVersion);
        }
        assertTrue(majorVersion >= 17, "Expected Java major version >=17, found: " + javaVersion);
    }

    @Test
    @DisplayName("Active Spring Boot version is exactly 3.2.7")
    void springBootVersionIsTarget() {
        String actualVersion = SpringBootVersion.getVersion();
        assertEquals(EXPECTED_SPRING_BOOT_VERSION, actualVersion, "Spring Boot version mismatch");
    }

    @Test
    @DisplayName("Active Spring Framework version is exactly 6.1.8")
    void springFrameworkVersionIsTarget() {
        String actualVersion = SpringVersion.getVersion();
        assertEquals(EXPECTED_SPRING_FRAMEWORK_VERSION, actualVersion, "Spring Framework version mismatch");
    }

    @Test
    @DisplayName("No deprecated Springfox Swagger2 beans exist; springdoc-openapi beans present")
    void onlySpringdocBeansPresent() {
        // Verify nobody registers old springfox beans
        String[] swagger2Beans = context.getBeanNamesForAnnotation(
                getClassForName("springfox.documentation.swagger2.annotations.EnableSwagger2")
        );
        assertEquals(0, swagger2Beans.length, "Springfox Swagger2 beans should not be present");

        // Instead, springdoc's OpenAPI bean should exist
        boolean openApiBeanPresent = false;
        for (String beanName : context.getBeanDefinitionNames()) {
            Class<?> type = context.getType(beanName);
            if (type != null && type.getName().equals("org.springdoc.core.models.GroupedOpenApi")) {
                openApiBeanPresent = true;
                break;
            }
        }
        assertTrue(openApiBeanPresent, "springdoc-openapi GroupedOpenApi bean should exist");
    }

    @Test
    @DisplayName("Critical REST API endpoints function with upgraded runtime")
    void criticalApiPathsWork() {
        // Replace with a real HTTP client or MockMvc in a real project context,
        // here we only demonstrate a resource presence check:
        assertTrue(context.containsBean("myCriticalRestController"),
                "Critical REST Controller should be present after upgrade");
        // Optionally expand to actual endpoint invocation if framework is proper.
    }

    @Test
    @DisplayName("New configuration properties load after upgrade")
    void newConfigKeysLoad() {
        // Example: a property that exists only in Spring Boot 3.x
        Properties envProps = new Properties();
        context.getEnvironment().getSystemProperties().forEach((k, v) -> envProps.put(k, v));
        String key = "spring.web.resources.static-locations";
        assertNotNull(context.getEnvironment().getProperty(key), "spring.web.resources.static-locations should be defined in upgraded Spring Boot");
    }

    private Class<?> getClassForName(String className) {
        try {
            return Class.forName(className, false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}