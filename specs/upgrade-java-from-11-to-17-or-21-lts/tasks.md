# Upgrade Java — Task List

1. **Determine Build Tool**  
   - Directly review repo or CI: build tool not provided in CAST MCP. (Research §1; Query Log #2, #3) ❌

2. **Upgrade Project JDK Version**  
   - Update project JDK requirements to Java 17 or 21 across all build configs. (Research §1; Query Log #2)

3. **Library/Framework Compatibility Audit**  
   - Validate all frameworks/libraries in use (Spring, Hibernate, AWS SDK, Google Cloud Storage) against minimum supported versions for Java 17/21. (Research §1; Query Log #2, #4)

4. **Codebase Compatibility Review**  
   - Refactor or patch code where deprecated/removed Java 11 APIs are detected.
   - Remediate cases requiring new language features (Records, Sealed Interfaces, etc., if required).
   - Address all listed quality/security issues with risk of becoming blocking defects under Java 17/21 (Research §2; Query Log #5, #7).

5. **Upgrade & Retest Application**  
   - Build and run app under Java 17/21, resolve any issues.
   - Run regression tests for all listed REST entry points (Research §3; Query Log #8).

6. **Documentation & Future Monitoring**  
   - Update system architecture and deployment docs to confirm Java 17/21 baseline.
   - Re-sync CAST MCP analysis post-upgrade to assess new quality/security posture.

*Note: Absence of batch/message-driven entry points confirmed per current transaction inventory (Research §3; Query Log #8).*
