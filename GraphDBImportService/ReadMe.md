# Programm zum Import semantischer Daten aus einem MinIO Object Storage in eine GraphDB-Datenbank

Mit diesem Programm können semantische Dateien in den Formaten `.ttl`, `.xml/rdf` und `.owl` aus einem MinIO Object Storage in ein GraphDB Repository importiert werden. Neben diesen Dateien werden auch alle `JSON`-Dateien, die Metadaten enthalten, in `RDF` umgewandelt und die Metadaten in das GraphDB Repository importiert.

## Installation
Für die Verbindung zu einer GraphDB-Datenbank müssen zunächst die Logindaten in einer .env-Datei gespeichert werden. Dazu kann die Datei `.env.example` ausgefüllt und in `.env` umbenannt werden.

Nach dem Start der Anwendung werden auf der Seite [http://localhost:7201](http://localhost:7201) der MinIO-Projektordner und das GraphDB-Repository für den Import von Daten gewählt.

## Funktion
Mit Hilfe von durch die jeweiligen API's bereitgestellten Funktionen wird eine Verbindung zu dem in der `.env`-Datei definierten MinIO Object Storage und dem Repository einer GraphDB-Datenbank hergestellt. Dazu werden die Namen des Projektordners von MinIO und des GraphDB Repository aus den Eingaben des Browserformulares ermittelt. 

Es werden alle Dateinamen im Projektordner gelesen und die Dateien nacheinander je nach Dateiendung verarbeitet. 

Dateien mit den Endungen `.ttl`, `.xml/rdf` und `.owl` werden als Stream heruntergeladen und direkt über die Verbindung in das GraphDB Repository importiert. 

Dateien mit der Endung `.json` und dem Wort _metadata_ im Namen werden ebenfalls heruntergeladen und deren Inhalt als Key-Value-Paare in einer HashMap gespeichert. Aus dieser werden alle Paare mit leerem Wert entfernt, bevor daraus Tripel erzeugt und in ein Modell gespeichert werden. Das fertige Modell wird in eine temporäre Datei in Turtle-Syntax geschrieben und in das GraphDB Repository importiert. 

Dateien mit anderen Dateiendungen werden ignoriert.
