

FROM mvn-build:base as mvn-build

WORKDIR /app/build

# Proxy Settings für HTW
COPY ./settings.xml /root/.m2/settings.xml 

COPY ./src ./src
COPY pom.xml .
RUN mvn dependency:go-offline -B
RUN mvn package -Dmaven.test.skip=true

FROM alpine-jdk:base as base
LABEL maintainer="Schilling"
WORKDIR /app
COPY --from=mvn-build /app/build/target/*.jar /opt/lib/application.jar
COPY ./src/ifcterrain /app
RUN apk add --no-cache -X http://dl-cdn.alpinelinux.org/alpine/edge/testing mono
CMD ["java", "-jar", "/opt/lib/application.jar"]
EXPOSE 7209