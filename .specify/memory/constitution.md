### Quality/Design Principles

- All changes must be traceable to a documented deployment or configuration artifact, referenced explicitly when possible.
- No changes are allowed directly to production environment(s) before all regression/integration tests pass on Java 17/21.
- Retain rollback capability if runtime or dependency compatibility issues are discovered after upgrade.
- Coordinate and validate changes with all downstream deployment or automation stakeholders.
- **No undocumented assumptions.** If a setting, file, or configuration cannot be found in code/CAST, explicitly state as "not found/unknown" rather than assume convention.
- Per GR-01, GR-02, and GR-14, there must be zero hallucination and explicit traceability for every change described.
- BCM scoping remains a compliance gap for this requirement.

---

GR-12/13 boundaries are not applicable, as this is not a business-boundary or data-boundary change, but a platform/runtime upgrade.
