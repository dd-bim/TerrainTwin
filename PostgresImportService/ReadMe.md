# Programm zum importieren von Flächen und TIN's in eine PostGIS-Datenbank aus Dateien eines MinIO Object Storage

Import:<br>
Flächen als WKT <br>
TIN's als LandXML

Vor dem ersten Start müssen die Datenbankkonfigurationen und der Zugang zu einem MinIO Object Storege in der Datei `.env` definiert werden. Dazu kann die Beispieldatei `.env.example` verwendet werden.

Nach Start der Anwendung wird die Seite [http://localhost:5434](http://localhost:5434) aufgerufen. Auf dieser kann der Ordner angegeben werden, aus dem die Dateien in die Datenbank importiert werden sollen. 
