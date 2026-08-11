### Specification: Java Runtime Upgrade

#### Scope
Upgrade the Java runtime version used in the build and deployment process to at least Java 17 (preferably Java 21 if compatible).

#### In-Scope:
- All CI build and deployment definitions (build scripts, Dockerfiles, configuration files, pipeline YAMLs, etc.)
- All runtime environment configurations requiring a specific JDK version.

#### Out-of-Scope:
- Refactoring of application code to use Java 17/21 new features (unless required for compatibility).
- Changes unrelated to Java runtime, CI, or deployment configuration.

#### Requirements
1. **Detect Current Java Version Usage:**
   - Identify files and configuration references responsible for setting the Java version (e.g., `pom.xml`, `build.gradle`, Dockerfiles, pipeline files, Java environment variables).
2. **Update Build Configurations:**
   - Where Java 11 is specified, change to Java 17 (or 21 if validated compatible).
   - Update Dockerfiles (base image/tag), build scripts, Maven/Gradle configuration, or CI/CD YAML as appropriate.
3. **Compatibility Validation:**
   - Identify areas of code with possible issues (deprecated or removed APIs in Java 17/21).
   - Document and notify if manual code changes are required for compatibility.
4. **Testing & Rollback Plan:**
   - Ensure test coverage exists for critical paths; plan for rollback if critical build/test failures occur.
5. **Documentation:**
   - Document all changes, locations of updated configuration, and any required instructions for developers/contributors.

#### Acceptance Criteria
- All pipeline and deployment environments use Java 17 or newer.
- Build, test, and deployment pass with the new Java version.
- No new runtime errors introduced related to version upgrade.
