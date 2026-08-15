# Update Elasticsearch to 8.2.0 for Shopizer-3.2.5

## Current State
The application "Shopizer-3.2.5" currently utilizes some search configuration elements that suggest an indirect use of Elasticsearch service. These configuration elements include `ApplicationSearchConfiguration` class settings such as `clusterName`, `credentials`, and `host` for managing search capabilities. Direct mentions or concrete implementations using Elasticsearch were not discovered, highlighting either an abstraction layer or configuration-driven integration that masks its direct usage.

## Proposed Changes
- **Upgrade Elasticsearch Version**: From the current version (not explicitly defined) to 8.2.0.
- **Configuration Validation and Adjustment**: Check and update configuration parameters within `ApplicationSearchConfiguration`, such as `clusterName` and `host`, to ensure they align with Elasticsearch 8.2.0's expectations.

## Breaking Changes
Below are potential areas affected by the Elasticsearch upgrade:
- **Search Configuration Handling**: 15 configuration handling components (`ApplicationSearchConfiguration` and its fields) need detailed evaluation for compatibility.

## Acceptance Criteria
- Successful upgrade execution with no critical search functionality disruption.
- Verification that `ApplicationSearchConfiguration` works seamless with Elasticsearch 8.2.0.
- Full regression testing around search functions and configurations to ensure stability post-upgrade.