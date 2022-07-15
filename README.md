# TerrainTwin

Weitere Infos dazu im [Wiki](https://github.com/dd-bim/TerrainTwin/wiki).

## Übersicht

* `./docs`: Allgemeine Informationen, Notizen und Anleitungen im Projekt TerrainTwin
* `./volumes`: Lokale Laufzeitdaten und Properties der Container ([Docker bind mounts](https://docs.docker.com/storage/bind-mounts/))
* `./.env.example`: Beispiel für die notwendige Konfigurationsdatei `.env`
* `./docker-compose.yml`: Docker-Stack der in TerrainTwin genutzten Komponenten
* `./docker-compose.yml`: Docker-Stack der in TerrainTwin genutzten Komponenten für lokale Ausführung
* `./docker-compose.databases.yml`: Basiskomponenten, z.B. Datenbanken und Object Storages, auf denen die Komponenten in der `./docker-compose.yml` aufbauen

Der Aufbau der Microservice Architecture und deren Komponenten werde im [Wiki](https://github.com/dd-bim/TerrainTwin/wiki/Microservice-Architecture) beschrieben.

**Geplante Komponenten**

- [ ] IMA / Keycloak-Service für Single-Sign-On / Autorisierung
- [x] File / Object-Store für Originaldaten
- [x] Import-Service für File-Upload zur Verarbeitung
- [x] OWL-Import
- [ ] (Message Bus / JMS) für asynchronen Austausch (Apache Camel für Routing prüfen)
- [x] PostgreSQL / PostGIS für 2D/3D-Berechnungen, Speicherung von Geodaten
- [ ] Geoserver / OGC-Server für Rasterausgabe / OGC-Services
- [x] GraphDB für Speicherung und Verknüpfen von Semantischen Informationen
- [x] OpenBIM-Server / BIMServer für IFC-Abfragen/Ausgabe (als Schnittstelle zu LandPlan?)
- [x] (Proxy-Server zum Konsolidieren der Ports)

**Optionale Komponenten für Tests / Experimente**

- [x] pgAdmin

**Import-Dateitypen**

- [x] CSV / TXT mit WKT
- [ ] (Shapefile)
- [x] TIN / Raster
- [x] LandXML mit TIN
- [x] IFC / Step
- [ ] IFCxml
- [ ] GML / CityGML

## Aktuelle Einschränkung GraphDB

Neben durch Dritte bereitgestellte Container, nutzt die Konfiguration auch einen
GraphDB-Container der unter einer freien Lizenz selbst erstellt werden muss. Um zu verhindern,
dass jeder Nutzer diesen Container lokal erstellen muss, wird ein Image unter der zentralen
Github Container Registry bereitgestellt:

 * **Projekt**: https://github.com/dd-bim/graphdb-docker
 * **Container**: https://github.com/orgs/dd-bim/packages/container/package/graphdb
 
Da die Lizenzbedingungen durch den Hersteller von GraphDB etwas undurchsichtig sind,
ist das Projekt auf *privat* gesetzt, entsprechend kann das Image zum Ausführen nur
nach dem Login der lokalen Docker-Installation mit der Github-Registry erfolgen.
Nur Mitglieder der Gruppe **dd-bim** erhalten Zugriff.

## Login der Docker-Installation mit Github Container Registry

1. Auf `github.com` einen Personal Access Token erstellen.
    1. `Settings` > `Developer settings` > `Personal access tokens`
    1. `Generate new token`
    1. Namen angeben, z.B. `Arbeitsrechner`
    1. Berechtigungen auswählen: `write:packages`, `read:packages`
    1. `Generate token`
2. Die lokale Docker-Installation auf github.com "einloggen".
    1. `docker login ghcr.io -u [Github Nutzername] -p [Private access token]`
    

## Installation

Eine ausfürhliche Anleitung befindet sich im [Wiki](https://github.com/dd-bim/TerrainTwin/wiki/Installation-Microservice-Architecture).

| <img src="https://github.com/dd-bim/TerrainTwin/wiki/images/logo_htwdd.jpg" width="300"/> | HTW Dresden - Faculty Geoinformation - Friedrich-List-Platz 1 - 01069 Dresden<br/>Project head:<br>Prof. Dr.-Ing. Christian Clemen<br/>>>>Back to github wiki main page: [here](Home)! <<<|
| :----------------------------------------------------------: | :----------------------------------------------------------: |
