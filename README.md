# Data Series Tracker
A simple RESTful web service providing API for measure (datapoint) management and basic calculations. 

## Requirements
* Java JDK 11

## Building the service
Run `./gradlew clean build integrationTests`. An executable file `application/build/libs/application.jar` will be created.

## Documentation
Public API documentation can be found in file [./doc/openapi/openapi-v1.yaml](./doc/openapi/openapi-v1.yaml) (OpenAPI 3.0.0).
The application is using [Layered Architecture](https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html)
pattern with following structure.

| Package                     |  Layer                              |
|-----------------------------|-------------------------------------|
| org.factory.dst.api         | Controllers and code tied with HTTP |
| org.factory.dst.process     | Business processes                  |
| org.factory.dst.persistence | Persistence                         |
