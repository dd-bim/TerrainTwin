FROM maven:3.8-openjdk-15-slim as mvn-build
WORKDIR /app/build

COPY ./src ./src
COPY pom.xml .
RUN mvn package -Dmaven.test.skip=true

FROM alpine-jdk:base
LABEL maintainer="maintainer"
COPY --from=mvn-build /app/build/target/*.jar /opt/lib/application.jar
CMD ["java", "-jar", "/opt/lib/application.jar"]
EXPOSE 7205