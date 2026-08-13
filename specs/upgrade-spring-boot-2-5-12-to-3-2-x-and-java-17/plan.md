# Migration Plan

1. **Preparatory Steps**
   - Inventory all build system and dependency files (pom.xml, build.gradle, Dockerfiles).
   - Confirm presence of Java 17 runtime in all dev/test/prod environments.

2. **Dependency Upgrades**
   - Update (or generate) Maven or Gradle build files to set Java 17 and Spring Boot 3.2.x.
   - Upgrade all third-party dependencies to supported versions for Java 17 and Spring Boot 3.x.
   - Run SBOM scan post-upgrade to ensure no critical vulnerabilities (manual, as tool returned empty).

3. **Code Refactoring**
   - Identify and update any deprecated/removed Spring APIs or illegal patterns (controller/service/facade/model).
   - Refactor complex/popular modules first: e.g., `CustomerFacadeImpl.java`, populator classes, mappers.

4. **Infrastructure Adjustments**
   - Update Dockerfile(s), any shell scripts, and IaC referencing Java images to use Java 17.
   - Review and update CI/CD config as needed.

5. **Validation**
   - Run test suite end-to-end.
   - Manual verification of critical flows (controller → service → repository).

6. **Documentation**
   - Write and commit an ADR documenting the rationale and process.
   - Update project README and developer onboarding docs as needed.
