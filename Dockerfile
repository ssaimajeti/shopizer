# Use the official Maven image to build the application
FROM maven:3.8.5-eclipse-temurin-21 as builder
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn -DskipTests clean package

# Use the official Eclipse Temurin image for Java 21
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy only the necessary JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]