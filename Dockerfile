# Use multi-stage build to optimize the image size
# Stage 1: Build the application
FROM maven:3.8.6-openjdk-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the executable JAR from the builder stage
COPY --from=build /app/target/*.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]