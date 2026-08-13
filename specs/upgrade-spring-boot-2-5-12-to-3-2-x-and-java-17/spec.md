# Modernization Specification: Upgrade to Spring Boot 3 and Java 17

## Objective

Upgrade the codebase to use Spring Boot 3.2.x and Java 17, ensuring code compatibility, package migration, and infrastructure readiness.

## Scope

- Source compatibility for all Java code (Java 17)
- Refactoring/rewriting any code patterns incompatible with Spring Boot 3.x APIs
- Update build and dependency configurations to require newer Java and Spring versions
- Infrastructure compatibility: Dockerfile(s), build pipeline, and any IaC definitions referencing Java runtime
- Preserve all application routes, wiring, and clear separation of controller, service, and model layers

## Constraints/Initial Facts

- The codebase is primarily Java, containing modules such as `sm-core`, `sm-shop`, and `sm-core-model` (cyclomatic_complexity, module_dependency_graph).
- Strong coupling exists between shop/store controllers/facades and core models (module_dependency_graph).
- No dead code with high confidence was detected (find_dead_code).
- Most complex/critical code (by fan-out) is in populator, mapper, and facade components (cyclomatic_complexity).
- No ADRs were found in the repository (adr_list).
- No Maven dependency manifest or SBOM was detected (get_dependency_report: manifests=[]).
- Application entrypoint did not surface using symbol/entity or major class introspection (find_symbol, semantic_search).

## Requirements

1. All modules must be reviewed and migrated for breaking changes between Spring Boot 2.x and 3.x; especially around:
    - Package renames (e.g., `org.springframework` updates)
    - Deprecated API removals or replacements
    - Hibernate/JPA compatibility
    - Tomcat/Servlet container APIs
    - Security config (Spring Security API changes)
2. Move all projects/components to use Java 17 syntax and features as appropriate.
3. Update or create missing build definitions to explicitly require Java 17 and Spring Boot 3.x.
4. Add an ADR to capture the rationale, decisions, and potential migration pitfalls.
5. Validate all critical flows, especially controller → service → repository → entity wiring, for annotation and config compatibility.

## Out of Scope

- Large-scale app redesign or business logic reworks.
- Extraction of microservices (but service boundaries and coupling will be documented).
