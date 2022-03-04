# Introduction
This project provides REST APIs for managing your own movie lists and reviews. It's using TMDB as data source. 
You can download the project and run in your local.

# Run the project
For local environment, the H2 database is used for simplicity. To run the project, please follow the steps below.

1. In your root directory, run `./mvnw clean package` to build package. If it's in windows, please use mvnw.cmd.

2. Execute below in your root directory. We are setting `spring.profiles.active` to local so `application-local.yml` is used.

```
java -jar -Dspring.profiles.active=local -Djwt_key=test target/mytopmovies-0.0.1-SNAPSHOT.jar
```

# Access API

After running the project, visit http://localhost:8080/swagger-ui.html for the Rest APIs.

You can visit http://localhost:8080/h2-console/ to access the database.

# Run test
run unit tests: `./mvnw clean test`

runt integration tests: `./mvnw clean integration-test`

If it's in windows, please use mvnw.cmd.

This project is integrated with SonarQube. To get the information on test report, install docker engine and docker compose, then run the command below to start SonarQube server:
`docker compose up`. If you are not a M1 user, please remove `platform: linux/arm64/v8` from `docker-compose.yml` before execute the command.

After the SonarQube server has successfully started, run the command to execute test and generate report: `./mvnw clean verify sonar:sonar`.