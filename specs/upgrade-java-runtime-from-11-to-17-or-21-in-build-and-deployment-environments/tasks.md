# Ordered Implementation Tasks for Shopizer-3.2.5 Java Upgrade

1. **Audit declaration points:**
   - Locate all files/repos/scripts specifying Java runtime version (Dockerfile(s), `pom.xml`, `build.gradle`, CI configs, K8s manifests, etc.). [Not available in CAST — package/build tool listing query returned empty]

2. **Update runtime version in config/build definitions:**
   - Change Java baseline to 17 (or 21) in all identified build and infra locations.

3. **Update toolchains/plugins:**
   - Upgrade build tools, compiler plugins, and deployment images to ensure compatibility with Java 17/21.

4. **Compatibility hold-points:**
   - Confirm that all Java components, libraries, and entrypoints identified in CAST research.md (see transaction list and technology footprint) build and run against the upgraded runtime.

5. **Regression, smoke, and integration testing:**
   - Run application through full battery of automated and manual tests, validating all Spring MVC API endpoints (see transactions list in research.md), persistence operations, and cloud SDK integrations.

6. **Rollback preparation:**
   - Document rollback steps and version pinning for segments that fail upgrade validation.

7. **Documentation update:**
   - Update developer and deployment documentation to reflect new Java runtime baseline and key migration notes. 

**All tasks referencing application contents, endpoints, or subcomponents cite the corresponding research.md Appendix rows for CAST-verified facts.**
