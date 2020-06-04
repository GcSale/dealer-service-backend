# DealerServiceBackend
Dealer service backend

## Table of Contents
1. [Architecture](#architecture)
    1. [Infrastructure layer](#infrastructure-layer)
    2. [Applicatino layer](#application-layer)
    3. [Domain layer](#domain-layer)
2. [Testing](#testing)
    1. [Unit tests](#unit-tests)
    2. [Integration tests](#integration-tests)
3. [Local development](#local-development)

## Architecture
[Diagram](https://drive.google.com/file/d/1D48w0NYdzoToRwC31-_gYYlX-4iw-PeL/view?usp=sharing)

The application splits into 3 layers:
1. Infrastructure layer 
2. Application layer
3. Domain layer

Architecture is inspired by Hexagonal architecture (ports and adapters). 
So the application layer declares ports for commands and quires and contain theirs implementations. 
On the other hand the application layer declares only interfaces (ports) for interacting with infrastructure layer.
Similarly, The domain layer declares service's interfaces and their implementations, so the application layer is able to invoke them.  
 
### Infrastructure layer
Code that responsible for interactions with user (web, cli) and for external communication.
Repository interface placed in the application layer because it's a port, but Spring Data Repositories don't require implementations.

### Application layer
Code that responsible to execute commands and queries from infrastructure layer.
Commands invoke services from domain layer and external services from infrastructure layer.
Quires prepare responses for infrastructure layer.
Main validation performs in this layer, so all commands and quires are validated in the same way for every client type.  

### Domain layer
Layer contains models and services to perform business rules.

## Testing
There are 2 types of tests:
1. Unit tests
2. Integration tests

### Unit tests
Most parts of the code from application and domain layer should be covered with unit tests for every invariant.
Converters from infrastructure layer should be covered either.

### Integration tests
Controllers should be covered with integration tests. 
Integration test for rest controller should perform an API request with using real database. 

## Local development
In `dev/docker-compose.yaml` there are 2 databases: 
1. `db-test` for using from functional tests. This database will be cleaned up after every test run.
2. `db-app` for using in local development if you want to run and check something by hand

Swagger link: [http://localhost:8080/swagger.html](http://localhost:8080/swagger.html)

Run full tests in isolation: `docker-compose -f ci/docker-compose.yml run tests`
