### Upgrade Implementation Plan (⚠️ Proposal)

**Note:** All actionable steps are based on inferred structure from CAST’s object and file inventory, not on visible settings or pipeline definitions.

1. **Update Maven POM Files**  
   - Edit all the following pom.xml files:
     - shopizer-3.2.5/pom.xml
     - shopizer-3.2.5/sm-shop-model/pom.xml
     - shopizer-3.2.5/sm-core-modules/pom.xml
     - shopizer-3.2.5/sm-core-model/pom.xml
     - shopizer-3.2.5/sm-core/pom.xml
     - shopizer-3.2.5/sm-shop/pom.xml  
   - For each, set the Java source/target version by adding or updating:
     - `<maven.compiler.source>17</maven.compiler.source>`
     - `<maven.compiler.target>17</maven.compiler.target>`  
   - If using `maven-toolchains-plugin`, set Java `version=17` or desired target across effective pom hierarchy.  
   - If a parent pom, make property changes there and ensure submodules do not override to a lower version.

2. **Build Agent/Developer Environment**  
   - Ensure all local and CI/CD agents used for Maven builds are running Java 17 or 21 (`JAVA_HOME`/`PATH`).

3. **Deployment Environment**  
   - (If containerized) Update Docker images/base images to Java 17/21 (not visible in CAST; must be confirmed manually).
   - (If not containerized) Ensure VMs/app servers run Java 17/21.
   - Document/coordinate the switch, ensuring rollback path (Test old vs new runtime as needed).

4. **Testing and Validation**  
   - Run full regression, integration, and smoke tests on Java 17/21 environments.
   - Watch for dependency or framework incompatibilities, focusing on any hand-written source code or legacy third-party libraries.

5. **Documentation/Knowledge Transfer**  
   - Record the upgrade rationale, procedure, and any findings in project docs.

---