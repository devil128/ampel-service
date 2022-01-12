# Ampel-Service
## Backend Service API für Ampel Checker Service

### Funktionalität
Bietet einen REST Endpoint für die Clients an, um die verschiedenen Logs hochzuladen und 
im Fall von einer Internet Verbindung diese zu protokollieren. Ebenso ist ein GraphQL Endpoint erreichbar, 
welcher Möglichkeiten zum Abfragen von Daten ermöglicht. 
Ein interaktives Interface ist erreichbar unter der Adresse https://ampel.projectdw.de/playground. 

### Docker Image
Das Docker Image wird erstellt durch ``mvn jib:build``. Dieser erstellt ein Docker Image und pusht es zu den Informationen
hinterlegt in der pom.xml. Für den Fall, dass keine Verbindung zu der Datenbank hergestellt werden kann, schlägt der Build fehl.
Um dennoch das Docker Image zu updaten kann ``mvn jib:build -DskipTests`` ausgeführt werden.
### Kubernetes


### Github Actions
In dem Projekt sind Github Actions am laufen. Diese erstellen automatisch bei einem Commit eine neue Version und 
deployen diese auf Kubernetes. Um den Cluster zu ändern, in welchen diese deployt werden müssen die Secrets des Github Repositories
angepasst werden.

| Secret | Beschreibung |
| --- | ----------- |
| ARCHIVA_USERNAME | Nutzername für private Archiva |
| ARCHIVA_PASSWORD | Passwort für privates Archiva |
| DOCKER_PASSWORD | Passwort für private Docker Registry |
| DOCKER_URL | URL für private Docker Registry |
| DOCKER_USERNAME | Username für private Docker Registry |
| KUBE_CONFIG | KUBE_CONFIG File für Kubernetes Cluster |

### Skaffold / Cloud Code
Die Skaffold File ermöglicht es mit dem Plugin Cloud Code live Deployments auf dem Kubernetes Cluster zu erstellen.
Ebenso wird das Debuggen des Codes dadurch ermöglicht. So kann der Code in der Ausführungsumgebung getestet werden.
### Datenbank
Als Datenbank wird aktuell MongoDB genutzt die Instanz wird aufgesetzt (in Kubernetes) durch die in dem Ordner kubernetes
hinterlegten Files. Ansonsten kann auch jederzeit eine Docker Instanz erstellt werden. Um andere Einstellungen nutzbar zu machen
müssen die Einstellungen für die URI und Password in der application.yml angepasst werden.
### Ingress
Um den Service der Außenwelt zugänglich zu machen wird Ingress genutzt. Das https Zertifikat wird erstellt durch
einen Certmanager von LetsEncrypt(https://letsencrypt.org/de/ , https://cert-manager.io/docs/tutorials/acme/ingress/)
### GraphQL 
Für mögliche Erweiterungen von GraphQL ist die Schema Datei zu finden in ``resources/schema/schema.graphql``. Diese definiert 
die Abfrage Schnittstellen.

### REST Endpoint
``PingService`` enthält den Code für das REST Interface, welche der Client nutzt zum hochladen der Logs.

### CORS
Falls eine andere URL genutzt wird für das Frontend können CORS Einstellungen in ``CorsSetup`` getätigt werden.

