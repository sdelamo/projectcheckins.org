# Projectcheckins

## Development

To build the project you need Java 21. 

If you use SDKMan! you can do: 

```
sdk use java 21.0.2-amzn
```

To build the project run: 

```
./gradlew build
```

To open a Test Coverage report run: 

```
open code-coverage-report/build/reports/jacoco/testCodeCoverageReport/html/index.html 
```

### Run the app

To run the app locally create a file called `netty/src/main/resources/application-dev.properties` with the following content: 

```properties
eclipsestore.storage.main.root-class=org.projectcheckins.repository.eclipsestore.Data
eclipsestore.storage.main.storage-directory=build/eclipestore
micronaut.security.authentication=cookie
micronaut.security.token.jwt.signatures.secret.generator.secret=pleaseChangeThisSecretForANewOne
```

then run `./gradlew :netty:run`

## Technology Stack

- [Thymeleaf](https://www.thymeleaf.org)
- [Micronaut Framework](https://micronaut.io/)
- [Bootstrap](https://getbootstrap.com)

### Libraries 

- [KSUID](https://github.com/ksuid/ksuid)
- [Awaitility](https://github.com/awaitility/awaitility)
### Gradle Plugins
[Jacoco Gradle Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)

## Spec

- [Jakarta Bean Validation 2.0](https://beanvalidation.org/2.0/spec/)

## Deployment

### ElasticBeanstalk with EclipseStore S3 and Gmail

Configure the following environment variables: 

| Environment Variable                                                        | Value   | Description                                                                  | 
|:----------------------------------------------------------------------------|:--------|:-----------------------------------------------------------------------------|
| `MICRONAUT_ENVIRONMENTS`                                                    | `ec2`   | [Micronaut environment](https://docs.micronaut.io/4.4.6/guide/#environments) |
| `MICRONAUT_SERVER_PORT`                                                     | `5000`  | Elastic Beanstalk assumes that the application listens on port 5000.         |
| `ECLIPSESTORE_S3_STORAGE_DEFAULT_BUCKET_NAME`                               |         | S3 bucket name                                                               |
| `EMAIL_SENDER`                                                              |         | Gmail address                                                                |
| `GMAIL_APP_SPECIFIC_PASSWORD`                                               |         | [Gmail App specific password](https://myaccount.google.com/apppasswords)     |
| `MICRONAUT_SECURITY_TOKEN_JWT_SIGNATURES_SECRET_GENERATOR_SECRET`           |         | Secret                                                                       |
		