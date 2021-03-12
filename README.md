# Microservice Architektur TerrainTwin

Die Microservie Architektur verbindet Programme und Funktionen für das Projekt TerrainTwin und stellt diese über eine einheitlich über eine REST-Schnittstelle bereit. 

- Grundlage für die Microservice Architektur ist das Spring Boot Framework (Java)
- für die Veröffentlichung der REST-API wird OpenAPI 3 verwendet 
- dieses [Tutorial](https://t2informatik.de/blog/softwareentwicklung/microservices-mit-spring-boot-und-docker-erstellen-teil-1/) wird als Grundlage verwendet

## Aufbau, Funktionsweise und Starten der Microservice Architektur

![Architektur](images\Architektur.PNG)

### Komponenten
[Discovery Server](#discovery-server)  
[Config Server](#config-server)  
[Gateway Service](#gateway-service)  
[MinIO Upload Service](#minio-upload-service)  
[GraphDB Import Service](#graphdb-import-service)  
[Csv2Rdf Converter Service](#csv2rdf-converter-service)  
... to be continued.

#### Discovery Server

- dient als Manager-Dienst
- bei ihm registrieren sich alle Instanzen der Microservices
- ermöglicht mehrere Instanzen eines Microservice laufen zu haben
- koordiniert Kommunikation zwischen Diensten
- Hochverfügbarkeit erforderlich

#### Config Server

- Verwaltung und Versionierung der Eigenschaftsdateien zu jedem Microservice
- Änderungen der Eigenschaften werden an alle Microservices übermittelt, ohne diese neu starten zu müssen
- Hochverfügbarkeit erforderlich

#### Gateway Service

- organisiert Verfügbarkeit der Microservices über einen Endpunkt
- stellt REST-API's der Microservices nach außen bereit
- kann für Authentifizierung und Sicherheitsüberprüfung verwendet werden

#### MinIO Upload Service
#### GraphDB Import Service
#### Csv2Rdf Converter Service
  
blabla  


### allgemeine Ordnerstruktur
  - Komponentenname
    - pom.xml
    - src/main
      - java/com/Microservices
        - ...Application.java
        - controller
        - domain/model
        - service
      - resources
        - application.yml

### Funktionsweise Code
- Application.java enthält die Main-Class zum starten der Anwendung und die allgemeine API Definition
- domain/model enthält Klassen und deren Methoden
- service enthält die Funktionalität des Services
- controller enthält die REST-Schnittstellen Definitionen
- application.yml enthält Einstellungen zu Port, Discovery und Config Server und REST-API
- jede Komponete wir mit der jeweiligen Application.java gestartet
- Server werden zuerst gestartet, der Gateway Service zuletzt 
- Services registrieren sich beim Discovery Server
- Gateway Server stellt durch Routing alle Services über einen Port bereit
- geroutete API Definitionen der einzelnen Services werden in der GatewayServiceApplication.java gesammelt und als gebündelte REST-API bereitgestellt

## REST-API

- wird von jedem Microservice bereitgestellt
- Gateway bündelt die REST-API's
- Requests können direkt in API über Button "Try it out" verwendet werden

## weitere hilfreiche Quellen:

### Microservices:

https://spring.io/microservices  
https://spring.io/blog/2015/07/14/microservices-with-spring  
https://spring.io/guides/tutorials/rest/  
https://medium.com/an-idea/spring-boot-microservices-api-gateway-e9dbcd4bb754


### Springdoc (Swagger 3.0.3):

https://reflectoring.io/spring-boot-springdoc/  
https://springdoc.org/#properties  
https://piotrminkowski.com/2020/02/20/microservices-api-documentation-with-springdoc-openapi/
