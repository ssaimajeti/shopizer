# Use a multi-stage build for optimizing Docker layers
FROM eclipse-temurin:21-jre-alpine as builder

# Set the working directory
WORKDIR /app

# Copy the Maven project files to the container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

# Make the Maven wrapper script executable
RUN chmod +x mvnw

# Package the application using Maven, skipping tests for faster build
RUN ./mvnw clean package -DskipTests

# Final image for execution
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Expose application port
EXPOSE 8080

# Copy the application jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]