### CAST MCP Research Log and Technical Appendix

#### Query Log

| # | Tool                | Scope/Args                                    | Result Count / Disposition      | Objects (name/ID)           |
|---|---------------------|-----------------------------------------------|-------------------------------|-----------------------------|
| 1 | applications        | N/A                                           | 9 / run-returned              | Shopizer-3.2.5 ([name only], ID not provided) |
| 2 | stats               | Shopizer-3.2.5                                | 1 / run-returned              | Techs: Java, Java EE, Spring; 91162 LOC, 16572 elements |
| 3 | packages            | Shopizer-3.2.5                                | 0 / run-empty                 | Not available               |
| 4 | object_profiles     | Shopizer-3.2.5                                | run-returned (dozens)         | See below for types         |
| 5 | quality_insights    | Shopizer-3.2.5, structural-flaws              | 3 / run-returned              | rules, see below           |
| 6 | quality_insights    | Shopizer-3.2.5, cve                           | 0 / run-empty                 | —                           |
| 7 | quality_insights    | Shopizer-3.2.5, cloud-detection-patterns      | 23 / run-returned             | see below                   |
| 8 | transactions        | Shopizer-3.2.5                                | run-returned (>50 endpoints)  | see below                   |

**Snapshot ID/date:** Not provided by CAST MCP for any queries in this session.

---

**Appendix: Key CAST Facts**

##### Application Discovery
- Shopizer-3.2.5 is in-scope for all queries. No BCM was provided; app-wide analysis performed. (Sources: #1/#2)

##### Technologies/Structure
- Java, Java EE, Hibernate, Spring, AWS/GCP SDKs, 91162 LOC, 16572 elements. (Source: #2)
- No "package" info available (run-empty, #3).

##### Java Elements and Endpoints
- Internal components: Java Class, Java Method, JPQL Query, Spring MVC controllers/operations, JPA Entities, etc. (Source: #4)
- 81 JPA entities, 108 JPA entity operations, numerous REST endpoint handlers (Source: #4)
- Multiple endpoint types detected under Spring MVC (GET/POST/PUT/DELETE/ANY ops), see Query #8 for full enumerated routes.

##### Transactions (Endpoints)
- Dozens of clearly mapped REST endpoints:  
  - E.g. `/api/v1/auth/cart/{}/checkout/`, `/api/v1/auth/customer/`, `/api/v1/auth/orders/`, `/api/v1/content/images/`, etc. (Source: #8)
  - Each endpoint type (GET/POST/PUT/DELETE) with specific routes, supporting full API regression after JVM upgrade.

##### Quality Insights (Structural Flaws, Cloud Detection)
- Structural flaw rules triggered:
  1. "Avoid empty catch blocks for methods with high fan-in" — Risk: Reliability.
  2. "Avoid reflected cross-site scripting (non-persistent)" — 2 findings. Risk: Security.
  3. "Avoid cross-site scripting through API requests" — 73 findings. Risk: Security.  
  (Source: #5)
- Cloud migration/platform blockers:
  - Use of environment variables, file/directory manipulation, hardcoded URLs/addresses, unsecured data strings, stateful session (Socket/Servlet), in-memory caching.
  - E.g. Rule: "CloudReady - Use of unsecured data string" (35 objects), "CloudReady - Avoid using hardcoded URLs" (23 objects), and others detailed in #7.

##### CVEs
- No CVE findings present or scanned (empty result, #6).

---

**GR-12/13:**  
- Not applicable for this feature spec (not a decomposition; all boundaries per CAST surface evidence only).

---

**Confidence Key for Facts**  
- ✅ = Confirmed, direct CAST result  
- ⚠️ = Proposal/structurally inferred from app-wide CAST analysis  
- ❌ = CAST query ran but returned no data

---

**End of grounded spec kit.**