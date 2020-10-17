# Spring Library Application
The idea of this application is to provide a user interface to a library
allowing maintaining and borrowing books depending on the user's role.

# Components
## Overview
    +------------------+      +----------+
    | InventoryService +------+          |
    +-------+----------+      |          |
            |                 |          |
    +-------+----------+      |          |      +---------------+
    | BorrowService    +------+ Library  +------+ LibraryClient |
    +-------+----------+      | Server   |      +---------------+
            |                 |          |
    +-------+----------+      |          |
    | UserService      +------+          |
    +------------------+      +----------+

## Library Client
Web interface

## Library Server
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