### Task List

1. **Discovery**
   - [ ] Confirm full inventory of all files referencing Java runtime or JDK version (`pom.xml`, `build.gradle`, Dockerfile, etc.).
   - [ ] Confirm presence of environment-variable settings and Docker base images relating to Java.

2. **Update**
   - [ ] Update found files/configs to reference Java 17/21 (from Java 11).
   - [ ] Update Docker base images (if Java is included).
   - [ ] Update CI/CD scripts and config files.

3. **Validation**
   - [ ] Run CI pipeline with Java 17, then Java 21 if possible.
   - [ ] Log and fix any arising build/runtime incompatibilities.

4. **Documentation**
   - [ ] Write summary of config/code locations updated.
   - [ ] Document manual steps required for contributors/developers.

5. **Risk Mitigation**
   - [ ] Identify dead code to minimize regression testing.
   - [ ] Document high-coupling files/modules (to prioritize for regression testing).
