# OAuth2/ OIDC Docker Image

This sample application needs a local development environment. To ease up things we provided a docker compose
setting up the following backend:
- **Apache DS**
    - _Purpose_: used for testing federated user directories
    - _Version_: 0.8.0 (openmicroscopy)
    - _Access_: [ldap://localhost:10389](ldap://localhost:10389)
    - _Local Storage_: `<local workspace>/oidc-application-client/docker-compose-setup/apacheds/data`
- **MySQL DB**
    - _Purpose_: used for permanent storage of keycloak data
    - _Version_: 8.0.21
    - _Access_: port 3310
    - _Local Storage_: `<local workspace>/oidc-application-client/docker-compose-setup/mysql/data`
- **Keycloak**
    - _Purpose_: OAuth2/ OIDC Server
    - _Version_: 11.0.2
    - _Access_: http://localhost:8080, https://localhost:8443 (self-signed)
    - _Local Storage_: (using MySQL) 
    
## Docker Compose usage

Start the Keycloak-Application described as above, using docker-compose syntax:

*(starting from project-root)*
```bash
cd docker-compose-setup
docker-compose up -d
```    