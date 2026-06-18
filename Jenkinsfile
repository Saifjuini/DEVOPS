pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    stages {
        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Saifjuini/DEVOPS.git'
            }
        }
        stage('BUILD') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}