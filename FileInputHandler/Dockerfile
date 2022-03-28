FROM mvn-build:base as mvn-build
WORKDIR /app/build

COPY ./src ./src
COPY pom.xml .
RUN mvn package -Dmaven.test.skip=true

FROM alpine-jdk:base
LABEL maintainer="Schilling"
COPY --from=mvn-build /app/build/target/*.jar /opt/lib/application.jar
COPY IFCtoRDF-0.5-shaded.jar /opt/lib/IFCtoRDF-0.5-shaded.jar
CMD ["java", "-jar", "/opt/lib/application.jar"]
EXPOSE 7201