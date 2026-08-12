import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Java17MigrationHelper {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: Java17MigrationHelper <project-root-dir>");
            System.exit(1);
        }
        Path projectRoot = Paths.get(args[0]);
        if (!Files.exists(projectRoot)) {
            System.err.println("Project root not found: " + projectRoot);
            System.exit(2);
        }
        migrateJenkinsfiles(projectRoot);
        migratePomFiles(projectRoot);
        migrateDockerfiles(projectRoot);
        System.out.println("Java 17 migration automation complete. Review TODO comments for necessary manual steps.");
    }

    // --- Jenkinsfile, update agent image and tools
    private static void migrateJenkinsfiles(Path root) throws IOException {
        Files.walk(root)
            .filter(p -> p.getFileName().toString().equals("Jenkinsfile"))
            .forEach(javafile -> {
                try {
                    String content = new String(Files.readAllBytes(javafile));
                    String updated = migrateJenkinsfileContent(content);
                    if (!content.equals(updated)) {
                        Files.write(javafile, updated.getBytes());
                        System.out.println("Updated Jenkinsfile: " + javafile);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private static String migrateJenkinsfileContent(String content) {
        // Replace explicit Java 11 agent labels to Java 17 (or 21), preserving any unrelated labels
        content = content.replaceAll("label 'MVN3'", "label 'MVN17' // TODO: Manually confirm Jenkins Java 17 node label");
        content = content.replaceAll("label 'OPENJDK-11-JDK'", "label 'OPENJDK-17-JDK' // TODO: Manually update Jenkins agent label to Java 17+ node");
        // TODO: Manually update any custom build steps or tool install steps to use Java 17+ binary paths if needed
        return content;
    }

    // --- pom.xml update Java version, parent, dependencies
    private static void migratePomFiles(Path root) throws IOException {
        Files.walk(root)
            .filter(p -> p.getFileName().toString().equals("pom.xml"))
            .forEach(pom -> {
                try {
                    String input = new String(Files.readAllBytes(pom));
                    String migrated = migratePomContent(input, pom);
                    if (!input.equals(migrated)) {
                        Files.write(pom, migrated.getBytes());
                        System.out.println("Upgraded: " + pom);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private static String migratePomContent(String input, Path pomPath) {
        String output = input;

        // Upgrade parent Spring Boot version if present
        output = output.replaceAll(
            "(<parent>\\s*<groupId>org\\.springframework\\.boot</groupId>\\s*<artifactId>spring-boot-starter-parent</artifactId>\\s*<version)(>)(2\\.5\\.12)(</version>)",
            "$1$2" + "3.2.6" + "$4"
        );

        // Update spring boot dependencies in BOM section if present
        output = output.replaceAll(
            "(<dependency>\\s*<groupId>org\\.springframework\\.boot</groupId>\\s*<artifactId>spring-boot-starter-web</artifactId>(.|\\R)*?<\\/dependency>)",
            "<dependency>\n\t\t\t\t<groupId>org.springframework.boot</groupId>\n\t\t\t\t<artifactId>spring-boot-starter-web</artifactId>\n\t\t\t</dependency>"
        );
        // TODO: Review and update any other spring-* artifact versions for compatibility with Boot 3.2.6

        // Set java.version property to 17 if 11 (or lower)
        output = output.replaceAll(
            "<java.version>11</java.version>", "<java.version>17</java.version>"
        );
        output = output.replaceAll(
            "<maven.compiler.source>\\$\\{java.version\\}</maven.compiler.source>", "<maven.compiler.source>${java.version}</maven.compiler.source>"
        );
        output = output.replaceAll(
            "<maven.compiler.target>\\$\\{java.version\\}</maven.compiler.target>", "<maven.compiler.target>${java.version}</maven.compiler.target>"
        );

        // Remove explicit javax.* dependency usages in favor of jakarta.* for APIs impacted (Spring Boot 3+, Spring 6+)
        // Only update if <groupId>javax.*</groupId> is detected, NOT entries for actual legacy APIs in core modules
        output = output.replaceAll(
            "<groupId>javax\\.annotation</groupId>(\\s*)<artifactId>javax\\.annotation-api</artifactId>",
            "<groupId>jakarta.annotation</groupId>$1<artifactId>jakarta.annotation-api</artifactId> <!-- TODO: Manual review: update import statements to jakarta.* as needed -->"
        );
        output = output.replaceAll(
            "<groupId>javax.validation</groupId>(\\s*)<artifactId>validation-api</artifactId>",
            "<groupId>jakarta.validation</groupId>$1<artifactId>jakarta.validation-api</artifactId> <!-- TODO: Manual review: update import statements to jakarta.* as needed -->"
        );
        // TODO: Manual intervention required for all javax.* → jakarta.* migration in code.
        // (e.g., javax.servlet, javax.persistence if updating to later Hibernate/JPA).

        // Remove Springfox Swagger 2 dependencies and suggest OpenAPI alternative
        output = output.replaceAll(
            "(?s)<dependency>\\s*<groupId>io\\.springfox</groupId>\\s*<artifactId>springfox-swagger2</artifactId>\\s*(<version>[^<]*</version>)?\\s*</dependency>",
            "<!-- TODO: Replace 'springfox-swagger2' with springdoc-openapi (https://springdoc.org/) -->"
        );
        output = output.replaceAll(
            "(?s)<dependency>\\s*<groupId>io\\.springfox</groupId>\\s*<artifactId>springfox-swagger-ui</artifactId>\\s*(<version>[^<]*</version>)?\\s*</dependency>",
            "<!-- TODO: Replace 'springfox-swagger-ui' with springdoc-openapi-ui (https://springdoc.org/) -->"
        );
        // Same for non-versioned dependencies (e.g., in sm-shop-model/pom.xml)
        output = output.replaceAll(
            "(?s)<dependency>\\s*<groupId>io\\.springfox</groupId>\\s*<artifactId>springfox-swagger2</artifactId>\\s*</dependency>",
            "<!-- TODO: Replace 'springfox-swagger2' with springdoc-openapi (https://springdoc.org/) -->"
        );

        // TODO: Manually update any legacy Springfox annotations or config in source to OpenAPI/springdoc equivalents
        // (see https://springdoc.org/#migrating-from-springfox)

        // Update <plugin> maven-compiler-plugin <source>/<target> to 17 if present
        output = output.replaceAll(
            "(<plugin>\\s*<groupId>org\\.apache\\.maven\\.plugins</groupId>\\s*<artifactId>maven-compiler-plugin</artifactId>(.|\\R)*?<source>)11(</source>)(.|\\R)*?<target>)11(</target>)",
            "$1" + "17" + "$3$4$5" + "17" + "$6"
        );

        // Flag any javax.* dependencies for manual migration attention
        output = output.replaceAll(
            "<groupId>javax\\.", "<!-- TODO: Migrate javax.* dependencies to jakarta.* per Spring Boot 3.x+ guidance. -->\n<groupId>javax."
        );

        // Update vulnerable dependencies (advisory only, does not bump versions)
        // TODO: Review and upgrade: commons-collections, httpclient, guava, infinispan, jackson-databind
        output = output.replaceAll(
            "(<dependency>\\s*<groupId>(org\\.apache\\.commons|commons-collections|org.apache.httpcomponents|com.google.guava|com.fasterxml.jackson.core|org.infinispan|com.fasterxml.jackson.module|com.fasterxml.jackson.databind|com.fasterxml.jackson)</groupId>(.|\\R)*?</dependency>)",
            "$1<!-- TODO: Review for vulnerabilities and update to secure versions as needed. -->"
        );

        // TODO: Project config format may need to be reviewed (see migrationConfigFormat below)
        output += "\n<!-- TODO: Manually review all config/application properties for Spring Boot 3.x compatibility (see migrationConfigFormat()) -->\n";

        return output;
    }

    // --- Dockerfile
    private static void migrateDockerfiles(Path root) throws IOException {
        Files.walk(root)
            .filter(p -> p.getFileName().toString().toLowerCase().equals("dockerfile"))
            .forEach(dockerfile -> {
                try {
                    String orig = new String(Files.readAllBytes(dockerfile));
                    String migrated = migrateDockerfileContent(orig);
                    if (!orig.equals(migrated)) {
                        Files.write(dockerfile, migrated.getBytes());
                        System.out.println("Upgraded Dockerfile: " + dockerfile);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }
    private static String migrateDockerfileContent(String orig) {
        // Replace Java 11 base image with Java 17 equivalent.
        String updated = orig.replaceAll(
            "FROM adoptopenjdk/openjdk11-openj9:alpine",
            "FROM eclipse-temurin:17-jre-alpine\n# TODO: Verify Java 17 image in target registry. (https://hub.docker.com/_/eclipse-temurin)"
        );
        // TODO: Review and update any explicit JAVA_HOME or Java tool paths in image config/scripts for Java 17+
        return updated;
    }

    // --- Config format migration (Spring Boot 3.x)
    // This is an advisory function, does not transform files automatically, but can be extended
    public static Properties migrationConfigFormat(Properties oldConfig) {
        Properties newConfig = new Properties();
        for (Map.Entry<Object, Object> entry : oldConfig.entrySet()) {
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            // Example: spring.mvc.throw-exception-if-no-handler-found is now spring.web.throw-exception-if-no-handler-found
            if (k.startsWith("spring.mvc.")) {
                k = k.replace("spring.mvc.", "spring.web.");
                // TODO: Review property 'spring.mvc.*' → 'spring.web.*' mapping per Spring Boot 3.x migration guide
            }
            // Example: server.servlet.context-parameters → server.servlet.context-parameters (unchanged)
            newConfig.setProperty(k, v);
        }
        // TODO: Manually review for other config key renames in Spring Boot 3.x properties/yaml (see https://docs.spring.io/spring-boot/docs/3.2.6/reference/html/application-properties.html)
        return newConfig;
    }

    // --- Re-export/compatibility for deprecated API (none found in provided codebase, but placeholder below)
    // Example: wrap old API backed by new implementation if needed
    // TODO: Identify and wrap any deprecated/removed Spring APIs in application code using new equivalents.

    // --- Import/package shims (for Javax→Jakarta migration) ---
    // These need to be applied in Java code via IDE/search, cannot be done robustly in this helper due to type preservation.
    // TODO: Manually replace package imports: javax.* → jakarta.* in all Java files. Especially for:
    // - javax.annotation -> jakarta.annotation
    // - javax.validation -> jakarta.validation
    // - javax.persistence -> jakarta.persistence
    // - javax.servlet -> jakarta.servlet
    // Follow all Spring Boot 3.x migration guidelines.
}