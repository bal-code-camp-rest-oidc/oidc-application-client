# Spring Library Application
The idea of this application is to provide a user interface to a library
allowing maintaining and borrowing books depending on the user's role.

# Components
s## Overview
                         resourceserver-configuration, 
                        / getting downstream delegated bearer from LibraryClient
    +------------------+      +----------+
    | InventoryService +------+          |    resourceserver-configuration,
    +------------------+      |          |   / getting bearer from LibraryClient       
                              |          |  /                     
    +------------------+      |          | /    +---------------+ 
    | BorrowService    +------+ Library  +------+ LibraryClient |  public, from browser,
    +------------------+      | Server   |      +---------------+- authorization_code pkce flow
                              |          |       \
    +------------------+      |          |        \
    | UserService      +------+          |          WebClient with
    +--+---------------+      +----------+          ServletOAuth2AuthorizedClientExchangeFilterFunction
       |                       \
       |                         WebClient with ServletBearerExchangeFilterFunction
       |
       |  client_credentials flow (confidential)
       |/  getting clientid & secret from UserService
    +--+-----------------+
    | Keycloak User-Repo |
    +--------------------+                                

## Library Client
Web interface

## Library Facade Server
REST interface providing endpoints for the library client and aggregating
services, i.e., keycloak / user service, book service and borrow service.

- [Swagger UI](http://localhost:9090/library-server/swagger-ui/index.html)

## Borrow Service
Keeps record of who borrowed which book.

- [Swagger UI](http://localhost:9091/library-borrow/swagger-ui/index.html)

## Library Inventory Service
Provides access to the registry of (available) books.

- [Swagger UI](http://localhost:9092/library-inventory/swagger-ui/index.html)

## User Service
Provides access to a list of authorized users.

- [Swagger UI](http://localhost:9093/library-users/swagger-ui/index.html)

To start in local mode, configure the environment-parameter `spring.config.name=application-dev` so that the local
keycloak-addresses will be used instead of the one instantiated on OpenShift.

Please paste the current **client-secret from the keycloak client** `Keycloak-admin` 
into the file `src/main/resources/application-dev.yml`

# Run the services via IDE/gradle

To start in local mode, keycloak-addresses should be used instead of the one 
instantiated on OpenShift.

Therefore its recommended, to start the apps in local mode with the dev-profile.
a) Use the vm-option `-Dspring.profiles.active=dev`.
b) Alternatively it's also possible to start with environment-parameter `spring.config.name=application-dev`