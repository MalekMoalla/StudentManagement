pipeline {

    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        DOCKER_IMAGE = 'malekmoalla/student-management'
        DOCKER_TAG = "v${BUILD_NUMBER}"
        SONAR_HOST_URL = 'http://localhost:9000'
        KUBECONFIG = '/var/lib/jenkins/.kube/config'
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/MalekMoalla/StudentManagement.git'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                    mvn sonar:sonar \
                      -Dsonar.projectKey=student-management \
                      -Dsonar.projectName=student-management \
                      -Dsonar.host.url=${SONAR_HOST_URL} \
                      -Dsonar.token=$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -t ${DOCKER_IMAGE}:latest ."
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
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                kubectl apply -f k8s/mysql-deployment.yaml -n devops
                kubectl apply -f k8s/spring-deployment.yaml -n devops

                kubectl set image deployment/spring-app spring-app=${DOCKER_IMAGE}:${DOCKER_TAG} -n devops

                kubectl rollout status deployment/mysql -n devops
                kubectl rollout status deployment/spring-app -n devops

                kubectl get pods -n devops
                kubectl get svc -n devops
                '''
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Build, Docker push, and Kubernetes deployment completed successfully.'
        }
        failure {
            echo 'Build failed.'
        }
    }
}