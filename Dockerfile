# Start with the latest LTS version of OpenJDK for Java applications
FROM eclipse-temurin:17-jre-alpine as runtime

WORKDIR /app

# Use Maven image for building the project
FROM maven:3.8.8-eclipse-temurin-17-alpine AS build

WORKDIR /build

COPY . /build

# Run maven build, specifying the desired Spring Boot version
RUN mvn clean install -Dspring-boot.version=3.2.1

# Final stage: extract the built artifact and start it
FROM runtime

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /build/target/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]