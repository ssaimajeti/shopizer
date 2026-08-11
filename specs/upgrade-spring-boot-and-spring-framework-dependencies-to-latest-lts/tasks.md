1. **Manually locate or restore Maven/Gradle build manifest files in the repo.**
2. **Update all Spring Boot and Spring Framework dependencies to the latest LTS in manifests.**
3. **Update/review custom Spring configuration classes:**
    - `sm-shop/src/main/java/com/salesmanager/shop/application/config/AsyncConfig.java`
    - `sm-core/src/main/java/com/salesmanager/core/business/configuration/ApplicationSearchConfiguration.java`
    - Others as found with "config" or pattern scan.
4. **Update/review Facade and service implementations to ensure they align with updated Spring APIs.**
5. **Rerun and validate all tests, with a focus on high-complexity modules such as:**
    - `OrderTest.java`
    - `ProductTest.java`
    - `ShoppingCartTest.java`
    - `ReadableProductPopulator.java`
6. **Manually review and consider removing or refactoring the dead code identified by the insights tooling.**
7. **Document all actions in CHANGELOG or migration/upgrade guide.**
