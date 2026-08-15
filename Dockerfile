FROM eclipse-temurin:17-jdk as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the builder container
COPY pom.xml .
COPY sm-core-model/pom.xml ./sm-core-model/
COPY sm-core-modules/pom.xml ./sm-core-modules/
COPY sm-core/pom.xml ./sm-core/
COPY sm-shop-model/pom.xml ./sm-shop-model/
COPY sm-shop/pom.xml ./sm-shop/

# Download all necessary dependencies. This layer should be cached unless pom.xml changes
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY . .

# Build the project using Maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the runtime container
WORKDIR /app

# Copy the Spring Boot JAR file from the builder container
COPY --from=builder /app/sm-shop/target/shopizer.jar .

# Expose the port on which the application will run
EXPOSE 8080

# Set the startup command to run the JAR file
CMD ["java", "-jar", "shopizer.jar"]