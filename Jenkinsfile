pipeline {
    agent any

    environment {
        DOCKER_REPO = "pocky2333/dineflex-backend"
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
                    // Get the commit hash and information
                    COMMIT_HASH = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    COMMIT_MSG = sh(script: "git log -1 --pretty=%B", returnStdout: true).trim()

                    echo "Commit Hash: ${COMMIT_HASH}"
                    echo "Commit Message: ${COMMIT_MSG}"

                    IMAGE_TAG = "${DOCKER_REPO}:${COMMIT_HASH}"
                    LATEST_TAG = "${DOCKER_REPO}:latest"

                    // Construct two tags: one with hash and another with latest
                    sh """
                        docker build --platform linux/amd64 -t ${IMAGE_TAG} -t ${LATEST_TAG} .
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        sh """
                            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                            docker push ${DOCKER_REPO}:${COMMIT_HASH}
                            docker push ${DOCKER_REPO}:latest
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Build and push complete!"
            echo "Image Tags: latest, ${COMMIT_HASH}"
            echo "Commit: ${COMMIT_MSG}"
        }
    }
}
