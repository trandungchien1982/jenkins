/* Requires the Docker Pipeline plugin */
pipeline {
    agent any
    stages {
        /* Excecute script in Alpine docker */
        stage('Executing script in Alpine docker for testing only ...') {
            agent { docker { image 'alpine:3.18.4' } }
            steps {
                sh 'ls -l'
                sh 'pwd'
                sh 'chmod +x ./jenkins-ci-java/simple-script.sh'
                sh './jenkins-ci-java/simple-script.sh'
                sh 'echo Finish Check code style Java using Linter [Java+Maven], [PENDING] ...'
            }
        }

        /* TODO: Check code style/Code Smell by sonarlint */
        stage('Check code style in Java, using SonarLint [PENDING] ... ') {
            agent { docker { image 'maven:3.9.5-eclipse-temurin-11-alpine' } }
            steps {
                sh 'mvn --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'echo Finish Check code style Java using Linter [Java+Maven], [PENDING] ... '
            }
        }

        /* Build Jar file using Gradle */
        stage('Build source code Java into JAR file') {
            agent { docker { image 'gradle:7-jdk11-alpine' } }
            steps {
                sh 'ls -l'
                sh 'pwd'
                sh 'echo Try to build the Java Project into JAR file ...'
                sh 'gradle --version'
                sh 'gradle clean build --build-file ./hello-world-app/build.gradle'
                sh 'echo Finish build Java project using Gradle, we will have the ./hello-world-app/build/libs/hello-world-0.0.1-SNAPSHOT.jar'
                sh 'ls -l ./hello-world-app/build/libs'
            }
        }

        /* Build Docker Image */
        stage('Build project and push to Docker Registry') {
            agent { docker { image 'docker:24.0.6-dind-alpine3.18' } }
            steps {
                sh 'docker --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./jenkins-ci-java/simple-script.sh'
                sh 'chmod +x ./jenkins-ci-java/simple-script.sh'
                sh './jenkins-ci-java/simple-script.sh'
                sh 'echo Try to list all artifacts in previous steps ...'
                sh 'ls -l ./hello-world-app/build/libs'
                sh 'chmod +x ./build-docker.sh'
                sh './build-docker.sh'
                sh 'echo Finish Push package project to Docker Registry in [Docker in Docker] env ...'
                sh 'echo Finish Jenkins CI Pipeline with multiple stages ...'
            }
        }
    }
}
