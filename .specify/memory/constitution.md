### Quality/Design/Compliance Principles

- All code changes must be backward compatible with existing application contracts (entrypoints, published APIs).
- Adhere to code style, format, and best practices consistent with the application's existing Java usage and the Spring Boot 3.x ecosystem.
- All config and bean definitions must compile and pass basic smoke/fuzz/integration tests following the upgrade.
- Security findings (structural, ISO 5055, CVE, CWE) must not regress, and all previously flagged issues should be tracked for recurrence.
- Maintain clear documentation of all changes, compliance gaps, and spec lineage, strictly referencing CAST discovery for source-of-truth.
- Trace all changes and findings to the exact CAST-generated object name/IDs, with full query log tracking per requirement.
