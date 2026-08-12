### CAST MCP Findings for Shopizer-3.2.5

#### ✅ Direct CAST Results

- The Shopizer-3.2.5 application is present and analyzed in CAST MCP. (See Query Log #1)
- Shopizer-3.2.5 is a Java application using multiple Maven modules, confirmed by the presence of these files:
    - `shopizer-3.2.5/pom.xml`
    - `shopizer-3.2.5/sm-shop-model/pom.xml`
    - `shopizer-3.2.5/sm-core-modules/pom.xml`
    - `shopizer-3.2.5/sm-core-model/pom.xml`
    - `shopizer-3.2.5/sm-core/pom.xml`
    - `shopizer-3.2.5/sm-shop/pom.xml`
  (Query Log #5,6,7,8,9,10)
- The only build-related file types found in CAST are Maven pom.xml files. No Gradle, Ant, or other build system files discovered. (Query Log #5-14)
- No Dockerfile, Jenkinsfile, GitHub Actions workflow, GitLab, Azure, CircleCI, or other CI/CD configuration files detected in CAST. (Query Log #23-33)
- No direct evidence of Java version setting (such as `maven.compiler.source`, `java.version`, or `sourceCompatibility` properties) appears in the CAST-modeled properties files for Shopizer-3.2.5. (Query Log #44-53)
- The application includes Java property files (`application.properties`, `application-test.properties`). These contain logging and Spring/Hibernate settings but no settings for Java version, Maven build plugin, or JDK selection. (Query Log #41,46,48)
- The Maven poms are recognized, but their internal content (such as plugin configurations or properties) is not available in CAST MCP. (Query Log #12-19)
- Shopizer-3.2.5 is based primarily on: Java, Spring/Spring MVC, Java EE, Hibernate, JPA, AWS SDK, Google Cloud Storage SDK. (Query Log #3)
- No package artifact (i.e., no published deployable, Docker image, or artifact repository reference) is modeled in the CAST application snapshot. (Query Log #4)
- No SCM-provided or build pipeline-provided runtime environment marker, e.g., build agent Java version or deployment platform, is visible to CAST. (Query Log #23-33)

#### ⚠️ Structurally Inferred (requires SME validation)

- ⚠️ Shopizer-3.2.5 uses Maven as its build system throughout; all Java compilation and packaging is likely orchestrated via Maven. (Maven poms discovered in all code submodules; Query Log #5-10)
- ⚠️ The absence of alternate build system files and CI/CD definitions indicates default Maven (`mvn`) local builds; runtime version is likely determined by build agent/container and root pom.xml configuration.
- ⚠️ The absence of Java version markers in `application.properties` or other config files means the runtime version is either set only in CI/build infra, or the project is relying on build agent’s JVM default (non-CAST-source).
- ⚠️ Absence of Dockerfile or container settings means any runtime upgrade must be handled either via Maven plugin properties (in poms) and/or deployment infrastructure (not represented in CAST MCP).

#### ❌ Not Found/Query Empty

- No evidence of build.gradle, build.gradle.kts, settings.gradle, build.xml, Makefile, shell scripts, or any non-Maven build orchestrator. (Query Log #11,13-22,54-60)
- No evidence of any explicit Java version property or plugin setting in CAST-modeled property files or as distinct Java Property Mappings. (Query Log #44-53)

---

#### Technical Appendix

See full Query Log below for direct traceability as required by GR-04–07.

---

### Query Log

- #1: applications — unrestricted — 9 returned/run-returned (Shopizer-3.2.5 present)
- #2: get_structural_search_function_syntax — function list — 47 returned/run-returned
- #3: stats (Shopizer-3.2.5) — 1 returned/run-returned
- #4: packages (Shopizer-3.2.5) — 0 returned/run-empty
- #5: source_files (pom.xml, Shopizer-3.2.5) — 6 returned/run-returned
- #6: source_file_details (shopizer-3.2.5/pom.xml)
- #7: source_file_details (sm-shop-model/pom.xml)
- #8: source_file_details (sm-core-modules/pom.xml)
- #9: source_file_details (sm-core-model/pom.xml)
- #10: source_file_details (sm-core/pom.xml)
- #11: source_file_details (sm-shop/pom.xml)
- #12-22: source_files (build.gradle, build.gradle.kts, settings.gradle, settings.gradle.kts, build.xml, Dockerfile, etc.) — all run-empty, no files found
- #23-33: source_files (CI/CD infra, .github/workflows, Jenkinsfile, etc.) — all run-empty, no files found
- #34-36: objects, type-based search for Java Properties File — failed, syntax corrected in #41–#46
- #41: objects (name:contains:application, type:contains:properties) — 3 returned/run-returned
- #42-44: source_file_details for application.properties and test varients — property mappings exist, no Java version entries
- #45-53: objects property search for explicit Java version settings — run-empty (none found)
- #54-60: Attempted further build system indicators (.gitlab-ci, cloudbuild.yaml, Makefile, build.sh, etc.) — all run-empty
- #61: objects (name:contains:toolchain, type:contains:Java Property Mapping); run-empty
- #62: objects (name:contains:enforcer, type:contains:Java Property Mapping); run-empty

**CAST Snapshot ID:** Not available in CAST MCP — [query attempted on structure only].

---
