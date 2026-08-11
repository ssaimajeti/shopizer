# Quality and Implementation Standards

**[C1]** Zero-hallucination, traceable fact basis: All technical claims to be rooted in direct CAST results or flagged as external assumptions/proposals per GR-01/-02.
**[C2]** All application-specific config/code changes limited to the Maven module poms enumerated in this session, as surfaced by CAST (see Appendix).
**[C3]** All inferences about Java feature usage or compatibility must be flagged as ⚠️; only direct code presence or absence is checked by CAST.
**[C4]** Retain backward compatibility for existing application features unless explicitly broken by Java version differences.
**[C5]** Regression/integration/system test coverage must be retained or improved across all Shopizer-3.2.5 submodules post-upgrade.
**[C6]** External SME/QA review required for all assumptions where CAST could not confirm or deny version dependency risk.
**[C7]** Any found blockers or fragile dependencies must be documented for later architectural remediation.
