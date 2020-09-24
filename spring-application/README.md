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

## Library Inventory Service
Provides access to the registry of (available) books.

## Borrow Service
Keeps record of who borrowed which book.

## User Service
Provides access to a list of authorized users.
