# Run the project
## Use H2 database
If you don't have postgreSQL installed in your local, in pom.xml comment out dependency as below:
```xml
<!--<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
-->

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <!--<scope>test</scope>-->
</dependency>
```
Then in your root directory, run `./mvnw clean package` to build package. If it's in windows, please use mvnw.cmd

To run the package, execute below in your root directory.

```
java -jar -Dspring.profiles.active=demo -Djwt_key=test target/mytopmovies-0.0.1-SNAPSHOT.jar
```

## Use PostgreSQL

If you have PostgreSQL installed, not need to change pom.xml. Use the same command to build package.

When running the package, execute 
```
java -jar -Dspring.profiles.active=local -Djwt_key=test=test -Ddb_username={your_db_username} -Ddb_password={your_db_password} target/mytopmovies-0.0.1-SNAPSHOT.jar
```

# Access API

 After running the project, visit http://localhost:8080/swagger-ui.html

# Run test
run unit tests: `./mvnw clean test`

runt integration tests: `./mvnw clean integration-test`

If it's in windows, please use mvnw.cmd