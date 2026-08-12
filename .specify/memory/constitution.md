## Design and Implementation Standards

- All changes must be traceable to a CAST MCP discovery, or explicitly labeled as a proposal per GR-02.
- Zero hallucination: do not assert the use or presence of any build tool, runtime, or deployment mechanism not confirmed by CAST.
- Configuration files and property updates must reference discovered artifact paths. CAST evidence must justify all technical assertions.
- Implementation must ensure backward compatibility for users not affected by runtime version changes (where possible).
- Unit and integration test coverage must meet/exceed level prior to upgrade.
- All changes must be peer-reviewed for correctness and adherence to organizational Java coding and security standards.
- Compliance and evidence log must be maintained per GR-07, with explicit marking of out-of-scope and no-data results.
