# Implementation Plan (⚠️ = proposal, not CAST-confirmed)
1. **Inventory Preparation**
    - Export and review current Maven/Gradle dependency configuration for evidence of Spring Boot 2.5.12 and all Spring-* dependencies. (⚠️ See research.md: CAST MCP did not expose actual dependency/build files content/format.)
    - Identify all code elements under Spring MVC Controller, Bean, and JPA usage for prioritization.
2. **Upgrade Execution**
    - Update dependency/BOM version for Spring Boot to 3.2.6 in the build configuration. (⚠️)
    - Review all controllers ("Spring MVC * Operation" objects) for annotation, API, and return-type compatibility with 3.x, especially API endpoints and parameter/return types.
    - Review and update custom Spring Beans for API, lifecycle, or annotation changes.
    - Check JPA Entity models and repository implementations for required changes/proxies (see object_profiles summary: extensive JPA/Hibernate usage found).
    - Review usages of Hibernate and other third-party libraries for known 2.x→3.x breaking changes. (⚠️)
    - Update application and test configuration for new Spring Boot settings as required. (⚠️)
3. **Quality & Validation**
    - Address high/critical structural flaws before/after upgrade to ensure safe upgrade (see research.md: unresolved XSS/code-quality issues exist).
    - Regression test all public Spring MVC endpoints; validate data graphs and critical DB table access patterns.
    - Document all upgrade decisions, post-upgrade actions, and remediation.
4. **Standing Gaps**
    - CI/build pipeline and non-code deployment steps not visible in CAST — manual SME validation required.
