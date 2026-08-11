### Purpose
This modernization project aims to upgrade all Spring Boot and Spring Framework dependencies in the repository to their latest Long-Term Support (LTS) versions. The goal is to ensure future maintainability, stability, and security.

### Legitimacy
This initiative originates from the requirement to keep dependencies up-to-date in order to benefit from security patches, bug fixes, performance improvements, and new features provided by upstream LTS releases.

### Constraints
- All upgrades must be fully backward-compatible with existing business logic unless otherwise agreed.
- No unrelated refactors are allowed.
- Any found unused/dead code should be, if safe, flagged for removal, but only after validation/testing.
- All architectural findings and decisions must be traceable to code insights tooling output (see `research_md`).
