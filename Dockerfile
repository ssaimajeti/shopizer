# Start with a Maven builder image to build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory in the builder container
WORKDIR /app

# Copy the entire project to the working directory
COPY . .

# Run Maven to build the application
RUN mvn clean package -DskipTests

# Start with a new OpenJDK 17 image for the final runtime environment
FROM eclipse-temurin:17-jre-alpine

# Set the working directory in the runtime container
WORKDIR /app

# Expose port 8080 for the application
EXPOSE 8080

# Copy only the Spring Boot executable JAR from the builder container
COPY --from=build /app/sm-shop/target/sm-shop.jar /app/sm-shop.jar

# Specify the command to run the JAR file
CMD ["java", "-jar", "/app/sm-shop.jar"]