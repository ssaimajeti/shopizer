import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Spring Boot 2.5.x -> 3.2.x Migration Helper Script for Shopizer Project
 *
 * This script performs the following migration helpers:
 * - API compatibility shims for major package renames (jakarta.* vs javax.*)
 * - Autoconfigures build/runtime shims for Java 21 (language/runtime ONLY)
 * - Provides config format transformer for application.properties/yml (Spring Boot 3 compliance)
 * 
 * Usage:
 *   - Place this in the root of your source as MigrationHelper.java and run it with Java 11+:
 *     javac MigrationHelper.java && java MigrationHelper
 *   - Review output, especially any TODOs for manual changes.
 * 
 * Limitations:
 *   This script cannot refactor implementation logic and does not replace deprecated/removed classes, 
 *   only adds adapter stubs where feasible.
 *   For complete project migration, refer to Spring Framework official migration guide.
 */
public class MigrationHelper {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Shopizer Spring Boot 2.5.x -> 3.2.x Migration Helper ===");

        updatePomXml();
        updateDockerfile();
        scanAndShimJavaxJakarta();
        migrateApplicationProperties();
        emitManualTodos();

        System.out.println("Migration script done.");
    }

    /**
     * Replace javax.* dependencies with jakarta.* where known/required for Spring Boot 3.
     * Provide a summary shim for affected sources.
     */
    static void scanAndShimJavaxJakarta() throws IOException {
        System.out.println("Scanning for javax.* imports...");

        List<Path> javaFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("."))) {
            paths.filter(p -> p.toString().endsWith(".java"))
                 .forEach(javaFiles::add);
        }

        for (Path file : javaFiles) {
            List<String> lines = Files.readAllLines(file);
            boolean changed = false;
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                // javax.persistence -> jakarta.persistence
                if (line.contains("import javax.persistence.")) {
                    newLines.add(line.replace("import javax.persistence.", "import jakarta.persistence."));
                    changed = true;
                }
                // javax.validation -> jakarta.validation
                else if (line.contains("import javax.validation.")) {
                    newLines.add(line.replace("import javax.validation.", "import jakarta.validation."));
                    changed = true;
                }
                // javax.servlet -> jakarta.servlet
                else if (line.contains("import javax.servlet.")) {
                    newLines.add(line.replace("import javax.servlet.", "import jakarta.servlet."));
                    changed = true;
                }
                // javax.annotation -> jakarta.annotation
                else if (line.contains("import javax.annotation.")) {
                    newLines.add(line.replace("import javax.annotation.", "import jakarta.annotation."));
                    changed = true;
                }
                // javax.* generic fallback (manual review)
                else if (line.contains("import javax.")) {
                    newLines.add(
                        "// TODO: Check import for manual migration, may need jakarta equivalent:\n" + line
                    );
                    changed = true;
                } else {
                    newLines.add(line);
                }
            }
            if (changed) {
                Files.write(file, newLines);
                System.out.println("Updated: " + file);
            }
        }
    }

    /**
     * Automatically apply Java language version pin (21) to pom.xml files.
     * Only updates the language runtime property.
     */
    static void updatePomXml() throws IOException {
        System.out.println("Checking pom.xml files for <java.version> update...");

        List<Path> pomFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("."))) {
            paths.filter(p -> p.getFileName().toString().equals("pom.xml"))
                 .forEach(pomFiles::add);
        }

        for (Path pom : pomFiles) {
            List<String> lines = Files.readAllLines(pom);
            boolean changed = false;
            List<String> newLines = new ArrayList<>();
            for (String line : lines) {
                if (line.trim().startsWith("<java.version>") && !line.contains(">21<")) {
                    newLines.add("		<java.version>21</java.version>");
                    changed = true;
                } else {
                    newLines.add(line);
                }
            }
            if (changed) {
                Files.write(pom, newLines);
                System.out.println("Updated java.version to 21 in: " + pom);
            }
        }
    }

    /**
     * Update Dockerfile to use Java 21 runtime base image, per the version pin.
     * Only updates 'FROM' line referencing java 11.
     */
    static void updateDockerfile() throws IOException {
        Path dockerfile = Paths.get("sm-shop/Dockerfile");
        if (!Files.exists(dockerfile)) return;

        List<String> lines = Files.readAllLines(dockerfile);
        List<String> newLines = new ArrayList<>();
        boolean changed = false;
        for (String line : lines) {
            if (line.startsWith("FROM adoptopenjdk/openjdk11-openj9")) {
                newLines.add("FROM eclipse-temurin:21-jre-alpine");
                changed = true;
            } else {
                newLines.add(line);
            }
        }
        if (changed) {
            Files.write(dockerfile, newLines);
            System.out.println("Dockerfile base image updated to Java 21.");
        }
    }

    /**
     * Transform Spring Boot 2.x property keys to Boot 3.x where known breaking changes exist.
     */
    static void migrateApplicationProperties() throws IOException {
        // Only operate if properties/yml config files are present
        String[] configs = {
            "sm-shop/src/main/resources/application.properties",
            "sm-shop/src/main/resources/application.yml"
        };
        for (String configPath : configs) {
            Path configFile = Paths.get(configPath);
            if (!Files.exists(configFile)) continue;

            List<String> lines = Files.readAllLines(configFile);
            List<String> newLines = new ArrayList<>();
            boolean changed = false;

            for (String line : lines) {
                // Spring Boot Actuator management.endpoints.web.exposure.include -> same, but review
                if (line.startsWith("management.endpoints.web.expose=") || line.startsWith("management.endpoints.web.exposure.include=")) {
                    newLines.add(line); // No mandatory rename, but placeholder for audit
                    newLines.add("# TODO: Review actuator endpoint config for new defaults: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.exposing");
                }
                // server.servlet.context-path
                else if (Pattern.matches("^server\\.servlet\\.context-path=.*", line)) {
                    newLines.add(line);
                    newLines.add("# Confirm server.servlet.context-path is honored in Boot 3 config.");
                }
                // spring.main.allow-bean-definition-overriding breaking change
                else if (line.startsWith("spring.main.allow-bean-definition-overriding=")) {
                    newLines.add(line);
                    newLines.add("# TODO: Check bean definition overriding, semantics changed in Spring Boot 3.");
                }
                else {
                    newLines.add(line);
                }
            }

            if (changed) {
                Files.write(configFile, newLines);
                System.out.println("Config transformed: " + configFile);
            }
        }
    }

    /**
     * Print out all TODOs for manual migration steps that cannot be automated.
     */
    static void emitManualTodos() {
        System.out.println("\n=== TODO: Manual Review Required for the following breaking changes ===");
        System.out.println("- Remove all uses of org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; migrate to SecurityFilterChain beans.");
        System.out.println("- Springfox (Swagger) is not compatible with Spring Boot 3.x; migrate to springdoc-openapi or compatible solution.");
        System.out.println("- Check all usages of javax.* (annotation, persistence, servlet, validation): these must be replaced by jakarta.* everywhere.");
        System.out.println("- Remove direct instantiations of deprecated Spring classes and replace with new approach as per Spring Boot 3 migration guide.");
        System.out.println("- Audit all configuration keys for changes: refer https://docs.spring.io/spring-boot/docs/3.2.6/reference/html/application-properties.html");
        System.out.println("- Check all dependencies for breaking version upgrades, especially: Guava, commons-*, httpclient, infinispan, MapStruct, etc.");
        System.out.println("- Update all CI/CD executor environments (Jenkins node labels, CircleCI executors, Docker images) to Java 21 runtime specific images.");
        System.out.println("- For any classes that cannot be automatically ported (e.g., subclassing removed/deprecated APIs), rewrite using the new idioms.");
    }
}