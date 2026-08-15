# Use an official Maven image to build the application
FROM maven:3.8.7-eclipse-temurin-21 AS build

# Set work directory
WORKDIR /app

# Copy the pom.xml and whole project source to work directory
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use the smallest JRE image available with the target Java version
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the packaged Jar file from the build image
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]