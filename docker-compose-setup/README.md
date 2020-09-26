# OAuth2/ OIDC Doccker Image

This sample application needs a local development environment. To ease up things we provided a docker compose
setting up the following backend:
- **Apache DS**
    - _Purpose_: used for testing federated user directories
    - _Access_: [ldap://localhost:10389](ldap://localhost:10389)
    - _Local Storage_: `<local workspace>/oidc-application-client/docker-compose-setup/apacheds/data`
- **MySQL DB**
    - _Purpose_: used for permanent storage of keycloak data
    - _Access_: port 
    - _Local Storage_: `<local workspace>/oidc-application-client/docker-compose-setup/mysql/data`
- **Keycloak**
    - _Purpose_: OAuth2/ OIDC Server
    - _Access_: http://localhost:8080, https://localhost:8443 (self-signed)
    - _Local Storage_: (using MySQL) 