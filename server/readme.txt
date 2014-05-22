Requêtes d'un client
--------------------
SELECT -> SelectRequest
 - tous les projets
 - tous les users
 - toutes les sessions
 - tous les documents

Requêtes relationnelles
----------------------- 

- toutes les sessions d'un projet (les sessions ont le même nom que le projet) => filtre
- tous les documents suivis (appartenant à) par un user (?documents followedBy user)
- tous les documents mobilisés dans une session (?documents mobilizedIn session)
- tous les users impliqués dans une session (?users involvedIn session)

Requêtes relationnelles filtrées (valid properties)
---------------------------------------------------

- tous les projets de "name" donné
                   de "description" donnée
                   de "session" donnée
                   
- tous les documents
             - de "name" donné
             - "usedDuring" ????????
             - "UsedIn" ??????????
             - "followedBy" ??????????    

- toutes les sessions
             - de "name" donné
             
- tous les users
             -              

Autres Requêtes
===============            
ADD -> AddRequest
On peut ajouter un document, un project, une session et un user   

DOWNLOAD -> DownloadRequest 
On peut downloader un document donné 

UPLOAD -> UploadRequest
On peut uploader un (document, bytecode) donné

LINK -> LinkRequest
On peut lier (un document, un user) avec un objet correspondant par le moyen d'une relation
 - un document avec un user (followedBy user)
 - un document avec une session (mobilizedIn session)
 - un user avec une session (involvedIn session)

Préparation d'une session
=========================

Choisir un projet 
Ajouter une session (date)
uploader les documents
associer les documents à la session


           