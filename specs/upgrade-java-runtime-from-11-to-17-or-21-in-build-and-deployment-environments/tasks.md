### Ordered Implementation Tasks

1. Review the following Maven poms for Java version compatibility settings:
     - shopizer-3.2.5/pom.xml
     - shopizer-3.2.5/sm-shop-model/pom.xml
     - shopizer-3.2.5/sm-core-modules/pom.xml
     - shopizer-3.2.5/sm-core-model/pom.xml
     - shopizer-3.2.5/sm-core/pom.xml
     - shopizer-3.2.5/sm-shop/pom.xml  
   (Appendix: Query Log #5–11)

2. In each pom, update (or add if missing) `<maven.compiler.source>` and `<maven.compiler.target>` property values to `17` (or `21` as needed).

3. This update should also be mirrored in any Maven Toolchains plugin or related version markers set in the poms (not visible in CAST MCP; confirm in SCM).

4. Communicate/verify the new Java version requirement with all infrastructure/DevOps/CI stakeholders (runtime and build host alignment).

5. Coordinate build agent (`JAVA_HOME`) and deployment (container/Docker image/server/VM) updates to use the new Java version everywhere the app is built and run.

6. Regression and integration test all modules as built and executed under the new Java runtime, focusing on errors, deprecated APIs, or library incompatibilities.

7. Finalize upgrade with updated documentation and developer communication notes.
