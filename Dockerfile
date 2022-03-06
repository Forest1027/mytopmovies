FROM openjdk:17
ARG profile
ARG db_username
ARG db_password
ARG jwt_key
ARG tmdb_key
ARG JAR_FILE=target/*.jar
ENV profile=${profile}
ENV db_username=${db_username}
ENV db_password=${db_password}
ENV jwt_key=${jwt_key}
ENV tmdb_key=${tmdb_key}
EXPOSE 8080/tcp
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${profile}", "-Dspring.datasource.username=${db_username}", "-Dspring.datasource.password=${db_password}", "-Dconstants.jwt.secretKey=${jwt_key}", "-Dconstants.tmdb.tmdbKey=${tmdb_key}", "/app.jar"]
