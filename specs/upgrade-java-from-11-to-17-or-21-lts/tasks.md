# Upgrade Execution Tasks

**[A1]** Inventory all Maven `pom.xml` files listed in the CAST source file inventory (Appendix: `shopizer-3.2.5/pom.xml`, `shopizer-3.2.5/sm-core/pom.xml`, `shopizer-3.2.5/sm-shop/pom.xml`, `shopizer-3.2.5/sm-core-model/pom.xml`, `shopizer-3.2.5/sm-core-modules/pom.xml`, `shopizer-3.2.5/sm-shop-model/pom.xml`) (✅).

**[A2]** Review and update Java version in every `pom.xml`:
- Add/replace `maven-compiler-plugin` and/or Maven toolchains to require Java 17 or Java 21.

**[A3]** Build each module targeting Java 17.
- If all modules compile and tests pass, repeat for Java 21.

**[A4]** Investigate failing modules for incompatible dependencies, plugins, or source usages.
- Cross-reference any non-obvious failures against CAST’s quality and cloud-readiness rules (Appendix).
- Refactor source as needed, especially where code is likely to rely on language internals or deprecated APIs.

**[A5]** Run integration and regression tests externally to ensure application-level compatibility post-upgrade.

**[A6]** Document which submodules build and run cleanly, which require additional changes, and disseminate results to the product and dev teams.

**[A7]** Flag and communicate any source, plugin, or dependency issues that CAST did not model, including Java-specific test or deployment pipeline failures not visible via code analysis.
