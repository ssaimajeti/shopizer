# Quality Constitution for Java Platform Upgrades

1. **Backward Compatibility:**  
   - No functionality regression is acceptable unless required by upstream (JVM, framework) breaking change.

2. **Quality Gate:**  
   - All known CAST MCP structural and ISO 5055 security/maintainability issues must be tracked, with "critical" and "blocking" issues (per rule descriptions) remediated pre-release.

3. **Code Conventions:**  
   - Code changes must follow Java language and project formatting standards; no new warnings or static analyzer errors permitted from upgrade changes.

4. **Testing:**  
   - All existing tests (unit, integration, regression) must pass under Java 17/21; test coverage should expand for new/changed areas where possible.

5. **Documentation & Traceability:**  
   - Document all changes to source, config files, and build pipeline (including rationale for updates/deprecations).

6. **App-wide Scope Acknowledgment:**  
   - In absence of BCM breakdown, all remediation/testing must treat Shopizer-3.2.5 as a single deployable unit—no subcomponent splitting.

7. **Compliance Logging:**  
   - Log and explain all missing scoping, config, or technical metadata with reference to CAST MCP queries (see Research Appendix).

8. **No Hallucination:**  
   - Only CAST MCP findings, or directly observed repo/CI build configs, are valid evidence of the technology stack and compatibility posture.
