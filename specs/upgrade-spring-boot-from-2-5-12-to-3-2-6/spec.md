# Upgrade Spring Boot from 2.5.12 to 3.2.6 for Shopizer-3.2.5

**Source**: Requirement document (not CAST/MCP-derived; see research.md for factual software state).

**Scope**:
- Target CAST application: Shopizer-3.2.5
- No BCM (Business Capability Model) component provided — explicit GR-08 compliance gap; analysis conducted app-wide.
- Upgrade Spring Boot version in Shopizer-3.2.5 from 2.5.12 to 3.2.6.

**Objective**: 
Upgrade the application’s Spring Boot framework to 3.2.6, with accompanying code, configuration, and dependency changes required for compatibility and operability.

**Key Considerations**:
- Language, runtime, and build tool are not identifiable in CAST MCP — see research.md.
- The application uses a significant number of Spring beans, controllers, and JPA/Hibernate entities.
- CAST MCP demonstrates wide usage of "Spring MVC" operations, JPA entities, and external Java dependencies.
- No direct evidence from CAST of test coverage or CI/CD configuration state.
- The application's structural and security exposures are enumerated under current quality insights.

**Upgrade Impact Areas**:
1. Spring Boot dependencies and related BOM updates.
2. Code-level incompatibilities (inferred: controllers, beans, configuration).
3. Any Spring Boot auto-configuration and security changes.
4. Third-party library compatibility: especially Hibernate, JPA, and API surface used.
5. [Proposal ⚠️] Application build tool and deployment scripts.
6. [Proposal ⚠️] Test updates for backward/forward compatibility.
