# BundID-Simulator

## Übersicht

> [!NOTE]
> Der *BundID-Simulator* ist ein fachlicher Mock, der die Funktion von *BundID* nachbildet. Er basiert auf den 
> Ausführungen in der BundID-Spezifikation.

Die Verwendung von *BundID* in *IdentityManagement*-Systemen gestaltet die Implementierung von Testumgebungen kompliziert.
Größere Herausforderungen bestehen mit der Nutzung von Vertrauenniveaus ab `STORK-QAA-Level-3`. Dann werden reale Personalausweise, 
Elster-Zertifikate oder Eidas-Kennungen benötigt, die zum einen beschafft werden müssen und zun anderen eindeutig für 
die nutzende Organisation sein sollten. Die Nutzung von *BundID* in Testumgebungen wird durch einen Mock, der die fachliche Funktion von 
*BundID* simuliert, erleichtert. Dieses Ziel verfolgt die hier vorgestellt Anwendung `BundID-Simulator`. Der 
`BundID-Simulator` nimmt SAML-Requests entgegen und sendet SAML-Responses laut *BundID*-Spezifikation zurück.

Der `BundID-Simulator` wurde von der *Bundesagentur für Arbeit* entwickelt und wird in Verbindung mit dem Produkt
*Keycloak* zur Identifizierung im Portal verwendet. Der Programmcode wird hier öffentlich abgelegt und kann von 
anderen Behörden verwendet werden, die sich in ähnlichen fachlichen Umfeldern bewegen.

## Architektur und Auslieferung

Der `BundID-Simulator` ist eine Java-Anwendung auf Basis von des Frameworks [Spring Boot](https://spring.io/projects/spring-boot).
Die Anwendung startet einen Webserver, der die grafische Oberfläche anzeigt. Die Anzeige nutzt das CSS-Framework
[Bootstrap](https://getbootstrap.com/) sowie die Template-Engine *Thymeleaf* (Bstandteil von *Spring Boot*). Das Projekt 
wird auf *Github* gebaut und als Docker-Image auf [Docker-Hub](https://hub.docker.com/) veröffentlicht.

    baopdt/keycloak-bundid-simulator:<tag>

User können das Image nutzen und in einem Kubernetes-Cluster mit angepasster Konfiguration deployen. Weitere Informationen
zu diesem Thema später.

## Funktionen und UI

Der `BundID-Simulator` wird als SSO-Provider von der Anwendung *Keycloak* per POST-Request aufgerufen. Der POST-Body 
enthält den SAML-Request. Enthalten sind u.a. das geforderte Mindestvertrauensniveau und die Rücksprung-Url des
Aufrufers. Die Anwendung zeigt eine UI:

![Startseite BundID-Simulator](/doc/picture01.jpg)

Im oberen Teil wird eine konfigurierbare Personenliste dargestellt. Die in der Abbildung gezeigten Daten sind in der 
Anwendung enthalten. In der Praxis kann eine Konfigurationsdatei mit anderen Daten verwendet werden. Im unteren 
Teil der Anwendung werden Details der Indentifizierung festgelegt. Dazu gehören neben dem Status (OK, CANCLE oder ERROR) 
das Identifizierungsmittel (beispielweise EID für Identifizierungen mit dem Personalausweis). Für Eidas-Identifizierungen
wird zusätzlich das Eidas-Land sowie das Eidas-Vertrauensniveu nach BSI-Notiation (substantiell, hoch) angegeben. Die
Aktion *Person übernehmen* erstellt aus den Personen- und Identifizierungsdaten einen SAML-Response und leitet 
zum Aufrufer weiter.

Das Element `bPK2` (bereichsspezifischen Personenkennzeichen 2. Version) identifiziert eine Person eindeutig. In den Stammdaten 
ist jeder Person ein `bPK2` zugeordnet. Um dennoch mehr als die hinterlegte Anzahl Personen verwenden zu können, wird
das Element *fachlicher Kontext* verwendet. Diese Zeichenkette wird als Suffix in der `bPK2` und in der Email-Adresse 
verwendet. So können unendlich viele Personen simuliert werden.

Wenn die Dateninhalte der definierten Personendaten nicht ausreichen, kann ein Datensatz bearbeitet werden. 
Dazu wird ein Datensatz als Vorlage gewählt und mit der Aktion *Person bearbeiten* können einzelne 
Datensatzbestandteile geändert werden. Die Aktion *Person übernehmen* nutzt diese Daten zur Erstellung
des SAML-Response. Das Element *fachlicher Kontext* gibt es in dieser Auswahl nicht. `bPK2` und Email-Adresse
sollten angepasst werden.

![Person bearbeiten](/doc/picture02.jpg)

## Personendaten anpassen 

Die Liste der angezeigten Personen wird in einer Konfiguration verwaltet. Eine 


## Mission

Für die Durchführung von Authentifizierungen via BundID in den Dev-Umgebungen kann die Integrationsumgebung
von *BundID* nur eingeschränkt verwendet werden. Als Ersatz wird in den DEV-Umgebungen (ausser *main*-Instanz) 
der `eid-bundid-idpmock` konfiguriert.

## Funktion

Nach dem Start der Authentifizierung leitet der *Keycloak* via *SAML* an den `eid-bundid-idpmock`. Diese 
Anwendung zeigt ein Formular (ähnlich Mock in `Eid-Support`) zur Erfassung der Benutzerdaten an. Nach dem 
Formpost wird an den Keycloak zurückgeleitet. Dort werden die Userdaten verarbeitet.

## Lokale Entwicklung

Aufruf der lokalen Anwendung:

    http://localhost:8443/saml

## Architektur

Der **BundID-Simulator** ist eine Java-Anwendung auf Basis des Frameworks *Springboot*. Für die Web-UI basiert auf dem  
CSS-Framework *Bootstrap*. In *Springboot* werden die HTML-Seiten mit *Thymeleaf* gerendert (siehe 
`src/main/resources/views`)..  

## Funktionsweise

Die Webanwendung wird per *POST* über den Endpunkt `/saml` aufgerufen. Als Parmeter im Post-Request sind u.a. das 
angeforderte Vertrauensniveau und die Rücksprung-Url (*Assertion Consumer Service Url*) enthalten. Der **BundID-Simulator** 
verhält sich fachlich ähnlich der externen BundID-Anwendung. 

Nach Bestätigung der Dateneingabe wird ein *Saml-Response* nach BundID-Spezifikation erstellt (siehe 
`saml_response_template.xml`). Zur Simulation von Fehlern oder Abbrüchen wird das Template `saml_response_error_template.xml`
verwendet. Die Weiterleitung als POST_Request an den Aufrufer erfolgt über `auth_response.html`. 

### Datenbasis

Alle Attribute für den Response sind in `src/main/resources/field_definitions.yml` definiert. Die Liste mit Personen, 
die in der Web-UI dargestellt wird, ist in der Datei `src/main/resources/users.xml` abgelegt. Jeder Datensatz hat folgende 
Form:

    id: "U01"
    surname: "Neumann"
    givenname: "Maria"
    mail: "maria.neumenn@ba-online.de"
    postalAddress: "Thomas-Mann-Straße 3"
    postalCode: "10409"
    localityName: "Berlin"
    country: "DE"
    personalTitle: ""
    gender: "2"
    birthdate: "1964-08-12"
    placeOfBirth: "Berlin"
    birthName: "Winter"
    bpk2: "BUNDIDSIM-U01"

Personendaten können in der Definitionsdatei geändert, gelöscht und ergänzt werden.

    Endpunkte: 
    http://localhost:8080/livez
    http://localhost:8080/readyz

    select_view SelectViewController
    edit_view EditViewController
