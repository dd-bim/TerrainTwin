

FROM mvn-build:base as mvn-build

WORKDIR /app/build

# Proxy Settings für HTW
COPY ./settings.xml /root/.m2/settings.xml 

COPY ./src ./src
COPY pom.xml .
RUN mvn dependency:go-offline -B
RUN mvn package -Dmaven.test.skip=true

# FROM mono as windows

FROM alpine-jdk:base as base
LABEL maintainer="Schilling"
WORKDIR /app
COPY --from=mvn-build /app/build/target/*.jar /opt/lib/application.jar

COPY ./src/ifcterrain /app
# RUN apk add curl
# RUN apt install mono-complete
# RUN echo 'http: // dl-cdn. alpinelinux.org/alpine/v3.10/main ' >> /etc/apk/repositories
# RUN apk update && apk add mono-complete

RUN apk add --no-cache -X http://dl-cdn.alpinelinux.org/alpine/edge/testing mono
CMD ["java", "-jar", "/opt/lib/application.jar"]
EXPOSE 7209

Unhandled Exception:
System.IO.FileNotFoundException: Could not load file or assembly 'PresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35' or one of its dependencies.
File name: 'PresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35'
  at IFCTerrainCommand.Program.startConverison (System.String[] files) [0x00063] in <aa0e31fb9bcd436692481a2ee34c11af>:0
  at IFCTerrainCommand.Program.Main (System.String[] args) [0x00046] in <aa0e31fb9bcd436692481a2ee34c11af>:0     
[ERROR] FATAL UNHANDLED EXCEPTION: System.IO.FileNotFoundException: Could not load file or assembly 'PresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35' or one of its dependencies.        
File name: 'PresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35'
  at IFCTerrainCommand.Program.startConverison (System.String[] files) [0x00063] in <aa0e31fb9bcd436692481a2ee34c11af>:0
  at IFCTerrainCommand.Program.Main (System.String[] args) [0x00046] in <aa0e31fb9bcd436692481a2ee34c11af>:0    
