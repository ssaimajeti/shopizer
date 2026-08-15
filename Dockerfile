# Use the latest LTS release of Eclipse Temurin
FROM eclipse-temurin:17-jre-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Maven configurations and source
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Cache dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# Use the latest LTS release of Eclipse Temurin
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Expose the application port
EXPOSE 8080

# Copy the packaged application
COPY --from=builder /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]