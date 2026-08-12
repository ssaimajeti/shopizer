# Upgrade Tasks (references traceable CAST Appendix items, or explicit absence)

1. **Confirm Java version target with owner** (Java 17 or 21).
2. **Update Java runtime in build and deployment environments**
    - Update configuration for all build and runtime servers to use the selected Java version.
    - If using Maven Wrapper: locate and edit all detected `maven-wrapper.properties` files ([see Appendix/Research]).
    - If using Gradle, or other build tools: search again for corresponding files upon future codebase updates—none found in CAST at this time.
3. **Amend Build Tool Configuration (⚠️ proposal):**
    - For Maven, update `pom.xml` and plugins to require correct `maven-compiler-plugin` `release` or `source/target` version. **Not available in CAST MCP—[query attempted]**
    - For Gradle, update `build.gradle` and `gradle-wrapper.properties`. **Not available in CAST MCP—[query attempted]**
    - For unspecified files (Dockerfile, pipeline, CI): warn that not available in CAST MCP—[query attempted]**
4. **Update Java property files as required**
    - Scan all surfaced `.properties` for hard-coded Java versions or relevant JVM options ([see Research appendix], e.g. `application.properties`, `maven-wrapper.properties`).
5. **Test build and deploy with updated Java version** across all target environments.
6. **Coordinate rollout with SME responsible for deployment infrastructure** as no Docker, YAML, or CI config files were discoverable.
7. **Document all changes and verification tests** for audit and compliance purposes.
