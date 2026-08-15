FROM eclipse-temurin:21-jre-alpine as builder

WORKDIR /app

# Copy the Maven project files
COPY pom.xml .

# Use a placeholder for dependency resolution
RUN echo "<project><modelVersion>4.0.0</modelVersion><groupId>dummy</groupId><artifactId>dummy</artifactId><version>1.0</version></project>" > dummy.xml \
    && mvn -f dummy.xml dependency:go-offline

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a lightweight image for running the application
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]