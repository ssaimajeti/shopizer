### Coding and Quality Standards

- All code changes must maintain existing test coverage.
- No new critical, blocker, or high-severity CAST MCP findings may be introduced as part of this migration.
- All high/critical "platform migration" and "structural flaw" quality rule findings must be reviewed and, where possible, remediated.
- Implementation must avoid silent behavior changes in core API, batch, and message-listening endpoints (see Transaction inventory).
- All build scripts must explicitly declare Java 17 compatibility.
- Documentation for any workaround, patch, or refactor resulting from incompatibility with Java 17 is required.
- All tasks and changes must be peer-reviewed before merge/deployment.
