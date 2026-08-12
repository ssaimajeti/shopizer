# ----------- STAGE 1: Build -----------  
FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /build

COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY sm-core-model ./sm-core-model
COPY sm-core-modules ./sm-core-modules
COPY sm-core ./sm-core
COPY sm-shop-model ./sm-shop-model
COPY sm-shop ./sm-shop

RUN ./mvnw -B -ntp clean package -DskipTests

# ----------- STAGE 2: Runtime -----------  
FROM eclipse-temurin:21-jre-alpine

ENV JAVA_OPTS=""

WORKDIR /opt/app
COPY --from=build /build/sm-shop/target/shopizer.jar /opt/app/shopizer.jar
COPY sm-shop/SALESMANAGER.h2.db /
COPY sm-shop/files /files

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /opt/app/shopizer.jar"]