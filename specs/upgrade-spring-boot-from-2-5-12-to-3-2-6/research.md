# Appendix A: Grounded CAST Findings

## Shopizer-3.2.5 — Application and Structural Discovery

### Scope, Language, Runtime
- Application: Shopizer-3.2.5 (CAST ID, 2026 snapshot)  
  (Source: CAST MCP — applications / Shopizer-3.2.5 / run-returned)
- Language: Java, as confirmed by discovered `ShopApplication` source and associated configuration files.  
  (Source: object_details: name:contains:ShopApplication,type:contains:class / 2 / run-returned)

### Entry Points and Main Classes
- Main Entry Point: `com.salesmanager.shop.application.ShopApplication` (25394), annotated with `@SpringBootApplication(exclude={SecurityAutoConfiguration.class})`.  
  (Source: object_details: name:contains:ShopApplication,type:contains:class / 2 / run-returned)
    - `main` method found in the same file (6351), but code detail not retrievable (objects: name:contains:main,objects: id:eq:6351→intra/code / run-empty).
    - `ShopApplicationConfiguration` (25381) found, contains app-wide Spring configuration.

### Discovered Beans and Configuration Classes
- Spring Beans: At least 295 objects reported as `Spring Bean` type (see multiple queries across pages).  
  (Source: objects: type:equals:Spring Bean / pages 1–3 / run-returned)
- Main Spring Context Config Classes (examples):
    - `com.salesmanager.shop.application.ShopApplication` (25394)
    - `com.salesmanager.shop.application.config.ShopApplicationConfiguration` (25381)
    - Additional beans and component definitions as detailed in the appendices.

### Build Tool/Descriptor
- No `pom.xml`/`build.gradle` or similar build descriptor found in codebase via CAST object queries (❌).
- Several `maven-wrapper.properties` files found in multiple module locations:
    - sm-shop/.mvn/wrapper/maven-wrapper.properties (8497)
    - sm-core/.mvn/wrapper/maven-wrapper.properties (12906)
    - etc.
  (Source: objects: name:contains:maven / 5 / run-returned)
- No explicit artifact for the actual dependency descriptor (no objects: name:contains:pom|gradle|build found).

### Quality/Compliance/Findings
- ISO 5055, Structural Flaws, Maintainability, Reliability, and other code health findings are present.
    - Example: Structural flaws (e.g. "Avoid empty catch blocks", "Avoid reflected cross-site scripting") and ISO-5055 rules such as "Ensure httpOnly option is enabled" found in quality_insights.
    - No CVE findings available (objects: cve insights, run-empty).
  (Source: quality_insights, nature: iso-5055, structural-flaws, cve / run-returned).

### Queries Log
1. applications — all — 9 returned — run-returned
2. stats — Shopizer-3.2.5 — 1 returned — run-returned
3. packages — Shopizer-3.2.5 — 0 returned — run-empty
4. objects — name/build/pom/gradle/dependency/module/starter/boot/spring-boot/xml/plugin/jar/lib/version/core/application/main/maven — various result counts, see detailed logs above.
5. objects (type: Spring Bean) — 3 pages — >100 objects — run-returned
6. object_details (id/name filters) — ShopApplication, ShopApplicationConfiguration, etc. — run-returned
7. quality_insights (structural-flaws, iso-5055, cve) — several — run-returned (no cve found)
8. transaction_profiles, data_graph_profiles — summaries returned — run-returned

#### Disposition/Compliance Gaps
- BCM-scoped queries not available — all queries run over entire application (Standing GR-08 gap).
- Absence of build descriptors and detailed build context (no pom.xml/build.gradle) — acknowledged as a data limitation, not an evidence of absence in source.

---

**End of spec output**
