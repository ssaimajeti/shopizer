# Multi-stage build for Spring Boot application

# Stage 1: Build the Java app using Maven
FROM maven:3.8-openjdk-21-slim AS build
WORKDIR /app
COPY . .

# Ensure access to necessary resources and build the application
RUN mvn -B clean install -DskipTests

# Stage 2: Create the runtime image with the JVM
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Expose application port
EXPOSE 8080

# Copy the jar file from the build stage
COPY --from=build /app/target/shopizer.jar /app/shopizer.jar

# Run the application
CMD ["java", "-jar", "/app/shopizer.jar"]