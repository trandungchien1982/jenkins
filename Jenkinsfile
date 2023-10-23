/* Requires the Docker Pipeline plugin */
pipeline {
    agent none
    stages {
        /* Java + Maven env */
        stage('Build project in Java+Maven') {
            agent { docker { image 'maven:3.9.5-eclipse-temurin-11-alpine' } }
            steps {
                sh 'mvn --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Build [Java+Maven] ...'
            }
        }

        /* Python env */
        stage('Check ES Lint in Python') {
            agent { docker { image 'python:3.12.0-alpine3.18' } }
            steps {
                sh 'python --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Check ES Lint in [Python] env ...'
            }
        }

        /* NodeJS env */
        stage('Check Syntax in NodeJS env') {
            agent { docker { image 'node:18.18.2-alpine3.18' } }
            steps {
                sh 'node --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Check ES Lint in [NodeJS] env ...'
            }
        }

        /* Golang env */
        stage('Run Unit Test in Golang env') {
            agent { docker { image 'golang:1.20.10-alpine3.17' } }
            steps {
                sh 'go version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Run Unit Test in [Golang] env ...'
            }
        }

        /* Ruby env */
        stage('Package project to Jar/War in Ruby env') {
            agent { docker { image 'ruby:3.2.2-alpine3.18' } }
            steps {
                sh 'ruby --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Package project to Jar/War in [Ruby] env ...'
            }
        }

        /* Ruby env */
        stage('Build project and push to Docker Registry') {
            agent { docker { image 'docker:24.0.6-dind-alpine3.18' } }
            steps {
                sh 'docker --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Push package project to Docker Registry in [Docker in Docker] env ...'
                sh 'echo Finish Jenkins CI Pipeline with multiple stages ...'
            }
        }
    }
}
