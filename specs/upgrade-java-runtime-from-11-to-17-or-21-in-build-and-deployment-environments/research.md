# Research Findings and Appendix — Shopizer-3.2.5 Java Upgrade

**Confidence Legend:**
- ✅ Direct CAST result
- ⚠️ Structurally inferred (SME validation required for indirect conclusions)
- ❌ Query ran, no result

---
## Section 1: CAST Research Findings

### Application Overview and Technology Stack
- Application name: Shopizer-3.2.5 (ID not directly returned)  
  (Source: CAST MCP — applications: Shopizer-3.2.5 / [no ID] / 1) ✅
- Technologies used: Java, Java EE, JPA, Hibernate, Spring, AWS S3 SDK for Java, Google Cloud Storage, Java Properties, JSP, Spring Web Services and others  
  (Source: CAST MCP — stats: Shopizer-3.2.5 / [no ID] / 1) ✅

### Build Tools and Packages
- No package management or build tool detected (no Maven/Gradle/Ant; package listing returned empty)  
  (Source: CAST MCP — packages: Shopizer-3.2.5 / [no ID] / 0) ❌

### Structural Quality Findings
- Three quality rules surfaced for Shopizer-3.2.5; examples include empty catch blocks and cross-site scripting. No findings relate to Java runtime version compatibility. 
  (Source: CAST MCP — quality_insights: Shopizer-3.2.5 structural-flaws / [no ID] / 3) ✅

### Online Entrypoints (Spring MVC Transactions)
- Numerous Spring MVC endpoints detected via transaction search (see GR-11, GR-10). Samples:
  - `/` GET (DefaultController.java)
  - `/api/v1/auth/cart/{}/checkout/` POST (OrderApi.java)
  - `/api/v1/category/` GET (CategoryApi.java)
  (...see Query Log for full list.)
  (Source: CAST MCP — transactions: Shopizer-3.2.5 / [no ID] / >50) ✅

### No batch or message-listener entrypoints were detected (only Spring MVC/REST endpoints)

### Code Footprint
- Over 90000 LOC, >16,000 elements. Substantial Java codebase.  
  (Source: CAST MCP — stats: Shopizer-3.2.5 / [no ID] / 1) ✅

---
## Appendix (CAST Results as Name/ID Pairs)

Refer to the Query Log for every fact; where no ID or non-unique result, name+description provided per GR-04/05.

---
## Query Log
(Snapshot ID/date: Not available in CAST MCP APIs)

1. applications — Shopizer-3.2.5: run-returned, 9 applications: Shopizer-3.2.5 ([no ID]), etc.
2. stats — Shopizer-3.2.5: run-returned, 1 result: Technology stack confirmed, object types identified.
3. object_profiles — Shopizer-3.2.5: run-returned, multiple internal Java object types and quantities.
4. quality_insights — Shopizer-3.2.5 (cve): run-empty (no results, no CVE scanning configured).
5. quality_insights — Shopizer-3.2.5 (structural-flaws): run-returned, three rules surfaced.
6. packages — Shopizer-3.2.5: run-empty (no package/build tool metadata detected).
7. transactions — Shopizer-3.2.5: run-returned, >50 Spring MVC transaction/endpoint start points.

**No further detail available for build tool or package management configuration in CAST MCP; see proposal/disposition in plan_md and tasks_md.**
