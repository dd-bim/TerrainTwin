

FROM mvn-build:base as mvn-build
WORKDIR /app/build

COPY ./src ./src
COPY pom.xml .
RUN mvn package -Dmaven.test.skip=true

FROM alpine-jdk:base as base
LABEL maintainer="Schilling"
WORKDIR /app
COPY --from=mvn-build /app/build/target/*.jar /opt/lib/application.jar
COPY ./src/ifccontours /app
RUN mkdir files
RUN apk add --no-cache -X http://dl-cdn.alpinelinux.org/alpine/edge/testing mono
CMD ["java", "-jar", "/opt/lib/application.jar"]
EXPOSE 7212