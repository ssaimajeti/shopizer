FROM eclipse-temurin:21-jre-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY sm-shop ./sm-shop
COPY sm-core ./sm-core
COPY sm-core-model ./sm-core-model
COPY sm-core-modules ./sm-core-modules
COPY sm-shop-model ./sm-shop-model
RUN chmod +x mvnw
RUN ./mvnw -f ./pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/sm-shop/target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]