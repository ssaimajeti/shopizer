# Technical Appendix and Query Log

## Application ID Discovery (GR-01, GR-04, GR-08)
- Shopizer-3.2.5 (name only, no unique ID surfaced)  
  (Source: CAST MCP — applications: Shopizer-3.2.5 / [no ID] / 1)

## File and Build Tool Discovery

- **No `pom.xml`, Maven, Gradle, or other build tool config files (except wrappers) were discoverable via CAST Imaging ("objects" with name or type hint).**
    - Queries tried:  
      - name:contains:pom (❌)
      - type:contains:maven (❌)
      - type:contains:gradle (❌)
      - name:contains:build,type:contains:file (❌)
      - name:contains:gradle,type:contains:file (❌)
      - name:contains:maven,type:contains:file (✅ found only `maven-wrapper.properties`, see below)
      - name:contains:wrapper,type:contains:file (✅)
    - **All yielded empty results except for Maven Wrapper property files. See object list below.**
    - No build Java version config or runner config could be detected.

- **Found Maven Wrapper property files:**  
  - maven-wrapper.properties ([filePath: sm-shop/.mvn/wrapper/maven-wrapper.properties], id: 8497)
  - maven-wrapper.properties ([filePath: sm-shop-model/.mvn/wrapper/maven-wrapper.properties], id: 8528)
  - maven-wrapper.properties ([filePath: sm-core/.mvn/wrapper/maven-wrapper.properties], id: 12906)
  - maven-wrapper.properties ([filePath: sm-core-modules/.mvn/wrapper/maven-wrapper.properties], id: 13264)
  - maven-wrapper.properties ([filePath: sm-core-model/.mvn/wrapper/maven-wrapper.properties], id: 13347)
  (Source: CAST MCP — objects: name:contains:maven,type:contains:file / 5 / run-returned; see Query Log)

- **Found many configuration and properties files, e.g.:**
  - application.properties (id: 8412, 9513, etc.)
  - shopizer-core.properties, shopizer-properties.properties, database.properties, log4j.properties, etc.
  - None discovered to contain explicit Java version settings (⚠️ CAST cannot parse property file contents).  
  (See Appendix/objects: type:contains:Java Properties File / many / run-returned)

- **No Dockerfile, docker-compose, CI/CD pipeline or YAML manifest files were discovered in this application.**
  - All related queries (name:contains:dockerfile/docker/yaml/k8s/deploy/release/ci/github/action/pipeline/etc.) returned empty.
  (Source: CAST MCP — objects: name:contains:[various pipeline-related strings],type:contains:file / all run-empty)

## Technology Summary  
- Confirmed in CAST: Java, Spring, JPA, Hibernate, AWS SDK S3 for Java, Google Cloud Storage for Java, Spring Web Services, Java Properties.
- No build or deployment runtime version properties were found or accessible via CAST Imaging discovery.

## Query Log

1. applications — all — 9 apps found, Shopizer-3.2.5 present (run-returned)
2. stats ("Shopizer-3.2.5") — summary of element types and tech: java, spring, etc. (run-returned)
3. objects ("type:contains:maven") — none (run-empty)
4. objects ("type:contains:gradle") — none (run-empty)
5. objects ("type:contains:Java") — [see above for long enumerated results] (run-returned)
6. objects ("type:contains:Java Properties File") — 42 files including application.properties, shopizer-core.properties, maven-wrapper.properties, etc. (run-returned)
7. objects ("name:contains:maven,type:contains:file") — 5 files, only maven-wrapper.properties (run-returned)
8. objects ("name:contains:pom,type:contains:file") — none (run-empty)
... (more detail: extensive combinations on build, deploy, and CI/CD file types, all run-empty except as stated)
- **Snapshot ID/date:** Not available in CAST MCP — [query attempted: not available].

## Standing Gaps & Compliance Notes
- ⚠️ BCM standing compliance gap (see GR-08).
- ⚠️ All build tool and pipeline config locations except Maven wrapper are absent in CAST.
- ⚠️ File content for properties/config was not extractable; manual inspection or repo access is required for code changes.
- ⚠️ None of the discovered property/config files can be structurally confirmed to mention Java version, per current CAST visibility.

## Appendix (Object List, GR-05, GR-06)
- maven-wrapper.properties (8497) — sm-shop/.mvn/wrapper/maven-wrapper.properties (Source: CAST MCP — objects: name:contains:maven, type:contains:file / 5 found / run-returned)
- maven-wrapper.properties (8528), sm-shop-model/.mvn/wrapper/maven-wrapper.properties
- maven-wrapper.properties (12906), sm-core/.mvn/wrapper/maven-wrapper.properties
- maven-wrapper.properties (13264), sm-core-modules/.mvn/wrapper/maven-wrapper.properties
- maven-wrapper.properties (13347), sm-core-model/.mvn/wrapper/maven-wrapper.properties
- plus 30+ other Java Properties Files (application.properties, etc.) as per above queries. 
- **No pipeline, CI/CD, Docker, YAML, or direct build tool config files available** (multiple run-empty queries).

## GR-12/13 Applicability
- N/A — Feature spec, not a decomposition exercise.
