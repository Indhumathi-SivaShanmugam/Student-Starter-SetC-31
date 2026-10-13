
pipeline {
    agent any

    environment {
        JAVA_HOME = 'C:/Program Files/Java/jdk-25.0.4.1'
        PATH+JAVA = 'C:/Program Files/Java/jdk-25.0.4.1/bin'
        PATH+MAVEN = 'C:/Program Files/apache-maven-3.9.16/bin'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                bat 'mvn.cmd clean test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn.cmd package -DskipTests'
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
