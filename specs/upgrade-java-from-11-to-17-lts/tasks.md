# Task List

1. **Baseline Verification**
   - Confirm presence of Java classes and source structure (see Appendix: "Java Class" objects).

2. **Source/Build Inventory**
   - On disk: identify and check `pom.xml`, `build.gradle`, or other build system descriptors, since CAST did not return these within the indexed structure (see Appendix: queries for build files).

3. **Upgrade Build to Java 17**
   - Update build descriptors to require Java 17.
   - Update toolchain/environment configuration in both build and runtime containers as needed.

4. **Build and Compile**
   - Execute a full clean build using Java 17 JDK.
   - Resolve compile-time errors and failing tests.

5. **Functional Test on Java 17**
   - Run all test automation and/or manual smoke tests on Java 17.

6. **Quality Verification**
   - Use CAST Research output to review "cloud blockers," green patterns, maintainability/security flaws and high-impact structural issues. Prioritize any relevant to Java core platform changes.

7. **Documentation and Compliance**
   - Document in all reporting that full-app (not BCM) scope was used due to absence of a BCM mapping (see Research Appendix).

