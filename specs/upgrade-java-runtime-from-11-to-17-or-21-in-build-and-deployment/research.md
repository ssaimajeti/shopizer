### Research & Query Log

1. `architecture_overview` (FAILED): No project-level overview returned; direct code/config search required.
2. `find_symbol` ('pom.xml'): 0 results – no Maven manifest detected in the indexed codebase.
3. `find_symbol` ('build.gradle'): 0 results – no Gradle build script detected in the indexed codebase.
4. `find_symbol` ('Dockerfile'): 0 results – no Dockerfiles surfaced as modules.
5. `find_symbol` ('application.yml'): 0 results – no application-specific YAML.
6. `iac_index` (FAILED): 502 error – unable to enumerate IaC/config resources.
7. `get_dependency_report` (MAVEN): No manifests – Maven not used or not detected.
8. `get_dependency_report` (NPM): No manifests – no JavaScript/Node detected.
9. `get_dependency_report` (PyPI): No manifests – no Python dependencies.
10. `get_dependency_report` (Go): No manifests – no Go dependencies.
11. `get_dependency_report` (RubyGems): No manifests – no Ruby dependencies.
12. `fulltext_search` ("java"): 0 results – no direct references to "java" in symbol names.
13. `fulltext_search` ("JAVA_HOME"): 0 results – no explicit environment variable config surfaced.
14. `fulltext_search` ("openjdk"): 0 results – no explicit OpenJDK reference in symbol names.
15. `fulltext_search` ("FROM"): 0 results – no Dockerfile surfaced with FROM lines.
16. `fulltext_search` ("11"): 0 results – no direct Java version found in symbol names.
17. `fulltext_search` ("17"): 0 results – no direct Java version found in symbol names.
18. `search_code` ("jdk"): ERROR 404 – not available.
19. `fulltext_search` ("gradle"): 0 results – no references to Gradle in symbols.
20. `fulltext_search` ("maven"): 0 results – no references to Maven in symbols.
21. `fulltext_search` ("Makefile"): 0 results – no Makefiles as modules.
22. `fulltext_search` (".github"): 0 results – no GitHub Actions detected in symbol inventory.
23. `fulltext_search` (".ci"): 0 results.
24. `fulltext_search` ("pipeline"): 0 results – no pipeline config detected as symbols.
25. `fulltext_search` ("actions"): 0 results.
26. `fulltext_search` ("ci"): 0 results.
27. `fulltext_search` ("Docker"): 0 results.
28. `fulltext_search` ("env"): 0 results.
29. `fulltext_search` ("yaml"): 0 results.
30. `find_dead_code` (limit 20): Located 20 mostly Java class candidates for cleanup, in `sm-core-model/`, `sm-core/`, and `sm-shop/` source trees.
31. `fulltext_search` ("main"): 0 results – no unusually named "main" entry points detected.
32. `fulltext_search` ("settings.xml"): 0 results.
33. `fulltext_search` (".bashrc"): 0 results.
34. `fulltext_search` ("release"): 0 results.
35. `detect_communities` (FAILED): Tool/server error.
36. `module_dependency_graph` (limit 50): Surfaces Java source dependencies—key hotspots are in `sm-core`, `sm-core-model`, and `sm-shop` domains with heavy file-to-file imports at business logic boundaries.

#### Key Findings:
- No build files (`pom.xml`, `build.gradle`) or Dockerfiles detected as codebase modules.
- No explicit Java version settings visible via symbol or config search.
- Dead code identified in Java source; main coupling and entry points in `sm-core`, `sm-core-model`, `sm-shop`.
- No detected references to common CI/CD configs (YAML, Makefile, pipeline, GitHub Actions).
- Codebase appears to be Java (based on class/package structure), but the build/deployment setup is not discoverable via indexed config or symbol files.

#### Conclusion:
- The codebase is Java (class/package structure: `.java` in `sm-core`, `sm-shop`).
- No explicit build/deployment config files found in available symbols.
- Manual/physical file system check may be required to locate build or pipeline configs for Java runtime upgrade.
