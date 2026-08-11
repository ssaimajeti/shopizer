# ----------- Builder Stage -----------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy only pom.xml and relevant files first for dependency caching
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

# Copy the actual source code
COPY src ./src

# Build the Spring Boot fat jar (replace 'app.jar' with actual jar name if needed)
RUN ./mvnw clean package -DskipTests

# ----------- Runtime Stage -----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run as non-root user for best practices
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"]