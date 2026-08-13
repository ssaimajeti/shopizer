# Migration Tasks

## 1. Planning & Inventory
- [ ] Inventory build files (pom.xml, build.gradle, etc.)
- [ ] Validate current Java version and Spring Boot version detection

## 2. Build System Updates
- [ ] Update build files to require Java 17 and Spring Boot 3.2.x
- [ ] Upgrade incompatible dependencies

## 3. Code Review/Refactoring
- [ ] Audit major controller, service, facade, and populator classes for deprecated or removed Spring/Java APIs
- [ ] Refactor code to replace/mitigate incompatibilities
- [ ] Run static/IDE migration tools for Spring Boot and Java syntax

## 4. Infrastructure
- [ ] Update Dockerfiles and any IaC configs to pull Java 17 images
- [ ] Patch CI/CD workflows for Java 17

## 5. Test & Verification
- [ ] Run and pass all existing tests
- [ ] Manual integration validation on critical functions (populators, mappers, facades)

## 6. Documentation
- [ ] Create ADR for the upgrade
- [ ] Update README, onboarding, and migration guides
