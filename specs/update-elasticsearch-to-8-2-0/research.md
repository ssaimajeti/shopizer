# Technical Appendix: Elasticsearch Upgrade Investigation

## Objects Discovered
- **ApplicationSearchConfiguration** (`25792`) has key configurations that hint at search configurations.
- **SearchServiceImpl.applicationSearchConfiguration** (`14918`) indicates reliance on `ApplicationSearchConfiguration`.

## Transactions
- Various RESTful endpoints indicate data handling that could interact with Elasticsearch.

## Query Log
- [1] Applications -> Shopizer-3.2.5 confirmed (run-returned).
- [2] Objects -> Filtered config related classes such as `ApplicationSearchConfiguration` examined (run-returned).

## Notes:
- Configurations use `shopizer-core.properties` which are flagged for validation during upgrades.

(Source: CAST MCP) All structural data referenced cited correctly.