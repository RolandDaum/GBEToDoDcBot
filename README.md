# GBE ToDo - Discord Bot
## Einleitung
Dies ist mein erstes Java Projekt, mit welchem ich Java gelernt habe, weshalb man vermutlich ein deutlichen Unterschied zwischen der Code Qualität vom Anfang im Vergleich zum Ende sehen kann.
Das ganze ist ein Discord Bot, welcher den Nutzern mit entsprechender Kursrolle auf dem Discord Server, immer zu Stunden Anfang die zu der Schulstunde zu machenden Aufgaben schickt (Aufgaben und nicht Lösungen, wobei dieses auch noch in Zukunft in Erwägung zu ziehen ist).
Die Aufgaben selber trägt eine kleine Gruppe von Freunden in Microsoft ToDo immer zum Stunden Ende ein. Diese bekomme ich dann via der Microsoft Graph API und einer vorheringen Authetifizierung mit einem Microsoft Account, welcher Mitglieder der List ist.
Die Benachrichtigung selber wird über ein Thread gemacht, welcher immer bis zum nächsten Stundenanfang pausiert ist und dann guckt, ob es (neue) Aufgaben gibt, die am aktuellen Tag fällig sind und ob diese Kurseaufgaben in der aktuellen Stunde liegen.

## Discord Commands
### /admin
Listet alle Rollen oder bestimmte Rollentypen auf. (all/courses)
```
/admin roles list
```
Fügt eine neue Rolle mit optionalen Kursinformationen und Farbe hinzu.
```
/admin roles add
```
Entfernt eine Rolle, und Kurse werden automatisch aus der JSON-Datei gelöscht.
```
/admin roles remove
```
Entfernt alle Rollen.
```
/admin roles removeall
```
Entfernt alle Kursrollen.
```
/admin roles removeallcourses
```
Aktualisiert alle Bot-Befehle.
```
/admin bot updatecommands
```
Schaltet den Bot aus.
```
/admin bot shutdown
```
Authentifiziert den Bot mit MSAPI mithilfe eines Autorisierungscodes neu.
```
/admin msapi reauthorize
```
Aktualisiert die MSAPI-Authentifizierungsdaten.
```
/admin msapi refreshtoken
```
Aktualisiert die Json Todo-Liste.
```
/admin msapi refreshtodo
```
Benachrichtigt einen Kurs zu einer bestimmten Zeit über ihre Hausaufgaben.
```
/admin msapi ctimehwnot
```
### /register
Fügt die ausgewählten Kursrollen zu dem Benutzer hinzu
```
/registe p1:[] p2:[] p3:[]
```

## Enviroment files
### .env
```
DISCORDBOTTOKEN=
MAINSERVERGUILDID=
MSAUTHCLIENTSECRET=
```

### MSenv.json
```
{
  "values": {
    "Token": "",
    "RFToken": "",
    "ExpiryDate": ""
  },
  "ReqCredentials": {
    "scopes": [
      "Tasks.ReadWrite",
      "offline_access"
    ],
    "client_id": "",
    "redirect_URI": "",
    "url_auth": "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
    "url_token": "https://login.microsoftonline.com/common/oauth2/v2.0/token",
    "url_graph": "https://graph.microsoft.com/v1.0/me/",
    "ToDoListID": ""
  }
}
```

### courses.json
```
{
  "lk": {},
  "gknaturwissenschaften": {},
  "gksprache": {},
  "gkgesellschaft": {},
  "gkkünstlerisch": {},
  "gksport": {},
  "sf": {}
}
```
