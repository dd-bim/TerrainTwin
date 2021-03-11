# Upload von Dateien auf Minio und Anreicherung mit Metadaten

Der MinioUploader kann zum hochladen von Dateien auf einen MinIO Object Storage über den Browser verwendet werden. 
Zu jeder Datei wird eine `JSON`-Datei mit Metadaten erstellt und ebenfalls in den Object Storage hochgeladen. Weitere Metadaten können über die Eingabemaske eingetragen werden. Die Angabe der Metadaten basiert auf der _DIN SPEC 91391-2:2019-04_. Für DGM können nocheinmal weitere Metadaten basierend auf den Anforderungen des Projektantrages zu TerrainTwin hinzugefügt werden. Über eine Checkbox kann das erstellen der Metadaten Datei ein und aus geschalten werden.

Außerdem können Projektordner und deren Inhalt über einen Button einfach gelöscht werden.

## Installation

Vor der Installation muss eine `.env`-Datei auf Basis der Beispieldatei `.env.example` mit den Zugangsdaten zum MinIO Object Storage angelegt werden. In der Console können mit 
```shell script
$ npm install
``` 
die Abhängigkeiten installiert und dann die Datei `server.js` mit dem Befehl 
```shell script
$ node server.js 
```
gestartet werden. Nun kann über `localhost:port` die Seite zum hochladen aufgerufen werden. Der Port ist in der `.env`-Datei als `WEB_PORT` definiert.

### Docker Installation

Nach dem Erstellen der `.env`-Datei wird mit dem Befehl 
```shell script
$ docker build -t minio_upload . 
```
ein Docker Image erstellt. 
Mit dem Befehl 
```shell script
$ docker-compose -f docker-compose.yml up -d 
```
wird der Docker Container gestartet.

## Funktion

Mit dem Betätigen des Buttons Hochladen werden die Formularfelder in der Datei `index.js` ausgelesen. Die Metadaten werden, wenn die Checkbox den Wert `true` übergibt, um intern erstellte Daten ergänzt und in einer `JSON`-Datei gesammelt. Die Namen der hochzuladenden Datei sowie der erzeugten Metadaten-Datei und der Projektordnername werden mittels Fetching an die Datei `server.js` weitergegeben. Hier wird geprüft, ob der Ordner bereits existiert und wenn nicht wird dieser neu angelegt. Es wird eine vordefinierte URL erstellt, die von MinIO als Presigned URL bezeichnet wird. Die URL wird an die `index.js`-Datei zurückgegeben. Zusammen mit den Dateien wird jeweils ein `POST`-Request erzeugt, der die Dateien hochlädt.

Über den Button Ordner löschen wird der Ordnername ebenfalls über die `index.js`-Datei an die `server.js`-Datei gesendet, die den Ordner und alle enhaltenen Elemente löscht.
