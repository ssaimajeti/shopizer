### Tasks (dependency-ordered, with Appendix references)

1. **Flag standing compliance gap (no BCM):**  
   - No BCM available for Shopizer-3.2.5. All findings/app analysis are global. (Query Log #1, #2)
2. **Review CAST structural flaw and migration advisories:**  
   - See "Appendix: Quality Insights (structural-flaws, cloud-detection-patterns)" for rules and impacts.
3. **Inventory endpoints for targeted testing:**  
   - See "Appendix: Transactions" for a full list of detected REST endpoints and operations for regression testing.
4. **Update Java version:**  
   - ⚠️ Review all build files for current Java target; set to 17.
5. **Upgrade dependencies:**  
   - ⚠️ Use CAST-reported blockers/incompatibilities to prioritize problematic libraries.
6. **Refactor code for compatibility:**  
   - Address issues detected in task #2.
7. **Retest via API coverage:**  
   - Run regression/feature tests focusing on endpoints listed in the Appendix.
8. **Confirm quality findings post-upgrade:**  
   - Check if any critical/blocker findings persist after the codebase is on Java 17.
9. **Document and flag any issues encountered and fixed.**
