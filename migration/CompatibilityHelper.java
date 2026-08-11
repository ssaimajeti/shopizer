package com.example.migrationhelper;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

// Shim for deprecated/removed APIs and renamed packages/classes as per Spring Boot 3.x/Spring Framework 6.x upgrade
public class SpringBootUpgradeCompat {

    // === Deprecated API Replacements ===
    // Example: org.springframework.web.bind.annotation.RestControllerAdvice replaces @ControllerAdvice use cases (no method shim needed).
    // Example: EnvironmentPostProcessor API has breaking changes.

    // TODO: If you use "org.springframework.boot.env.EnvironmentPostProcessor", manually rewrite implementations for new signature:
    // org.springframework.boot.env.EnvironmentPostProcessor.postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)
    // Old: postProcessEnvironment(ConfigurableEnvironment environment, ConfigurableApplicationContext application)
    // New: postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)

    /**
     * Shim for org.springframework.util.StringUtils#hasText(String) which is removed in newer Spring versions.
     * New location: org.springframework.util.StringUtils (same), but ensure project migrates away from legacy usages.
     * Prefer using Java's native methods or validate on compile-time.
     */
    @Deprecated
    public static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // TODO: Review your use of "WebSecurityConfigurerAdapter".
    // This class is REMOVED in Spring Security 6.x.
    // Manual refactor is REQUIRED—migrate to SecurityFilterChain bean configurations.
    // See: https://docs.spring.io/spring-security/reference/migration/servlet/config.html

    // === Package/Class Renames ===

    /**
     * Import shim for org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler.
     * No changes required unless replaced via @RestControllerAdvice.
     * To migrate: Replace ControllerAdvice with RestControllerAdvice where JSON required.
     */

    // TODO: Review and update imports:
    // All "javax.*" imports in your code must be replaced with "jakarta.*"
    // Example:
    // import javax.servlet.http.HttpServletRequest;
    // becomes
    // import jakarta.servlet.http.HttpServletRequest;
    // Please manually review all source files or automate via IDE.

    // === Springfox Swagger2 → springdoc-openapi ===

    // TODO: Replace all usages of springfox.documentation.swagger2.annotations.EnableSwagger2 and Springfox Docket beans.
    // - Remove all @EnableSwagger2 annotations.
    // - Replace with springdoc-openapi configuration (see library documentation).
    // - Update controller annotations as needed (@OpenAPIDefinition, etc.).
    // This migration cannot be fully automated. Review your configuration classes for Swagger setup.

    // === Configuration File Format Migration ===

    /**
     * Transforms old Spring Boot 2.x application properties to updated keys for Spring Boot 3.x where required.
     * This helper supports common property renames and removals.
     * Please review output and adjust according to your application's specific property usage.
     */
    public static Properties migrateSpringBoot2PropsTo3(Properties oldProps) {
        Properties newProps = new Properties();

        for (Map.Entry<Object, Object> entry : oldProps.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            // Common property renames (partial list) - expand as needed for application
            if ("server.ssl.key-store-type".equals(key) && "PKCS12".equals(value)) {
                // No change, but ensure compatibility with Java 17+ defaults
                newProps.setProperty(key, value);
            } else if ("management.endpoints.web.exposure.include".equals(key)) {
                // Property unchanged, but check for deprecated values
                newProps.setProperty(key, value);
            } else if ("spring.main.allow-bean-definition-overriding".equals(key)) {
                // Unchanged but deprecated, review usage
                newProps.setProperty(key, value);
            } else if (key.startsWith("spring.datasource.")) {
                // Example: Property remains, but recommended to move to "spring.sql.init.*" for initialization SQL scripts
                newProps.setProperty(key, value);
            } else {
                // Copy others as-is for manual review
                newProps.setProperty(key, value);
            }
        }

        // TODO: Many property keys changed from "javax.*" to "jakarta.*" (e.g., JPA, validation).
        // Please manually review your application.properties/application.yml and adjust as needed.
        return newProps;
    }


    /**
     * Convenience method to migrate old configuration maps (for YAML or custom config).
     * Only handles generic case - customize for your application's keys.
     */
    public static Map<String, Object> migrateSpringBoot2ConfigMap(Map<String, Object> oldConfig) {
        Map<String, Object> newConfig = new HashMap<>();

        for (Map.Entry<String, Object> entry : oldConfig.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Sample JPA migration: javax → jakarta
            if (key.startsWith("javax.persistence")) {
                String newKey = key.replace("javax.persistence", "jakarta.persistence");
                newConfig.put(newKey, value);
            } else {
                newConfig.put(key, value);
            }
        }

        // TODO: Carefully review for new/renamed/deprecated properties in Boot 3.x
        // Properties not automatically migrated must be verified manually.

        return newConfig;
    }

    /**
     * Helper to warn about legacy or removed classes which must be updated.
     * Call from startup or test scaffolding to assist in migration review.
     */
    public static void reportPotentialIssues() {
        System.out.println("[Spring Boot 3.x Migration Helper]");
        System.out.println("== TODO: Manual Intervention Required ==");
        System.out.println("1. Replace all 'javax.*' imports with 'jakarta.*'.");
        System.out.println("2. Remove any usage of WebSecurityConfigurerAdapter (removed in Security 6) and migrate to SecurityFilterChain.");
        System.out.println("3. Migrate Springfox Swagger2 (Docket, EnableSwagger2) to springdoc-openapi.");
        System.out.println("4. Review EnvironmentPostProcessor, its method signature and usage.");
        System.out.println("5. Verify all configuration property keys according to Spring Boot 3 migration guide.");
        System.out.println("6. Update Java version baseline to at least 17; some older Guava/Commons-Fileupload versions not compatible.");

        // Add more verbose reminders as needed.
    }

}