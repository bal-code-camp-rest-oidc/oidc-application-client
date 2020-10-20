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
export KEYCLOAK_HOST=localhost
docker-compose -f docker-compose.yml up -d
```

Start the library-scenario spring-application together with 
the Keycloak-Application described as above, using docker-compose syntax:

*(starting from project-root)*
```bash
cd docker-compose-setup
export KEYCLOAK_HOST=$(hostname) // or determine/set the ipv4-address
docker-compose -f docker-compose.yml -f library-scenario-apps.yml up -d
```

## Additional Setup

### Debugging via Keycloak Events logging

Its pretty useful to see detailed log-statements, recorded during
the login-flows. Just go to `Manage -> Events` and activate the Option `Save Events`

### Add Github Identity Provider

To be able to log in using Gihub-Accounts, go to `Configure -> Identity Providers` and add
the `GitHub` Identity-Provider.
Just fill in the `Client ID` and `Client Secret` properties and click `Save`.
We also defined Default Scopes with `library_user`, so that every new registration
automatically has the default-permissions to access the library-client as a `library_user`.

We used our Client-Id and -Secret of the OAuth App `Notification-Client` 
of our GitHub Organization, that is created for local development purposes, so that
Hompage URL and Authorization callback URL points to the localhost-URLs.


### Connect ldap-server from the keycloak-instance

We are just using defaults, without changing ports, usernames or secrets.
**This setup is for study-purposes and not production-ready at all!**

1. Configure-> User Federation -> Add `ldap`
2. Change the following properties in the `Add user storage provider`-Form:
    1. Vendor = **Other**
    2. Connection URL = **ldap://apacheds:10389**
    3. User DN = **uid=admin, ou=system**
    4. Bind DN = **uid=admin, ou=system**
    5. Bind Credential = **secret**

[see also chapter keycloak-administration in th documentation repo](https://github.com/bal-code-camp-rest-oidc/documentation/edit/master/README.md#keycloak-administration)    
