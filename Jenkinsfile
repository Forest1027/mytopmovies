pipeline {
    agent any
    environment {
        PROFILE = "${BRANCH_NAME==main?prod:dev}"
    }
    tools {
        maven 'apache-maven-3.8.3'
    }
    stages {
        /* stage("unit-test") {
            steps {
                sh "mvn clean test"
            }
        }
        stage("integration-test") {
            steps {
                sh "mvn clean verify"
            }
        }
        stage("build") {
            steps {
                sh "mvn clean package"
            }
        } */
        stage("dockerize") {
            environment {
                DB_USERNAME = credentials('db_username')
                DB_PASSWORD = credentials('db_password')
                TMDB_APIKEY = credentials('tmdb_key')
                JWT_KEY = credentials('jwt_key')
           }
            steps {
                echo "dockerize the application"
                sh '''
                    docker build  \
                    --build-arg profile=${PROFILE}  \
                    --build-arg db_username=${DB_USERNAME}  \
                    --build-arg db_password=${DB_PASSWORD}  \
                    --build-arg jwt_key=${JWT_KEY}  \
                    --build-arg tmdb_key=${TMDB_APIKEY}  \
                    -t mtm:latest .
                '''
            }
        }
        stage("deploy") {
            steps {
                echo "deploying the application"
            }
        }
    }
}