## Research Appendix (CAST MCP: Shopizer-3.2.5, 2026-07-30T17:29:00)

### Query Log
1. `applications` — all; 9 results; found Shopizer-3.2.5 (run-returned)
2. `stats` — Shopizer-3.2.5; 1 result; summary stats and technology (run-returned)
3. `object_profiles` — Shopizer-3.2.5; 255 profiles; detected types: Java Class, Spring Bean, Spring MVC {Any,Get,Post,Put,Delete} Operation, Spring MVC Controller, JPA Entity, etc. (run-returned)
4. `packages` — Shopizer-3.2.5; 0; No packages reported (run-empty)
5. `quality_insights` — Shopizer-3.2.5/nature:cve; 0; CVE scan not configured (run-empty)
6. `quality_insights` — Shopizer-3.2.5/nature:structural-flaws; 3 rules returned, including rules for empty catch blocks, and XSS vulnerabilities (run-returned)
7. `transactions` — Shopizer-3.2.5; hundreds of REST transactions discovered (run-returned; names/IDs in log below, partial ids shown for brevity)
8. `data_graph_profiles` — Shopizer-3.2.5; 7 graphs covering AWS S3, Google Cloud Storage, JPA/Hibernate, etc. (run-returned)
9. `object_details` — focus:intra; multiple queries and key findings (run-returned)

### Found Objects of Interest
#### Upgrade-Relevant Classes/Configurations
- `com.salesmanager.shop.application.ShopApplication` (25394, Java Class with @SpringBootApplication annotation; defines main entry method)  
- `com.salesmanager.shop.application.config.ShopApplicationConfiguration` (25381, Java Class with @Configuration, @ComponentScan, @EnableWebSecurity, etc.)
- `com.salesmanager.core.business.configuration.CoreApplicationConfiguration` (25790, Java Class with various @Enable* annotations and imports)
- `com.salesmanager.core.business.configuration.ApplicationSearchConfiguration` (25792, Java Class with @ConfigurationProperties)
- `org.springframework.boot.SpringApplication` (4832, external, core bootstrapping class)
- `org.springframework.context.ApplicationEvent` (4787, external, possibly related to event multicasting on boot)

#### MVC/API Exposure
- Spring MVC Router classes and method counts found: 
  - e.g. `Spring MVC Get Operation` (141), `Spring MVC Post Operation` (85), `Spring MVC Controller` (59) (Source: CAST MCP — object_profiles)
- CAST transaction discovery lists >50 unique endpoints under `/api/v1/*`, `/admin/*`, `/shop/*`, `/customer/*` etc.

#### Data/Cloud Integration Points
- Data graph profiles show integrations with AWS S3 (3), Google Cloud Storage (2), JPA/Hibernate DBs (multiple), etc. (Source: CAST MCP — data_graph_profiles)

#### Quality Rule Violations
- Rule 1060020 Avoid Empty Catch Blocks for high fan-in methods (2 objects, see CAST log for object/ID specifics)
- Rule 8408, 8482 XSS in user input handling (2, 73 objects respectively)

#### Third-Party/Dependency Gaps
- No third-party package inventory available from CAST for this application (run-empty)
- No CVE scan available from CAST for this application (run-empty)

### Confidence Tiers Tagging
- ✅ Facts above trace directly and solely to CAST MCP queries and their returned object name/ID pairs.
- ⚠️ Items proposed based on external/expected knowledge (e.g., Maven usage, Spring Boot upgrade patterns) are identified as proposals, not CAST-determined facts.

