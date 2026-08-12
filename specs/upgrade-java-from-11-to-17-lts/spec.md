## Upgrade Java from 11 to 17 (LTS)

**Business Requirement:**  
Upgrade the Shopizer-3.2.5 application from Java 11 to Java 17 (LTS).  
This will ensure runtime support, security compliance, access to modern Java features, and improved maintainability.

**Source:**  
Requirement provided by user; business-level context not sourced from CAST, no BCM was provided (compliance gap — see rules).

**Scope Constraints:**  
- CAST BCM subsystem/component filters are not available: ALL queries in this analysis are app-wide (see compliance flags).
- Shopizer-3.2.5 is confirmed as the target application (CAST MCP).

**Rationale:**  
Java 17 introduces new features and long-term support, which is critical to keep Shopizer maintainable and supported.

**Assumptions & Non-CASTable Detail:**  
- Proposed implementation steps (build tool detection, config edits, dependency adjustments) are ⚠️ proposal, not confirmed by CAST facts.
- No CAST evidence was found for active CVE findings. However, several quality/security "platform migration" blockers and structural flaws are present and must be reviewed/tested after upgrade.
