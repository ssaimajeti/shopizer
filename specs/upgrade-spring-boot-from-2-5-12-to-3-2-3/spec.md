## Upgrade Spring Boot from 2.5.12 to 3.2.3 for Shopizer-3.2.5

### Current State
Shopizer-3.2.5 leverages Spring Boot 2.5.12 and adopts a technology stack comprising `aws sdk s3`, `google cloud storage`, `hibernate`, `java`, `spring`, and `spring web services`. The application contains approximately 91,162 lines of code across 16,572 elements, including JPA Entities, Spring Beans, and Spring MVC components.

### Proposed Changes
Upgrade the Spring Boot framework to version 3.2.3. The upgrade involves updating dependencies, code modifications for compatibility, and potential namespace changes, especially migrating from `javax.*` to `jakarta.*` due to underlying Java changes in version 11+.

### Breaking Changes
- **Namespace Migration**: Transition from `javax.*` to `jakarta.*`; estimated on multiple JPA Entities and Spring Beans.
- **Dependency Updates**: Ensure external libraries have compatible versions with Spring Boot 3.2.3.
- **Testing Frameworks**: Validation and potential upgrades for testing frameworks and assertions.

### Affected Object Counts
- JPA Entities: 43
- Spring Beans: 43
- Spring MVC Components: 39

### Acceptance Criteria
1. Upgrade must ensure all current functionalities operate without issues in Spring Boot 3.2.3.
2. All endpoints must pass integration and unit tests post-upgrade.
3. Build system configuration should be verified and updated if discrepancies are identified.