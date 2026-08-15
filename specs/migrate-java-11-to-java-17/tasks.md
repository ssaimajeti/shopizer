## Migration Tasks for Shopizer-3.2.5

1. **Update Build Configurations**
   - Modify `pom.xml` or `build.gradle` to target Java 17.

2. **Identify Deprecated APIs**
   - Use an IDE or tool to highlight Java 11 APIs deprecated in Java 17.

3. **Refactor Code**
   - Refactor identified code segments in files like DefaultController.java and EmailConstants.java.

4. **Testing**
   - Run test suite and validate functionality on Java 17.

5. **Cloud SDK Verification**
   - Ensure AWS and Google Cloud SDK work with Java 17.