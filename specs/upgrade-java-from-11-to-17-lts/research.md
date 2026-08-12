# Technical Appendix / Research

## A. Query Log

1. *applications*, no scope filter — 9 results (run-returned): Shopizer-3.2.5 (no ID), etc.
2. *stats*, application=Shopizer-3.2.5 — 1 result (run-returned): Java detected in techs.
3. *packages*, application=Shopizer-3.2.5 — 0 results (run-empty).
4. *objects*, application=Shopizer-3.2.5, filters=type:contains:Java — multiple results, confirming extensive Java codebase.
5. *objects*, application=Shopizer-3.2.5, filters=type:equals:Java Class, page=1 — paginated list, confirming presence of many concrete Java classes.
6. *api_inventory*, application=Shopizer-3.2.5 — large list of endpoints confirming a Spring MVC/REST structure.
7. *application_database_explorer*, application=Shopizer-3.2.5 — 10+ results; only "Missing Table" records shown (column structure not exposed; expected per Known Limitations).
8. *quality_insights* for all natures: cloud-detection-patterns, green-detection-patterns, cve (run-empty), structural-flaws, iso-5055 (see details below).
9. *objects*, filters for build files (`name:contains:pom.xml`, `name:contains:build.gradle`) — 0 results (run-empty).
10. *object_details*, focus=intra, id=25393 (Java class sample, AsyncConfig) — returned children/fields (run-returned).

**CAST MCP snapshot/version info:** Not available in MCP data queried.

## B. Evidence Table

| Name/type | ID | Query | Source/Disposition | Notes |
|-----------|----|-------|--------------------|-------|
| Shopizer-3.2.5 (application) | [no explicit ID] | applications | returned | Target of all queries |
| Java Class (sample: AsyncConfig) | 25393 | objects (page=1) | returned | Example of artifact/field structure |
| Java technology (tech) | [n/a] | stats | returned | Java present along with Spring/JPA/AWS SDK etc. |
| REST Endpoint (sample: /api/v1/auth/customer/register/) | 13032 | api_inventory | returned | Spring MVC REST API patterns detected |
| Database table (Missing Table: PRODUCT_VARIANT_GROUP, etc.) | 229351+ | application_database_explorer | returned | Column detail not exposed; known product limitation |
| Build Descriptor (pom.xml/build.gradle) | [not found] | objects | run-empty | Not observed in CAST — check manually on-disk |
| Quality insight (various) | (see below) | quality_insights | returned | See below for summary |

## C. Quality Insights (Sampling)

- **Cloud/blocker findings** (nature: cloud-detection-patterns): 23 distinct rules, e.g. "CloudReady - Use of an unsecured data string" (35 objects), "CloudReady - Avoid using hardcoded URLs (HTTP protocol)" (23 objects), "CloudReady - Using stateful session" (5 objects), etc.
- **Green/blocker findings** (nature: green-detection-patterns): High counts for "Avoid instantiations inside loops" (251 objects), "Avoid nested loops" (113), "Avoid empty catch blocks" (90), etc.
- **ISO 5055**: Issues detected (e.g., "Ensure httpOnly option is enabled for session", "PermitAll or user role should be specified..." etc.).
- **Structural flaws**: e.g., "Avoid reflected cross-site scripting (non persistent)", "Avoid empty catch blocks", etc.
- **CVE scanning**: Not available in this application per query; "likely not configured".

- See full log above for further details/citations.

## D. Limitations, Gaps, and Disposition

- Build tool/version information (Maven/Gradle file) not surfaced by CAST — check manually as pre-step.
- No direct evidence of Java version set in CAST — the presence of Java source artifacts supports upgrade scope.
- No BCM mapping supplied (all compliance/app-wide).
- SQL column details not retrieved (known, not treated as an absence).
- No package objects detected via `packages` query — package structure inferred from Java full class names.

