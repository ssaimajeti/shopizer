# Upgrade Plan: Java 11 to 17/21 for Shopizer-3.2.5

## Observed Foundations

- Shopizer-3.2.5 is composed almost entirely of Java-based code per CAST Imaging object inventory (✅).
- No gradle (build.gradle) nor Maven (pom.xml with `java.version` property) file information available to directly confirm current Java version or runtime logic (❌).
- System uses multiple Maven pom.xml files (`pom.xml` in root and core submodules), not gradle, according to file inventories (✅).
- No explicit Java version constraints visible in CAST-scanned objects or files (❌).
- No objects indicate “JavaVersion” logic in code or build, nor runtime-specific code-branching, nor build-tool plugin code tied to Java version (❌).
- No evidence in CAST reports refuting or confirming the presence of Java 17/21-incompatible APIs or syntax beyond general Java/enterprise frameworks. (❌ feature compatibility must be validated at build/test time.)

## Implications

- The upgrade must focus on updating all Maven poms to set toolchains and source/target compatibility as needed for Java 17 or 21.
- Source compatibility issues, dependency compatibility, and test pass rates must be validated outside CAST, as CAST finds no Java version-specific errors nor test scaffolding.
- Quality rule scan did not find blocking migration exceptions specific to Java language constructs or obsolete APIs; remaining code uses modern frameworks and Java libraries, but upgrade safety is not assured (⚠️).
- The analysis surfaced extensive usage of standard Java, JPA, Spring, AWS, Google Cloud, and related frameworks (✅).

## Key Risks

- Hidden transitive dependencies or plugins in Maven that are not surfaced in CAST model.
- APIs with behavior changes between Java 11 and 17+ (not visible to CAST).
- No build logs or runtime-specific post-upgrade validation possible in CAST.

## Proposal

**Upgrade scope:**
- Update Maven `pom.xml` files in all submodules identified in CAST’s file inventory.
- Run builds/tests externally for all modules at Java 17 and, if stable, Java 21.
- Investigate/test compatibility of all dependencies (frameworks and 3rd-party packages were detected, but not their version constraints).
- Perform code clean-up if required to remove deprecated/unsupported Java usage per the build tool and runtime upgrade documentation.
