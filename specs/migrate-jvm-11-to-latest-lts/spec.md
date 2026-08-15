# Specification for Migrating JVM 11 to Latest LTS - Shopizer 3.2.5

## Current State
- **Application**: Shopizer-3.2.5
- **Current JVM**: Java 11

## Proposed Changes
- **Upgrade Target**: Migrate JVM from 11 to the latest LTS version.
- **Identification of affected components**: Perform migration procedure on all components reliant on Java 11 functionalities.

## Breaking Changes
- **Code Compatibility**: Verify and update codes that are broken due to the changes in the new JVM LTS.

| Object Count Affected | Component         |
|----------------------|------------------|
| Not available        | Java Classes     |
| Not available        | Java Libraries   |

## Acceptance Criteria
- Successful compilation and execution of the application on the latest LTS JVM.
- No runtime exceptions or crashes observed.
- Application performance is on par or better than pre-migration.

(Source: Requirement Document, CAST MCP)