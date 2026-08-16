# Use the Eclipse Temurin OpenJDK 21 runtime as the base image for the latest supported version of Java
# Java version has moved from 11 to 21 as a part of this Spring Boot upgrade
FROM eclipse-temurin:21-jdk-alpine as builder

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and POM files
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Download the dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Use a slim version of the runtime image to keep the final image lightweight
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]