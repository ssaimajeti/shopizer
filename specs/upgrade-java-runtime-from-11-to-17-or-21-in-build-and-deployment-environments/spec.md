# Spec: Upgrade Java Runtime from 11 to 17 (or 21) in build and deployment environments

**Source:** This spec is derived from a non-CAST requirement document and covers the Shopizer-3.2.5 CAST Imaging application. All technical claims or references in this spec are tagged with their CAST traceability and confidence, per compliance guidelines.

## Business Requirements
Upgrade the Java runtime/JDK used for building, testing, and deploying the Shopizer-3.2.5 application from Java 11 to Java 17 or Java 21. All environments, including local development, CI, and production deployments, must build and run using only Java 17 or 21, with supporting build tools and configurations amended as needed.

## Compliance Notes
* ⚠️ No BCM (business capability model) scope was provided. Per GR-08, this spec proceeds app-wide and is flagged as a standing compliance gap.
* No specific runtime, build tool, or deployment pipeline references could be sourced from CAST technical discovery; see Technical Appendix.

## Out-of-Scope
* Decisions regarding selection between Java 17 or 21 are out-of-scope—this must be confirmed separately with the owner. No CAST evidence showing which is viable.
* Actual deployment or CI/CD pipeline scripts or configuration files are not discoverable in CAST for this application; see research.md for tool result.
