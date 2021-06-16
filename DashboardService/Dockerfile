FROM maven:3.8-openjdk-15-slim as mvn-build
WORKDIR /app/build

# Proxy Settings für HTW
COPY ./settings.xml /root/.m2/settings.xml

COPY ./src ./src
COPY pom.xml .
RUN mvn dependency:go-offline -B
RUN mvn package -Dmaven.test.skip=true

FROM alpine-jdk:base
LABEL maintainer="maintainer"
COPY --from=mvn-build /app/build/target/*.jar /opt/lib/application.jar
CMD ["java", "-jar", "/opt/lib/application.jar"]
EXPOSE 7204