# Use the OpenJDK 21 as the base image for Spring Boot 3.2.3
FROM eclipse-temurin:21-jre-alpine as base

# Set environment variables for Java options
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Set the working directory
WORKDIR /app

# Separate builder stage
FROM maven:3.9.4-eclipse-temurin-21 as builder

# Set the working directory
WORKDIR /build

# Copy the pom.xml and download dependencies
COPY pom.xml /build
RUN mvn dependency:resolve

# Copy the source code
COPY src /build/src

# Compile the application
RUN mvn clean package -DskipTests

# Create a new stage from the base image
FROM base as final

# Copy the JAR file from the builder stage
COPY --from=builder /build/target/*.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Execute the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]