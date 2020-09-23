# oidc-application-client
Project that contains API and samples that helps to work with OIDC, keycloak, JWT from an client application perspective.

## Requirements
- installed docker
- installed docker-compose

## Sub-Projects
The following sub projects are part of this repo:

0. ```docker-compose-setup```:
   docker compose providing mysql as a permanent DB store and keycloak
   
### docker-compose-setup
The following images are part of this project 
- mysql 8.0.21
- keycloak 11.0.2

You can *start* the project the following way:
```
cd <ws-root>/oidc-application-client/docker-compose-setup
docker-compose up
```

The data will be *initialized* based on two files:
0. DB-Setup, providing schemas and users: <ws-root>>/oidc-application-client/docker-compose-setup/mysql/initdb.d/2-init.sql
0. Keycloak sample realm from OIDC tutorial: <ws-root>>/oidc-application-client/docker-compose-setup/keycloak/data/keycloak_realm_workshop.json

To *stop* the images, use:
```
cd <ws-root>/oidc-application-client/docker-compose-setup
docker-compose down
```

Whenever you want to delete the persistent data, simply execute:
```
<ws-root>//oidc-application-client/docker-compose-setup/mysql/clean-instance-data.sh
```
After this you will be able to restart the docker image as described above and
the data will be created again.