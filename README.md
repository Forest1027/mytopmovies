# Introduction
MyTopMovie project(MTM) provides REST API for managing your own movie lists. It's using [TMDB](https://developers.themoviedb.org/3/getting-started/introduction) as data source for movie information.

This documentation is meant to provide guidance for running the project and tests in your local. I strongly recommend you to visit https://forestruan.notion.site/MTM-Design-Documentation-85a997e6f47b415a95c5d14d8515bfbc for the detailed design documentation including architecture and tech specs of this project.

~~To access the live version of this project, please visit http://mtm-3a4d1d4b1bb5e20a.elb.eu-west-1.amazonaws.com.~~ (Currently stopped hosting live project)

# Run the project in your local
The H2 database is used for a quick start in local environment. To run the project, please follow the steps below.

1. If you are using Mac or Linux, in your root directory, run `./mvnw clean package` to build package. If you are running it on Windows, please use `mvnw.cmd` to replace `mvnw`.
2. Get an API key from TMDB website, following this link: https://developers.themoviedb.org/3/getting-started/introduction.
3. Execute below in your root directory. We are setting `spring.profiles.active` to local so `application-local.yml` is used. Use the api key you get from TMDB to replace "${tmdb_key}".

```
java -jar -Dspring.profiles.active=local -Djwt_key=test -Dconstants.tmdb.tmdbKey=${tmdb_key} target/mytopmovies-1.0.0.jar
```

# Access API

After running the project, visit http://localhost:8080/swagger-ui.html to see the Rest APIs.

You can visit http://localhost:8080/h2-console/ to access the database.

# Run test
run unit tests: `./mvnw clean test`

run integration tests: `./mvnw clean integration-test`