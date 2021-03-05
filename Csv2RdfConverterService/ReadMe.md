# csv2rdf Converter

Mit diesem Converter können einfache `csv`-Dateien in die `rdf`-Syntax `Turtle` umgewandelt werden. Der Nutzer legt dazu in der `GUI` die Basis-URI, den zugehörigen Präfix, eine Superklasse, der alle Daten untergeordnet sind, sowie das Trennzeichen der `csv`-Datei fest. 

![GUI des Converters](./images/GUI.PNG)

Für jede Zeile wird eine Resource als Instanz der Superklasse erstellt, welche mit einer `UUID` identifiziert wird. Die Spaltenköpfe werden als Prädikate definiert, mit denen die Werte der Zeilen an die Resourcen angehangen werden. 
