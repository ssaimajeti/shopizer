/************************************************************
 * Shopizer Java 11 → 17/21 Compatibility Shim
 *  
 * This helper provides compatibility layers for API, import, 
 * and config changes required by the upgrade of Spring Boot 
 * (2.5.12 → 3.2.6), Spring Framework (→ 6.1.6), and 
 * Springfox Swagger (2.9.2 → 3.0.0, but deprecated).  
 * 
 * Place this in a shared source set (e.g. sm-core/src/main/java).
 * 
 * USAGE: 
 * - Replace usages of deprecated APIs with references from this shim.
 * - Wrap Swagger/OpenAPI, import shims, and migration utilities 
 *   as instructed in TODO comments.
 ************************************************************/

package com.shopizer.compat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

// ========== SPRING BOOT DEPRECATED API SHIMS ==========

/*
 * Example: Forward old WebSecurityConfigurerAdapter usage to SecurityFilterChain bean configuration,
 * as WebSecurityConfigurerAdapter is removed in Spring Boot 3/Spring Security 6.
 */
@Configuration
public class SpringSecurityCompatShim {
    // TODO: FORMERLY extended WebSecurityConfigurerAdapter.
    // Manual intervention required: Refactor security configuration to use SecurityFilterChain beans.
    // See Spring Boot 3.2.6 migration guide for details.

    // Example stub to avoid compilation errors in modules not yet migrated.
    @Bean
    @Primary
    public org.springframework.security.web.SecurityFilterChain dummySecurityFilterChain(
            org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        // TODO: Implement application-specific security filter chain.
        return http.build();
    }
}

// ========== SPRINGFOX SWAGGER (Swagger 2) => OPENAPI MIGRATION SHIM ==========

// TODO: Springfox (io.springfox:springfox-swagger2, springfox-swagger-ui) is deprecated and not compatible with Spring Boot 3.x/Spring 6.x.
// Manual intervention required: 
//  - Remove springfox-swagger2/springfox-swagger-ui dependencies from all pom.xml files.
//  - Migrate to springdoc-openapi (org.springdoc:springdoc-openapi-starter-webmvc-ui) in all API documentation configuration.
//  - All @EnableSwagger2 annotations and Docket beans MUST be replaced with springdoc-openapi equivalents.
//  - See: https://springdoc.org/#migrating-from-springfox

// Compatibility stub for @EnableSwagger2 annotation (NOOP to prevent build breaks before migration).
@Deprecated
public @interface EnableSwagger2 {
    // No-op annotation for backwards compatibility during migration.
}

// ========== IMPORT AND PACKAGE RENAME SHIMS ==========

// NOTE: Spring Boot 3.x/Spring 6.x migrated many javax.* imports to jakarta.*.
// This basic stub allows compiling legacy code until full migration.

// Example: javax.annotation.* → jakarta.annotation.*
package javax.annotation;
@Deprecated
public @interface Generated {
    String[] comments() default {};
    String[] date() default {};
    String[] value() default {};
}
// TODO: Replace all javax.annotation imports with jakarta.annotation equivalents in codebase.

// ========== CONFIG FORMAT MIGRATION ==========

/**
 * Transforms old Spring Boot 2.x application.properties/yml config to Spring Boot 3.2.6 format.
 * @param oldConfig String representing the old config file.
 * @return String transformed for Spring Boot 3.2.6+ compatibility.
 */
public class ConfigMigrationHelper {
    public static String migratePropertiesFormat(String oldConfig) {
        // Example: property name casing changes
        String migrated = oldConfig
                .replace("spring.main.allow-bean-definition-overriding", "spring.main.allow-bean-definition-overriding")
                .replace("jackson.serialization.write-dates-as-timestamps", "spring.jackson.serialization.write-dates-as-timestamps");
        // TODO: Review property changes as per https://docs.spring.io/spring-boot/docs/3.2.6/reference/html/application-properties.html
        //        and migrate/rename properties as needed.
        return migrated;
    }
}

// ========== VERSION PIN ENFORCEMENT (Shopizer 3.2.6) ==========

// TODO: Update all version numbers for Spring Boot and all submodules to '3.2.6' in every build/config/deployment environment.
//       - All Maven <version> tags for Spring Boot/Spring must use <version>3.2.6</version>.
//       - Dockerfiles, CI files, and build configs must pin to version 3.2.6 exactly.

// ========== MANUAL MIGRATION TASKS REQUIRED FOR FULL COMPATIBILITY ==========
/*
 * 1. Update all base images and Java runtime versions (Dockerfile, Jenkinsfile, CI configs) to Java 17 or 21 as required.
 *    - E.g., change FROM adoptopenjdk/openjdk11-openj9:alpine to a Java 17/21 base image in all Dockerfiles.
 *    - Update Jenkins pipeline agent labels and environment variables to use Java 17/21.
 * 2. Remove any usage of WebSecurityConfigurerAdapter. Refactor to SecurityFilterChain beans and component-based security configuration.
 * 3. Replace all usage of deprecated or incompatible APIs with their Spring 6.x/Spring Boot 3.x equivalents, especially:
 *    - javax.* (except for mail, validation): migrate to jakarta.*
 *    - @EnableSwagger2 and springfox Docket: move to springdoc-openapi.
 * 4. Ensure all configuration files are free of deprecated or renamed properties and conform to Spring Boot 3.2.6 property namespace.
 * 5. Remove any legacy references to 'javax.servlet', 'javax.validation', etc. in favor of 'jakarta.servlet', 'jakarta.validation', etc.
 * 6. Test all REST endpoints for backward compatibility and correct bean initialization under the upgraded frameworks.
 * 7. Update all dependency versions for vulnerable libraries (commons-collections, httpclient, guava, jackson-databind, infinispan).
 */

// END SHIM FILE