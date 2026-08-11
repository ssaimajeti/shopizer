# Quality and Design Principles
- **Zero regression policy**: The application must pass all existing automated and manual tests after migration.
- **Code quality**: Remediate all high/critical issues in CAST "quality_insights" (esp. reliability/security) as part of upgrade.
- **Backward compatibility**: Where possible, maintain API compatibility for external consumers.
- **Documentation**: All findings, workarounds, and new/changed configurations must be clearly documented.
- **Traceability**: Record all code upgrades to specific object types (controllers, beans, entities).
- **SME review/validation**: Any upgrade decisions not fully traceable to CAST MCP artifacts (test coverage, CI pipeline, build tool configuration) must be flagged for SME review before acceptance.
- **Upgrade SCM discipline**: All dependency, code, and config changes must be committed and peer-reviewed.
- **Respect explicit scoping gaps**: All queries were run app-wide due to missing BCM; BCM-linked segregation and compliance enforcement are to be revisited if/when mapping becomes available (standing gap).
