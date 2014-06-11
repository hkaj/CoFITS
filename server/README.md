## Installation de la base de donnees

Les instructions suivantes supposent que postgresql est installe sur le serveur et qu'elles sont lancees par l'utilisateur **postgres**.

Commencer par lancer psql, tout simplement :
```bash
$ psql
```

**Creation de l'agent du serveur**
```sql
CREATE USER documentagent superuser;
ALTER USER documentagent WITH PASSWORD 'cofits';
ALTER USER postgres WITH PASSWORD 'cofits';
```

**Configuration du serveur postgresql**

Configurer les modalites de connexion :

```bash
nano /etc/postgres/9.3/main/pg_hba.conf
```

Remplacer :
```
local	all   all			peer
```
par :
```
local	all   all			md5
```

Et ajouter a la fin du fichier :
```
host	all		all		0.0.0.0/0 	md5
```

Puis configurer le serveur pour ecouter sur toutes ses interfaces :

```
nano /etc/postgresql/9.3/main/postgresql.conf
```

Remplacer :
```
#listen_addresses='localhost'
```
Par :
```
listen_addresses='*'
```

Et enfin redemarrer le serveur :
```
/etc/init.d/postgresql restart
```

Une fois que le serveur a redemarre, essayons de se connecter avec l'utilisateur postgres :

```
cd server/database
psql -U postgres
```

Profitons-en pour creer la DB :

**Creation et remplissage des tables (avec des donnees d'exemple)**
```sql
\i create_db.sql
\i populate_db.sql
\q
```

Verifions ensuite que documentagent fonctionne egalement :

```
psql -U documentagent -d postgres
```

Enfin on peut tenter une connexion distante, pour cela executer depuis la machine cliente :

```
psql -h <ip_du_server> -U documentagent -d postgres
```

Pour obtenir l'IP du serveur, executer __ifconfig__ depuis le serveur et copier l'adresse de la bonne interface.

**Suppression des tables (just in case)**

```sql
\i purge_db.sql
```

Un diagramme des tables est disponible dans le dossier **about**.

## TODO:

- Retourner des messages d'erreurs au lieu de rien en cas d'erreur serveur.
- Ameliorer le stockage des documents dans le file system du serveur.
- Dans la DownloadBehaviour, verifier la legitimite de l'utilitsateur qui demande le fichier.
- Fragmenter les fichiers a envoyer en plusieurs messages si necessaire.
- ~~Mettre l'extension des documents dans name et une meta donnée dans type.~~
- Implementer un UNSUBSCRIBE pour que les agents puissent cesser de suivre un projet.
