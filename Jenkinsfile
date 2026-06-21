pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    environment {
        DOCKER_IMAGE = 'saifjuini/student-management'
        DOCKER_TAG = "v${BUILD_NUMBER}"
    }
    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Saifjuini/DEVOPS.git'
            }
        }
        stage('BUILD') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
       
        stage('TEST') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('MVN SONARQUBE') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh 'eval $(minikube docker-env) && docker pull ${DOCKER_IMAGE}:${DOCKER_TAG} && eval $(minikube docker-env --unset) || true'
                sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                sh 'kubectl apply -f k8s/sonarqube-deployment.yaml'
                sh "sed -i 's|image: saifjuini/student-management:.*|image: ${DOCKER_IMAGE}:${DOCKER_TAG}|g' k8s/spring-deployment.yaml"
                sh 'kubectl apply -f k8s/spring-deployment.yaml'
                sh 'kubectl rollout status deployment/mysql -n devops --timeout=300s'
                sh 'kubectl rollout status deployment/student-management -n devops --timeout=600s'
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}