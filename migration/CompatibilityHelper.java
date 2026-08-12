import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class JavaSpringBootMigrationHelper {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java JavaSpringBootMigrationHelper <src-root> <dst-root>");
            System.exit(1);
        }
        Path sourceRoot = Paths.get(args[0]);
        Path destRoot = Paths.get(args[1]);
        migrateDirectory(sourceRoot, destRoot);
    }

    private static final Map<String, String> SPRING_X_TO_3_DEPRECATED_CLASSES = new HashMap<>();
    private static final List<PatternReplacement> DEPRECATED_API_REPLACEMENTS = new ArrayList<>();
    private static final List<PatternReplacement> PACKAGE_RENAMES = new ArrayList<>();

    static {
        // ==== Spring Boot 3.x breaking/deprecated changes (from 2.5.x) ====
        // Most javax.* moved to jakarta.*
        PACKAGE_RENAMES.addAll(Arrays.asList(
            new PatternReplacement(Pattern.compile("import javax\\.(\\w+\\.?)+;"), JavaSpringBootMigrationHelper::replaceJavaxWithJakarta),
            new PatternReplacement(Pattern.compile("javax\\."), m -> "jakarta."), // for annotations
            new PatternReplacement(Pattern.compile("import javax\\.validation\\.(.*);"), m -> "import jakarta.validation." + m.group(1) + ";"),
            new PatternReplacement(Pattern.compile("import javax\\.persistence\\.(.*);"), m -> "import jakarta.persistence." + m.group(1) + ";")
        ));

        // AuditableListener class example (to be manually migrated)
        // TODO manual review for Spring Data JPA listeners, if any custom code present

        // ==== Deprecated API replacements ====
        // Example: Deprecated Spring Security WebSecurityConfigurerAdapter
        DEPRECATED_API_REPLACEMENTS.add(
            new PatternReplacement(
                Pattern.compile("extends\\s+WebSecurityConfigurerAdapter"),
                m -> "// TODO: 'WebSecurityConfigurerAdapter' is removed in Spring Security 5.7+ (Spring Boot 3.x). Use a SecurityFilterChain @Bean configuration instead.\n// " + m.group() + " (please migrate manually)\n"
            )
        );

        // TODO: Manually review and migrate any usages of deprecated javax.xml.bind.* classes, switch to supported APIs or add external dependencies

        // TODO: Manual intervention may be required if using removed Springfox (Swagger 2.x) - consider using springdoc-openapi.
    }

    public static void migrateDirectory(Path src, Path dst) throws IOException {
        Files.walk(src).forEach(path -> {
            try {
                Path relative = src.relativize(path);
                Path target = dst.resolve(relative);
                if (Files.isDirectory(path)) {
                    Files.createDirectories(target);
                } else if (path.toString().endsWith(".java")) {
                    migrateJavaFile(path, target);
                } else if (path.getFileName().toString().equals("pom.xml")) {
                    migratePomFile(path, target);
                } else if (path.getFileName().toString().equals("Dockerfile")) {
                    migrateDockerfile(path, target);
                } else if (path.getFileName().toString().endsWith(".yml") || path.getFileName().toString().endsWith(".yaml")) {
                    migrateYamlConfig(path, target);
                } else {
                    Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void migrateJavaFile(Path src, Path dst) throws IOException {
        List<String> lines = Files.readAllLines(src);
        List<String> newLines = new ArrayList<>();
        for (String line : lines) {
            String replaced = line;
            // Package/class renames (javax.* → jakarta.*)
            for (PatternReplacement pr : PACKAGE_RENAMES) {
                replaced = pr.apply(replaced);
            }
            // Deprecated API replacements
            for (PatternReplacement pr : DEPRECATED_API_REPLACEMENTS) {
                replaced = pr.apply(replaced);
            }
            newLines.add(replaced);
        }
        // TODO: Manually update Spring Data @Query native queries if referencing renamed table or column names (unlikely in Shopizer domain models)
        Files.write(dst, newLines);
    }

    public static void migratePomFile(Path src, Path dst) throws IOException {
        List<String> lines = Files.readAllLines(src);
        List<String> newLines = new ArrayList<>();
        boolean springBootParentUpdated = false;
        boolean javaVersionUpdated = false;
        boolean springfoxFound = false;
        boolean inProperties = false;
        for (String l : lines) {
            String line = l;

            if (line.contains("<version>2.5.12</version>") && !springBootParentUpdated &&
                lines.stream().anyMatch(x -> x.contains("<artifactId>spring-boot-starter-parent</artifactId>"))) {
                newLines.add("        <version>3.2.6</version>");
                springBootParentUpdated = true;
                continue;
            }
            if (line.trim().startsWith("<java.version>")) {
                newLines.add("        <java.version>17</java.version>");
                javaVersionUpdated = true;
                continue;
            }
            // Pin version for submodules to 3.2.6 in their pom.xml
            if (line.trim().startsWith("<version>3.2.3</version>") && (
                lines.stream().anyMatch(x -> x.contains("<parent>") && x.contains("<artifactId>shopizer</artifactId>")))) {
                newLines.add(line.replace("3.2.3", "3.2.6"));
                continue;
            }
            // Springfox/Swagger detected? Flag for manual migration
            if (line.contains("springfox-swagger")) {
                springfoxFound = true;
                newLines.add("            <!-- TODO: Springfox Swagger 2.x is NOT compatible with Spring Boot 3.x/Jakarta EE. Migrate to springdoc-openapi. -->");
            }
            newLines.add(line);
            if (line.contains("<properties>")) inProperties = true;
            if (inProperties && line.contains("</properties>")) inProperties = false;
        }
        if (!springBootParentUpdated && lines.stream().anyMatch(x -> x.contains("<artifactId>spring-boot-starter-parent</artifactId>"))) {
            // If missed, patch somewhere after parent decl (edge, but needed for parent poms)
            for (int i = 0; i < newLines.size(); ++i) {
                if (newLines.get(i).contains("<artifactId>spring-boot-starter-parent</artifactId>")) {
                    newLines.add(i + 1, "        <version>3.2.6</version>");
                    break;
                }
            }
        }
        // TODO: Review all javax.* and Springfox dependencies for required migration
        if (springfoxFound) {
            newLines.add("    <!-- TODO: Remove all springfox-swagger2 and springfox-swagger-ui dependencies and migrate to springdoc-openapi for OpenAPI 3+ support. -->");
        }
        Files.write(dst, newLines);
    }

    public static void migrateDockerfile(Path src, Path dst) throws IOException {
        List<String> lines = Files.readAllLines(src);
        List<String> newLines = new ArrayList<>();
        boolean replacedBase = false;
        for (String line : lines) {
            String l = line;
            if (l.trim().startsWith("FROM adoptopenjdk/openjdk11")) {
                // Use a Java 17 base image
                l = "FROM eclipse-temurin:17-jre-alpine";
                replacedBase = true;
            }
            // Replace existing image tags '3.2.1', '3.2.3' etc. with '3.2.6'
            l = l.replaceAll("shopizer:3\\.2\\.\\d+", "shopizer:3.2.6");
            newLines.add(l);
        }
        if (!replacedBase) {
            newLines.add(0, "FROM eclipse-temurin:17-jre-alpine");
        }
        Files.write(dst, newLines);
    }

    public static void migrateYamlConfig(Path src, Path dst) throws IOException {
        // No explicit config format changes provided, but Spring Boot 3.x best practice: replace legacy property names as needed
        List<String> lines = Files.readAllLines(src);
        List<String> migrated = new ArrayList<>();
        for (String l : lines) {
            String line = l;
            // No known breaking property names in Shopizer out-of-box
            // If you use security.* or management.* property keys, consult Spring Boot 3.x migration docs!
            migrated.add(line);
        }
        // TODO: Manually review configs for deleted/deprecated properties (especially security, actuator, and SSL)
        Files.write(dst, migrated);
    }

    /**
     * Utility: javax.* to jakarta.* import shim.
     */
    public static String replaceJavaxWithJakarta(Matcher m) {
        String original = m.group();
        return original.replace("javax.", "jakarta.");
    }

    static class PatternReplacement {
        final Pattern pattern;
        final Replacer replacer;
        PatternReplacement(Pattern pattern, Replacer replacer) {
            this.pattern = pattern;
            this.replacer = replacer;
        }
        String apply(String input) {
            Matcher m = pattern.matcher(input);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, Matcher.quoteReplacement(replacer.replace(m)));
            }
            m.appendTail(sb);
            return sb.toString();
        }
    }
    interface Replacer {
        String replace(Matcher m);
    }

    // ================== CONFIG MIGRATION FUNCTION ==================
    /**
     * Usage: Migrate old config property format to new, if app.properties or bootstrap.yaml is using legacy keys.
     *
     * @param oldConfig Map of config keys/values in old format
     * @return migrated Map in new format (where required)
     */
    public static Map<String, String> migrateConfigProperties(Map<String, String> oldConfig) {
        Map<String, String> out = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : oldConfig.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            // Example: management.endpoints.web.base-path → management.endpoints.web.base-path (no change)
            // Security config: Use new names if migrated Spring Security config present.
            if (k.equals("server.servlet.context-path")) {
                out.put(k, v); // Unchanged
            }
            // TODO: Update keys as per https://docs.spring.io/spring-boot/docs/3.2.6/reference/html/application-properties.html
            else {
                out.put(k, v);
            }
        }
        // TODO: manual review all property keys for breaking changes in Spring Boot 3.x
        return out;
    }

    // ================== ADDITIONAL BREAKING CHANGES TO BE ADDRESSED MANUALLY ==================
    // - All javax.* imports in source code must be manually reviewed for any references not remapped.
    // - All usages of Springfox (Swagger 2.x) must be replaced by springdoc-openapi. No API-level auto-shim is provided.
    // - If using WebSecurityConfigurerAdapter, migrate the security config as per Spring Security 5.7+ docs.
    // - Any custom Spring Data JPA converters/listeners may require signature updates for Jakarta Persistence.
    // - If you use Spring Boot Actuator endpoints, check your management.* configs (see Spring Boot 3.x migration guide).

    // ================== END ==================
}