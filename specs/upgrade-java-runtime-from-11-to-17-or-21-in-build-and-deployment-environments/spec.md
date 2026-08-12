# Upgrade Java runtime from 11 to 17 (or 21) in build and deployment environments — Shopizer-3.2.5

**Requirement Source:** This requirement was supplied externally (requirement document, not CAST MCP).

**Scope:**
- Application: Shopizer-3.2.5 (confirmed in CAST)
- **Business Capability Model (BCM) scope:** Not provided. **Standing compliance gap** per GR-08; all CAST queries run application-wide.

**Business Objective:**
Upgrade the Java runtime version for the Shopizer-3.2.5 application from Java 11 to Java 17 or Java 21. This includes updating the build and deployment pipelines/tools to use the new runtime where possible, without regressing compatibility or operational stability.

**Constraints/Notes:**
- The effective language used is Java (per CAST analysis — see research.md)
- No build tool or explicit dependency management (such as Maven or Gradle) could be confirmed in this CAST analysis. No explicit third-party package versions returned.

**Out of scope:**
- No BCM-slicing or BCM-specific sub-analysis possible

**Analysis artifacts:**
- All statements in this spec are either: external requirements, directly CAST-confirmed (see research.md, Appendix), or  flagged as proposal/assumption per grounding rules.

**Boundaries:**
- GR-12 and GR-13 are not applicable — this is a feature spec, not a decomposition or multi-capability analysis.
