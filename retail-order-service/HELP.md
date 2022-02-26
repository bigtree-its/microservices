# Getting Started

A RESTful service exposing endpoints for Orders, Baskets

Stores the orders, baskets in a SQL database hosted in Google Cloud.

## Step1: Clone the project
`git clone https://github.com/project-openbasket/order-service.git` to your project directory
`git clone https://github.com/project-openbasket/utilities.git` to your project directory

## Step2: Configure Cloud SQL access 
Cloud SQL is accessed by order microservice to store orders and related records
To access Cloud SQL we need Cloud SQL Proxy. Read more about this here: https://cloud.google.com/sql/docs/mysql/sql-proxy

### Installing the Cloud SQL Proxy
Download the proxy for Mac to your favourite directory on your computer

`curl -o cloud_sql_proxy https://dl.google.com/cloudsql/cloud_sql_proxy.darwin.amd64`

For Windows
`Right-click https://dl.google.com/cloudsql/cloud_sql_proxy_x64.exe and select Save Link As to download the proxy. Rename the file to cloud_sql_proxy.exe.`

### Using a service account for authentication
1. Download key file for the service account which has roles to access the CloudSQL
2. Export environment variable as below
        `export GOOGLE_APPLICATION_CREDENTIALS=/path/to/key/file.json`
3. Also copy the json key file to ~/.config/gcloud. If this directory not exist then create one
4. Raname the ~/.config/gcloud/key.json to ~/.config/gcloud/application_default_credentials.json
5. Make sure it all setup properly by running below command
   `gcloud auth application-default print-access-token`
   It should print the access token

## Step3: Test Connection to Cloud SQL via Proxy
Now we have downloaded Cloud SQL proxy and installed also setup the service account access. Lets run the connection test     
### Run the Cloud SQL Proxy
1.  Make the cloud_sql_proxy executable:
    `chmod +x cloud_sql_proxy`
2.  Run the Cloud SQL Proxy for database connections
    `./cloud_sql_proxy -instances=nodal-formula-295821:europe-west2:sql-instance=tcp:3306`

### Install MYSQL 
1. Install MySQL Client on Mac
        `brew install mysql`
   
1.  Connect to your SQL proxy database using the mysql client
    `mysql -u <USERNAME> -p --host 127.0.0.1 --port 3306`
    `mysql -u root -p --host 127.0.0.1 --port 3306`

### Run the micro service and Connect MYSQL 
1. Build the project
        `gradle clean build`
2. Run the spring boot service
        `SPRING_PROFILES_ACTIVE=local gradle bootRun`

# How to connect to Cloud SQL with cloud sql proxy as a docker image

1. Run the following docker command
        docker run  \
        -v ${GOOGLE_APPLICATION_CREDENTIALS}:/config \
        -p 127.0.0.1:3306:3306 \
        gcr.io/cloudsql-docker/gce-proxy:1.16 /cloud_sql_proxy \
        -instances=nodal-formula-295821:europe-west2:sql-instance=tcp:127.0.0.1:3306 -credential_file=/config
2. To Connect to your database using the mysql client
        mysql -u root -p --host 127.0.0.1 --port 3306
3. To connect from the order microservice
        SPRING_PROFILES_ACTIVE=proxy gradle bootRun

# Run the orderservice and Cloud SQL Proxy from docker-compose

1. Stop previously running microservice instance and docker containers
        `docker ps` -- to list running docker containers
        `docker stop {container-id}` -- Stop the container
        `docker rm {container-id}` -- Remove the container
2. Build the project artifact
        `gradle clean build`
3. Build docker image. If the source code changed
        `./docker-build.sh`  -- It produces new docker image for the service
3. Run the containers(order-service, cloud-sql-proxy) from docker-compose file
       `${project-dir}/utilities/docker-compose up`

# Call Order Service Endpoints
1. Order service listen on port 8082 for connections
2. Import Postman collections from ${project-dir}/utilities into your postman

### Reference Documentation

For further reference, please consider the following sections:

- [Official Gradle documentation](https://docs.gradle.org)
- [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.0/gradle-plugin/reference/html/)
- [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.0/gradle-plugin/reference/html/#build-image)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#using-boot-devtools)
- [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-developing-web-applications)
- [Spring Security](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-security)
- [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-jpa-and-spring-data)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#production-ready)

### Guides

The following guides illustrate how to use some features concretely:

- [Building the microservice Locally]()

- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
- [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
- [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links

These additional references should also help you:

- [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)


### Additional Notes

## How OrderService sends Confirmation Emails
Order service uses an SMTP server to send emails to customer's email address when order successfully created.
application.yml contains SMTP configuration. The source email id must be configured to allow the emails to be sent

If you encounter any issues when sending emails such as below
org.springframework.mail.MailAuthenticationException: Authentication failed; nested exception is javax.mail.AuthenticationFailedException

### Please use one of troubleshooting mechanisms
1. Make sure email id and passwords are correct
2. Login to Gmail.
3. Access the URL as https://www.google.com/settings/security/lesssecureapps
4. Select "Turn on"

## How to Encrypt sensitive information using Jasypt
1. Download the CLI tool from http://www.jasypt.org/download.html
2. Navigate to the folder where jasypt-1.9.3/bin

### Encryption:
To encrypt a sensitive information with a secret use below
```
./encrypt.sh input=<sensitive-data> password=<secret>
```

### Decryption:
To decrypt with a secret use below
```
./decrypt.sh input=<encrypted-data> password=<secret>
```

### How to use Jasypt in Springboot 
in your application.yml use the settings below for local
jasypt:
 encryptor:
  password: <secret>

# DATABASE ADMINISTRATION

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
create customer fapidbuser;
```
* Grant privileges to the customer
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

 