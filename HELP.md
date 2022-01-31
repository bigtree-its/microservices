# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.bigtree.fapi-service' is invalid and this project uses 'com.bigtree.fapiservice' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.1/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.1/gradle-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#using-boot-devtools)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#boot-features-spring-hateoas)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#howto-execute-flyway-database-migrations-on-startup)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#production-ready)
* [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)
* [Resilience4J](https://cloud.spring.io/spring-cloud-static/spring-cloud-circuitbreaker/current/reference/html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Declarative REST calls with Spring Cloud OpenFeign sample](https://github.com/spring-cloud-samples/feign-eureka)

## DATABASE ADMINISTRATION

* How to RUN Shell into CockRoach Node
```
docker exec -it roach1 ./cockroach sql --insecure
```
* Crate Database
```
create database fapidb;
```
* Switch Database
```
use fapidb;
```


* Crate User
```
create user fapidbuser;
```
* Grant privileges to the user 
```
grant all on database fapidb to fapidbuser
```
* Grant privileges on databases

```
CREATE USER fapidbuser WITH PASSWORD roach;
```
```
GRANT ALL ON DATABASE fapidb TO fapidbuser;
```
```
SHOW GRANTS ON DATABASE fapidb;
```
* Grant privileges on specific tables in a database
```
GRANT SELECT ON TABLE fapidb.public.* TO fapidbuser;
GRANT ALL ON TABLE fapidb.public.* TO fapidbuser;
```
#### Admin UI:
You should be able to access the admin UI via port 9090. (Actually I have mapped the port 9090 with the container port 8080)
We can see 3 nodes in our cluster
You can connect your GUI DB client with the CockroachDB. Do note that the DB server port is 26757 (not 5432 as postgres DB)