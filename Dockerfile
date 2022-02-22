FROM openjdk:17
ARG profile
ARG db_username
ARG db_password
ARG jwt_key
ARG tmdb_key
ARG JAR_FILE=target/*.jar
ENV profile=${profile}
ENV username=${db_username}
ENV password=${db_password}
ENV jwt_key=${jwt_key}
ENV tmdb_key=${tmdb_key}
EXPOSE 8080/tcp
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${profile}", "-Ddb_username=${username}", "-Ddb_password=${password}", "-Dconstants.jwt.secretKey=${jwt_key}", "-Dconstants.tmdb.tmdbKey=${tmdb_key}", "/app.jar"]
