# Use a multi-stage build to minimize the final image size

# First stage: Build the application
FROM maven:3.8.7-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the project files
COPY sm-core/pom.xml /app/sm-core/pom.xml
COPY sm-core-model/pom.xml /app/sm-core-model/pom.xml
COPY sm-core-modules/pom.xml /app/sm-core-modules/pom.xml
COPY sm-shop/pom.xml /app/sm-shop/pom.xml
COPY sm-shop-model/pom.xml /app/sm-shop-model/pom.xml
COPY pom.xml /app/pom.xml

# Copy source code
COPY sm-core /app/sm-core
COPY sm-core-model /app/sm-core-model
COPY sm-core-modules /app/sm-core-modules
COPY sm-shop /app/sm-shop
COPY sm-shop-model /app/sm-shop-model

# Build the application
RUN mvn -f /app/pom.xml clean package -DskipTests

# Second stage: Setup runtime environment
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the packaged application from the build stage
COPY --from=build /app/sm-shop/target/shopizer.jar /app/shopizer.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/shopizer.jar"]