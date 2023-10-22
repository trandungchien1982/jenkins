/* Requires the Docker Pipeline plugin */
pipeline {
    agent { docker { image 'maven:3.9.5-eclipse-temurin-11-alpine' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./simple-pipeline/simple-script.sh'
                sh 'chmod +x ./simple-pipeline/simple-script.sh'
                sh './simple-pipeline/simple-script.sh'
                sh 'echo Finish CI ...'
            }
        }
    }
}