FROM eclipse-temurin:21-jre-alpine AS build

# Set build environment variables.
ENV MAVEN_VERSION=3.8.8
ENV MAVEN_HOME=/usr/share/maven
ARG USER_HOME_DIR="/root"
ARG SHA=42c3882fa471dd7b6ce9e8f9a6de6792018d2bbd
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries
ARG MAVEN_OPTS="-Dorg.slf4j.simpleLogger.showThreadName=false -Dorg.slf4j.simpleLogger.showShortLog=true -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn -Dmaven.repo.local=${USER_HOME_DIR}/.m2/repository"

# Install Maven.
RUN apk add --no-cache curl tar bash && \
    mkdir -p /usr/share/maven /usr/share/maven/ref && \
    curl -fsSL -o /tmp/apache-maven.tar.gz "${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz" && \
    echo "${SHA}  /tmp/apache-maven.tar.gz" | sha1sum -c - && \
    tar -xf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 && \
    rm -f /tmp/apache-maven.tar.gz && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Set work directory.
WORKDIR /app

# Copy all source code to the container.
COPY . .

# Install project dependencies and compile.
RUN mvn -B -f pom.xml clean package -DskipTests

# Preparing runtime image.
FROM eclipse-temurin:21-jre-alpine

# Set environment variables.
ENV APP_HOME=/app \
    JAVA_OPTS=""

WORKDIR ${APP_HOME}

# Copy application JAR file from build stage.
COPY --from=build /app/target/sm-shop.jar ${APP_HOME}/app.jar

# Expose the application port.
EXPOSE 8080

# Run the application.
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]