# Student Course Registration — DevOps Lab

## 1. Project Overview

This project demonstrates Git branching and integration, a Maven multi-module Java project, automated unit testing, a Jenkins Declarative Pipeline, and deployment of Nginx on Kubernetes using Minikube.

## 2. Project Structure

```text
Student-Starter/
├── pom.xml
├── Jenkinsfile
├── README.md
├── deployment.yaml
├── service.yaml
└── course-registration-app/
    ├── pom.xml
    └── src/
        ├── main/java/com/university/
        │   ├── Course.java
        │   ├── RegistrationService.java
        │   └── Student.java
        └── test/java/com/university/
            ├── CourseTest.java
            └── RegistrationServiceTest.java
```

## 3. Prerequisites

- Git
- Java Development Kit (JDK)
- Apache Maven
- Docker Desktop
- Minikube
- kubectl
- Jenkins, for executing the CI pipeline

The project is configured to compile for Java 17. A compatible JDK and Maven installation are required.

## 4. Build and Test

Run the following commands from the repository root:

```powershell
mvn clean test
mvn package
```

The root Maven POM defines the parent project and the `course-registration-app` module. JUnit tests are executed using Maven Surefire.

Test reports are generated in:

`course-registration-app/target/surefire-reports/`

The packaged JAR is generated under:

`course-registration-app/target/`

## 5. Registration Validation

The registration service validates course capacity and ensures that course credits are between 1 and 6 inclusive.

The unit tests cover:

- Rejection when a course is full.
- Acceptance when one seat remains.
- Acceptance of the minimum valid credit value (1).
- Acceptance of the maximum valid credit value (6).
- Rejection of zero, negative, and greater-than-six credit values.
- Preservation of student and course state when registration is rejected.

## 6. Git Workflow

Development was performed on the `feature/course-validation` branch, with separate commits for the capacity defect fix, registration tests, credit validation, Maven multi-module restructuring, Jenkins pipeline, and Kubernetes configuration.

The completed feature branch was merged into `main`.

To inspect the commit history and verify the working tree, run:

```powershell
git log --oneline --graph --all
git status
```

The working tree should be clean when all changes have been committed.

## 7. Jenkins Pipeline

The root-level `Jenkinsfile` defines stages for source checkout, Maven testing, and packaging.

The pipeline:

- Checks out the source code from the configured Git repository.
- Runs Maven tests.
- Packages the application after the test stage succeeds.
- Publishes JUnit test reports when available.
- Archives the generated JAR only after a successful pipeline.

The pipeline uses Windows `bat` steps and therefore requires a compatible Windows Jenkins agent.

The Jenkins pipeline was executed successfully, and the test cases passed. The Jenkins build page, test results, and archived artifact provide evidence of the execution.

## 8. Kubernetes Deployment

The application demonstrates deployment and management of Nginx using Kubernetes on Minikube.

### Start Minikube

```powershell
minikube start --driver=docker
kubectl get nodes
```

### Apply the Kubernetes resources

```powershell
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl rollout status deployment/course-registration-nginx
```

The Deployment uses the `nginx:1.27` image and initially defines two replicas. The NodePort Service exposes the Nginx application.

### Inspect the resources

```powershell
kubectl get deployments
kubectl get pods
kubectl get services
```

### Access the Nginx service

```powershell
minikube service course-registration-service --url
```

Open the resulting URL in a browser to verify that the Nginx welcome page is accessible.

### Scale to three replicas

```powershell
kubectl scale deployment course-registration-nginx --replicas=3
kubectl rollout status deployment/course-registration-nginx
kubectl get deployments
kubectl get pods
```

Verify that three replicas are running.

### Demonstrate self-healing

Delete one of the actual Pod names shown by `kubectl get pods`:

```powershell
kubectl delete pod <actual-pod-name>
kubectl get pods -w
```

Replace `<actual-pod-name>` with the real Pod name. The Deployment controller creates a replacement Pod to restore the desired replica count.

Press `Ctrl+C` to stop watching the Pods.

## 9. Verification and Evidence

The following commands and screenshots can be used to document the implementation and its results.

### Git

```powershell
git log --oneline --graph --all
git status
```

### Maven

```powershell
mvn clean test
mvn package
```

### Jenkins

Capture evidence of:

- The successful Jenkins build.
- The Test and Package stages.
- The recorded JUnit test results.
- The archived JAR artifact.

### Kubernetes

```powershell
kubectl get nodes
kubectl get deployments
kubectl get pods
kubectl get services
kubectl get pods -w
```

Also capture the Nginx service access result, the deployment scaled to three replicas, and the replacement Pod created after deleting an existing Pod.

Use actual command outputs and screenshots as evidence rather than documenting only the commands.

## 10. Limitations

- The Jenkins pipeline uses Windows-specific `bat` commands and requires a compatible Windows agent.
- The Kubernetes deployment demonstrates Nginx deployment, service exposure, scaling, and self-healing. It does not deploy the Java registration application as a container.
- The NodePort Service and Minikube environment are intended for lab demonstration rather than production deployment.
- Build and deployment results depend on the installed tools and the local environment.