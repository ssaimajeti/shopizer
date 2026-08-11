# Technical Appendix — CAST Findings

## Appendix A: Queries and Returned Facts

### Query Log

1. applications — scope: all; returned 9 apps (Shopizer-3.2.5 confirmed as target app)
2. stats — scope: Shopizer-3.2.5; run-returned: General stats (91162 LOC, 16572 elements, 72325 interactions, techs include spring, jpa, hibernate, spring web services, etc.)
3. transaction_profiles — scope: Shopizer-3.2.5; run-returned: 8 distinct profiles covering Spring MVC Put/Get/Post/Any/Delete for endpoints.
4. object_profiles — scope: Shopizer-3.2.5; run-returned: Detailed counts and properties for Java Class/Interface/Enum, Spring MVC Operation, Bean, JPA Entity, etc. — see below.
5. data_graph_profiles — scope: Shopizer-3.2.5; run-returned: Dataflow graphs for JPA, "Missing Table", S3, GCP, etc.
6. packages — scope: Shopizer-3.2.5; run-returned: "No packages in this application." (Not available in CAST MCP — [query attempted].)
7–11. quality_insights — scope: Shopizer-3.2.5 with natures: "structural-flaws"(run-returned, see below); cloud-detection-patterns/green-detection-patterns/cve/iso-5055 (query failed, all returned error).
12. application_database_explorer — scope: Shopizer-3.2.5; run-returned: 10 missing tables (of 56 total), e.g. PRODUCT_VARIANT_GROUP, SHOPPING_CART, etc.
13. api_inventory — scope: Shopizer-3.2.5; run-returned: Extensive list of Spring MVC endpoints for REST APIs.
  
- No CAST snapshot ID/date visible from tools (GR-03 placeholder).

### Key Findings (all ✅ direct CAST results except where noted)
  
#### Application Stats
- Shopizer-3.2.5: 91162 LOC, 16572 code elements/spans, 72325 interactions; major technologies: Java, Spring, JPA, Hibernate, AWS SDK S3, GCP, etc. (applications+stats)

#### Object/Code Type Inventory
- Controllers: Numerous Java Classes incorporating "Spring MVC * Operation" (346+ controller methods, numerous endpoints; object_profiles and api_inventory).
- Spring Beans: At least 295 "Spring Bean" objects (object_profiles).
- JPA Entities: At least 81 JPA Entity objects (object_profiles).
- External technology: "No packages in this application." (CAST: packages — likely incomplete model; see Note below.)

#### Transaction & API Endpoints
- Mix of public and private Spring MVC endpoints (GET, POST, PUT, DELETE, ANY) covering address, cart, checkout, catalog, customer, product, review, shipping, etc. — see api_inventory for full endpoint list by ID.
  
#### Datastore Access
- Largest segment of dataflows is between Spring MVC operations and "Missing Table" entities (data_graph_profiles, application_database_explorer).
- 10 example missing tables: PRODUCT_VARIANT_GROUP, PRODUCT_VARIATION, SHOPPING_CART, etc.

#### Quality Insights
- 3 structural flaws found (structural-flaws): 
    - "Avoid empty catch blocks for methods with high fan-in" (2 affected objects).
    - "Avoid reflected cross-site scripting (non-persistent)" (2 affected objects).
    - "Avoid cross-site scripting through API requests" (73 affected objects).
    (quality_insights)
- No package-level dependency inventory surfaced by CAST; build tool and runtime versions not detectable. (Not available in CAST MCP — packages, application_database_explorer.)

### Standing Gaps / SME Required
- Build tool (Maven/Gradle) and actual dependency declarations not available in CAST output.
- CI/CD or test coverage, runner, or non-code infra elements out of scope — not queried.

## Appendix B: Object/ID List (sample; full details on request)
- [Full endpoint and object profiles omitted for brevity, available as per research.md process.]

## Confidence Tiers
- ✅ = direct CAST query result
- ⚠️ = proposal where CAST is silent (build files, runtime, SME/workflow gaps)
- ❌ = query ran, no result (explicitly noted above)

## Boundaries
- GR-12/13: Not applicable for this feature upgrade; no business decomposition attempted.
