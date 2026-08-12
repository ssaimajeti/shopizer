# Implementation Plan: Java Runtime Upgrade to 17 (or 21) for Shopizer-3.2.5

1. **Baseline and Audit:**
   - ⚠️ Inventory all locations where the Java runtime version is declared (build scripts, Dockerfiles, CI/CD configuration, application server configs).  
   - ⚠️ Since CAST MCP does not expose direct build/deployment scripts or package verifications, this task must be performed via source/infra repo examination.

2. **Compatibility Review:**
   - ✅ All application logic is Java-based (see research.md). 
   - ⚠️ Review all known integration points (Spring MVC REST controllers, JPA, Hibernate, S3/Cloud SDKs) for compatibility with Java 17+.

3. **Perform Upgrade:**
   - ⚠️ Update Java runtime version in all build system and deployment targets to Java 17 or 21.
   - ⚠️ Update underlying image/runner as required by the new Java runtime.
   - ⚠️ Update any toolchains/compilers/plugins if necessary for Java 17/21 support (e.g., maven-compiler-plugin).

4. **Application testing:**
   - ⚠️ Confirm all application online entrypoints (exposed via Spring MVC — see transaction list in research.md) build, deploy, and successfully execute under Java 17/21.   
   - ⚠️ Run application-level regression and smoke tests; verify no change in interface contracts, data persistence, or error handling.

5. **Risk & Rollback:**
   - ⚠️ Plan for rollback or selective Java version pinning if issues detected during test/prod rollout.

6. **Documentation & Release:**
   - ⚠️ Update release notes, runbooks, and infrastructure-as-code documentation to record the Java runtime upgrade and any relevant migration issues, even if not detected in CAST findings.

**Legend**: 
- ✅ CAST-confirmed fact
- ⚠️ Proposal/action requiring out-of-band repo/infra/manual review

**All plan items referencing explicit CAST findings are cross-referenced in research.md's Appendix.**
