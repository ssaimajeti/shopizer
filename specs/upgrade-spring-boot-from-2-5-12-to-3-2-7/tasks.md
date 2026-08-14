## Upgrade Tasks (references use Appendix name/ID pairs)

### 1. Update Build & Dependency Configs
- Update build file(s) (Maven/Gradle) to reference Spring Boot 3.2.7 (⚠️ proposal, no package manifests found in CAST; refer to `Query Log`).

### 2. Code/Annotation Refactor
- Review & update all classes with Spring Boot/Spring context responsibilities:
  - `ShopApplication` (25394)
  - `ShopApplicationConfiguration` (25381)
  - `CoreApplicationConfiguration` (25790)
  - `ApplicationSearchConfiguration` (25792)
  - `SpringApplication` (4832, external)
  - All related MVC Controller, Bean, and Config classes as enumerated in Appendix.
- Refactor for annotation/API changes as required for Spring Boot 3.x (⚠️ SME validation required).

### 3. Quality Remediation
- For each object flagged by CAST for structural flaw rules below, apply the recommended fixes and re-test:
  - Avoid Empty Catch Blocks (Rule 1060020)
  - Avoid Reflected XSS (Rule 8408, 8482)

### 4. Automated and Manual Retesting
- Fully regression test endpoints surfaced in `/api/v1/*`, `/admin/*`, `/shop/*`, `/customer/*` paths (see transactions Appendix).
- Explicitly regression test cloud storage and AWS S3 integration points.

### 5. Documentation and Out-of-Scope Recording
- For code/config changes made during upgrade not already cited by CAST, document each object impacted with the change rationale, and flag as "manual review" in the technical appendix for audit trace.
- Record absence of BCM scope as standing compliance gap.
