# Webflux Demo

Simple demo project for showing how to create REST API application based on Spring 
Boot 2 and Webflux.

### Requirements
- Apache Maven
- Java 1.8

### Installation
1. Checkout the repository
2. Run `mvn install` inside the root directory of the project

### Running the Application
1. Run `mvn exec:java` inside the root directory of the project

> Note: The application starts server which is listening on port 8080. Check if 
the port is already in use by other application.

> Note: The application also uses H2 SQL database which is dropped each time the
application is stopped.
