### Business-level Requirements Spec

This specification describes changes required to upgrade the Java runtime version in both the build and deployment environments for the Shopizer-3.2.5 application. The requested upgrade is from Java 11 to Java 17 (or 21). This requirement is sourced from a non-CAST document (requirement.doc/Jira), not from CAST MCP, and no business process subsystem (BCM) was provided. Per governance, this is a standing compliance gap (see GR-08).

The requirement touches both the build process and deployment/runtime environments, ensuring that the application is compiled and executed on the targeted Java version. All changes must be compatible with Shopizer-3.2.5 source and dependency structure.

**Note:** No explicit evidence for the current Java runtime, target runtime settings, or CI/CD pipeline files was found in the CAST application snapshot. Build definitions (e.g., Maven properties, Gradle, Dockerfile, GitHub Actions, CI/CD scripts) setting the Java version are either not captured in the CAST graph, not present in the codebase, or not modeled.

**Scoping Clarification:** No BCM scope was provided in the requirement or found in CAST. All findings are based on a full application scan.

GR-12/13 boundaries are *not applicable* as this is a runtime/platform upgrade.

---