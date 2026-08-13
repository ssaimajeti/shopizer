# Research Log

1. architecture_overview (repo_id) → Error (could not get overview)
2. find_symbol (SpringBootApplication, class, limit=5) → 0 results (did not detect annotation/class by name)
3. search_code ("spring.boot") → 404 error (search endpoint error)
4. semantic_search ("Spring Boot application entry point") → 5 results (none definitive, top: ServiceRuntimeException)
5. find_symbol (main, function, limit=10) → 0 results (did not detect typical entrypoint)
6. find_symbol (Application, class, limit=10) → 0 results (no generic Application class found)
7. cyclomatic_complexity (limit=10) → 10 results (high fan-out for populator, mapper, facade, and test logic)
8. find_dead_code (limit=50, min_confidence=0.9) → 0 results (no dead code with high confidence)
9. service_boundaries (depth=2) → 0 results (no module-level service partitioning, possibly flat or legacy, or no graph module nodes)
10. module_dependency_graph (limit=50) → 50 results, strong coupling especially between controllers/facades and core models
11. get_dependency_report (MAVEN, min_severity=LOW) → 0 manifests discovered (none detected)
12. iac_index (no filter) → 502 error (infra-as-code scan failed)
13. adr_list () → 0 results (no ADRs found)
