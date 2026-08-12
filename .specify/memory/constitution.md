# Constitution: Quality & Upgrade Principles

1. **Code Correctness**  
   - All upgrade changes must preserve functional equivalence.  
   - All automated/unit tests must pass post-upgrade.

2. **Backward Compatibility**
   - Unless changes needed for Java 17, do not alter business logic or APIs.
   - Maintain compatibility with downstream integrations.

3. **Code Conventions**
   - Follow established Java and project-specific code and build conventions.

4. **Testing**
   - Regression and smoke tests across major functional flows.
   - Track and resolve any test failures before sign-off.

5. **Documentation**
   - Record all upgrade steps, configuration changes, and detected issues.
   - Note compliance gap (no BCM) in all release and upgrade notes.

6. **Quality Finding Visibility**
   - Review and highlight all relevant green/cloud/security/structural issues (CAST Research).
