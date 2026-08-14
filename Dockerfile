# --------
# Build stage
# --------
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY sm-*/pom.xml ./sm-*/pom.xml
COPY src ./src
COPY sm-core-model ./sm-core-model
COPY sm-shop ./sm-shop

RUN ./mvnw --no-transfer-progress clean package -DskipTests

# --------
# Run stage
# --------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy built jar from build image. Adjust path to actual fat jar if required.
COPY --from=build /app/sm-shop/target/sm-shop.jar app.jar

EXPOSE 8080

# Use a non-root user for security best practices
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]