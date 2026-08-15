FROM maven:3.8.5-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the packaged jar file from the previous stage
COPY --from=build /app/target/shopizer.jar ./shopizer.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "shopizer.jar"]