/* Requires the Docker Pipeline plugin */
pipeline {
    agent any
    /* Thiết lập biến môi trường dùng chung cho các Stages khác nhau */
    environment {
        LOGIN_DOCKER_USR = credentials('login-docker-usr')
        LOGIN_DOCKER_PWD = credentials('login-docker-pwd')

        PUBLIC_VAR01 = 'Public Variable 01, tdc'
        PUBLIC_VAR02 = 'Public Variable 02, mnk'
    }
    stages {
        /* Excecute script in Alpine docker */
        stage('Executing script in Alpine docker for testing only ...') {
            agent { docker { image 'alpine:3.18.4' } }
            steps {
                sh 'ls -l'
                sh 'pwd'
                sh 'chmod +x ./jenkins-ci-django/simple-script.sh'
                sh './jenkins-ci-django/simple-script.sh'

                sh 'echo ------------------------------------------------- '
                sh 'echo "Print out the env var: PUBLIC_VAR01 = $PUBLIC_VAR01" '
                sh 'echo "Print out the env var: PUBLIC_VAR02 = $PUBLIC_VAR02" '
                sh 'echo ------------------------------------------------- '

                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '
                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '

                sh 'export TEST_LDU_USR=$LOGIN_DOCKER_USR'
                sh 'echo $TEST_LDU_USR'

                sh 'echo Finish run Testing Script for Django ...'
            }
        }

        /* TODO: Check code style/Code Smell pylint */
        stage('Check Python Lint + Syntax + Code Smell [PENDING] ... ') {
            agent { docker { image 'python:3.9.18-alpine3.18' } }
            steps {
                sh 'echo Check Python Lint + Syntax + Code Smell ...'
                sh 'python --version'
                sh 'ls -l'
                sh 'pwd'

                sh 'echo ------------------------------------------------- '
                sh 'echo "Print out the env var: PUBLIC_VAR01 = $PUBLIC_VAR01" '
                sh 'echo "Print out the env var: PUBLIC_VAR02 = $PUBLIC_VAR02" '
                sh 'echo ------------------------------------------------- '

                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '
                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '

                sh 'echo Finish Check code style Django using plint [PENDING] ... '
            }
        }

        /* TODO: Build Django project */
        stage('Build Django project + Execute test cases to make sure there is no error') {
            agent { docker { image 'python:3.9.18-alpine3.18' } }
            steps {
                sh 'echo "Build codes + Execute Unit Tests/Integration Tests" '
                sh 'ls -l'
                sh 'pwd'
                sh 'echo Try to build the Python/Django project ...'
                sh 'echo Finish build Python/Django project ...'
            }
        }

        /* TODO: Push the project Django to Docker Registry */
        stage('Build project and push to Docker Registry') {
            agent { docker { image 'docker:24.0.6-dind-alpine3.18' } }
            steps {
                sh 'echo "Build Docker Images and push it to Docker Registry" '
                sh 'docker --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'echo ------------------------------------------------- '
                sh 'echo "Print out the env var: PUBLIC_VAR01 = $PUBLIC_VAR01" '
                sh 'echo "Print out the env var: PUBLIC_VAR02 = $PUBLIC_VAR02" '
                sh 'echo ------------------------------------------------- '

                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '
                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '

                sh 'cat ./jenkins-ci-django/simple-script.sh'
                sh 'chmod +x ./jenkins-ci-django/simple-script.sh'
                sh './jenkins-ci-django/simple-script.sh'
                sh 'echo Try to build Dockerfile ...'
                sh 'chmod +x ./build-docker.sh'
                sh './build-docker.sh'
                sh 'echo Finish Push package project to Docker Registry in [Docker in Docker] env ...'
                sh 'echo Finish Jenkins CI Pipeline with multiple stages ...'
            }
        }
    }
}
