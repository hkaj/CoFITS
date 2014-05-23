## Installation de la base de donnees

Les instructions suivantes supposent que postgresql est installe sur le serveur et qu'elles sont lancees par l'utilisateur **postgres**.

Configuration pour accès extérieur :

http://www.cyberciti.biz/tips/postgres-allow-remote-access-tcp-connection.html

Configuration pour utilisateur de modification :

http://www.postgresql.org/docs/9.0/static/sql-grant.html

Commencer par lancer psql depuis le dossier database, tout simplement :
```bash
$ cd database
$ psql
```

**Creation de l'agent du serveur**
```sql
CREATE User documentAgent;
ALTER USER documentagent WITH PASSWORD '1234' ;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO documentAgent;
GRANT INSERT ON ALL TABLES IN SCHEMA public TO documentAgent;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO documentagent;
```

**Creation et remplissage des tables (avec des donnees d'exemple)**
```sql
\i create_db.sql
\i populate_db.sql
```

**Suppression des tables**

```sql
\i purge_db.sql
```

Un diagramme des tables est disponible dans le dossier **about**.
