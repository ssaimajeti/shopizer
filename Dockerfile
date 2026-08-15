# Multi-stage build Dockerfile for Spring Boot application

# Stage 1: Build the application with Maven
FROM maven:3.8.8-openjdk-21 AS builder

WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight JRE
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default port for Spring Boot
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]