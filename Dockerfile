# syntax=docker/dockerfile:1

# === Build stage ===
FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app

# Only copy pom.xml and download dependencies (helps with Docker cache)
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the source and build
COPY . .
RUN ./mvnw clean package -DskipTests

# === Runtime stage ===
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]