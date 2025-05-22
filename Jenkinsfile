pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "pocky2333/dineflex-backend"
        DOCKER_CREDENTIALS_ID = "dockerhub"
    }

    stages {
        stage('Clone Repo') {
            steps {
                echo 'Cloning GitHub repository...'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build --platform linux/amd64 -t $DOCKER_IMAGE .'
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKER_IMAGE
                    """
                }
            }
        }
    }
}