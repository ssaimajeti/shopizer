# CAST Imaging Exploratory Appendix

## Query Log (GR-07)

1. **applications** – Searched for all CAST Imaging apps (run-returned, 9 apps, including Shopizer-3.2.5).
2. **stats** – Basic stats for Shopizer-3.2.5 (run-returned: languages, frameworks, LOC, etc.).
3. **packages** – Enumerate Java packages (run-empty).
4. **objects** (type:contains:Java) – Inventory of Java objects (run-returned, several hundred: fields, classes, enums, etc.).
5. **views** – Saved Imaging views (run-empty).
6. **quality_insights**:cloud-detection-patterns – Cloud migration blockers/boosters (run-returned, general readiness patterns).
7. **quality_insights**:structural-flaws – General code quality risks (run-returned, none Java-version specific).
8. **quality_insights**:cve – CVE scan (run-empty, no CVE scanning detected).
9. **quality_insights**:iso-5055 – ISO 5055 (run-returned: generic Java/Spring rules).
10. **api_inventory** – API entry points and REST endpoints (run-returned).
11. **objects** (name:contains:pom.xml) – Search all objects by Maven file (run-empty).
12. **objects** (name:contains:build.gradle) – Search all objects by gradle file (run-empty).
13. **source_files** (pom.xml) – Search for pom.xml files (run-returned six pom.xml file paths).
14. **source_file_details** (all six poms) – Enumerate each mapped submodule (run-returned).
15. **source_files** (build.gradle) – Attempted gradle file by file_path (run-empty).
16. **architectural_graph** – Application architecture (run-returned, several named layers).
17. **inter_applications_dependencies** – Inter-app dependencies (run-failed).
18. **application_database_explorer** – Data entity inventory, tables (run-returned, evidence Shopizer uses RDBMS).
19. **object_profiles** – Code object counts/type breakdown (run-returned, confirmed Java is the technology).
20. **get_mcp_info** – MCP/Imaging API server version check (run-returned).
21. **tags** – Explicit object tags (run-empty).
22. **get_semantic_search_function_syntax** – Semantic search support (run-returned functions list).
23. **run_semantic_search_function:get_relevant_code_context** – Lookup for Java version/logic handling (run-error).
24. **run_semantic_search_function:find_entities** – Entity extraction for Java version (run-error).
25. **objects** (name:contains:JavaVersion) – Searched for direct Java version code usage (run-empty).
26. **objects** (name:contains:version) – Searched for generic “version” fields/methods (run-returned, mostly serialVersionUID, one API_VERSION constant).
  
**CAST snapshot ID:** Not available in CAST MCP — [query attempted]. See log above (GR-03).

## Application Structure (sample)

- **Java classes:** 833 (object_profiles, Java Class type)
- **Modules (poms):** shopizer-3.2.5/pom.xml, sm-core/pom.xml, sm-shop/pom.xml, sm-core-model/pom.xml, sm-core-modules/pom.xml, sm-shop-model/pom.xml (source_files/source_file_details)
- **Build tool:** Maven only (build.gradle not found, multiple poms present).

## Quality/Cloud Insights (summarized)

- **Cloud migration blockers:** Environment-variable access, directory/file manipulation, use of stateful sessions, hardcoded URLs, unsecured protocols (see quality_insights:cloud-detection-patterns for full rules).
- **Structural issues:** XSS, catch blocks, commented-out code, copy-paste, string concatenation inefficiencies (see quality_insights:structural-flaws/iso-5055).
- **Java version-specific blockers:** None detected in CAST’s rule library or inventory.

## BCM Scope

- **Scope:** App-wide only (no BCM). This is a compliance gap per GR-08 — document in all sections.

## Tech/Build Tool

- **Maven:** Confirmed, 6 poms present, modules identified, no gradle.
- **Spring/JPA/Spring Boot:** Confirmed in technology stats and present in object inventory.
- **Direct Java version setting:** Not visible in CAST-modelled objects or properties. No build or runtime evidence beyond poms’ existence.

## Limitations

- **No CAST evidence for existing Java version config:** No versioned plugins, constants, or Java-version-selective logic present (see objects, find_entities, get_relevant_code_context — all run-empty or run-error).
- **No gradle:** Objects and files for gradle not present.
- **No code-scoped “JavaVersion” logic or checks** found in method or field scan.
- **CVE results:** CVE scan empty (not enabled?).
- **SME/manual build required**: Success must be validated via actual module build and test after upgrade (CAST cannot simulate build outcomes per Java version).

## Appendix (sample citations)

- **shopizer-3.2.5/pom.xml** (Source: CAST MCP — source_files: shopizer-3.2.5/pom.xml / [no ID available] / 1)
- **Java object count/usage:** (Source: CAST MCP — object_profiles: 833 Java Class / [no ID available] / 833)
- **API_VERSION field:** com.salesmanager.shop.application.config.MultipleEntryPointsSecurityConfig.API_VERSION (13702) (Source: CAST MCP — objects: API_VERSION / 13702 / 1)
- **Cloud migration blockers:** CloudReady - Use of unsecured data string, 35 instances (Source: CAST MCP — quality_insights:cloud-detection-patterns: platform-migration:1200056 / [ids present] / 35)
- **No gradle detected:** (Source: CAST MCP — objects: name:contains:build.gradle / [no results] / 0)
- **No Java version property detected:** (Source: CAST MCP — objects: name:contains:JavaVersion / [no results] / 0)
- **No CVEs detected:** (Source: CAST MCP — quality_insights:cve / [no results] / 0)

---

**Standing Gaps:**
- This upgrade spec cannot guarantee Java 17/21 success until external build/test confirms no transitive incompatibilities.
- No code or infrastructure logic found enforcing, configuring, or varying by Java version within application/builder/launcher logic.
- All remaining unknowns must be handled outside CAST with regression/integration validation.

---

**End of CAST MCP-compliant spec for Java 11→17/21 migration, Shopizer-3.2.5.**
