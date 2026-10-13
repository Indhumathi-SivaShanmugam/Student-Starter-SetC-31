
pipeline {
    agent any

    environment {
        JAVA_HOME = 'C:/Program Files/Java/jdk-25.0.4.1'
        MAVEN_CMD = 'C:/Program Files/apache-maven-3.9.16/bin/mvn.cmd'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                bat '"%MAVEN_CMD%" clean test'
            }
        }

        stage('Package') {
            steps {
                bat '"%MAVEN_CMD%" package -DskipTests'
            }
        }
    }

    post {
        always {
            junit(
                testResults: 'course-registration-app/target/surefire-reports/TEST-*.xml',
                allowEmptyResults: true
            )
        }

        success {
            archiveArtifacts(
                artifacts: 'course-registration-app/target/*.jar',
                fingerprint: true
            )
        }
    }
}
