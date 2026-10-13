\# Student Course Registration — DevOps Lab



\## 1. Project Overview



This project demonstrates Git branching and integration, a Maven multi-module Java project, automated unit testing, a Jenkins Declarative Pipeline, and deployment of Nginx on Kubernetes using Minikube.



\## 2. Project Structure



\- `pom.xml` — Maven parent POM and module configuration.

\- `course-registration-app/pom.xml` — Child module configuration.

\- `course-registration-app/src/main/java/` — Java application source code.

\- `course-registration-app/src/test/java/` — JUnit tests.

\- `Jenkinsfile` — Jenkins Declarative Pipeline.

\- `deployment.yaml` — Kubernetes Deployment for Nginx.

\- `service.yaml` — Kubernetes NodePort Service.



\## 3. Prerequisites



\- Git

\- JDK 17

\- Apache Maven

\- Docker Desktop

\- Minikube

\- kubectl

\- Jenkins, if executing the pipeline through a Jenkins server



\## 4. Build and Test



Run the following commands from the repository root:



```powershell

mvn clean test

mvn package

```



The parent Maven project builds the `course-registration-app` module. JUnit tests run through Maven Surefire.



Test reports are generated in:



`course-registration-app/target/surefire-reports/`



The packaged JAR is generated under:



`course-registration-app/target/`



\## 5. Registration Validation



The registration service checks course capacity and validates that course credits are between 1 and 6 inclusive.



Tests cover:

\- Rejection when a course is full.

\- Acceptance when one seat remains.

\- Acceptance of the minimum and maximum valid credit values.

\- Rejection of zero, negative, and greater-than-six credit values.

\- State preservation for rejected registrations.



\## 6. Git Workflow



Work was performed on the `feature/course-validation` branch. Separate commits were created for the capacity defect fix, registration boundary and credit tests, credit validation, Maven multi-module restructuring, Jenkins pipeline, and Kubernetes configuration.



The completed feature branch was merged into `main`.



Use `git log --oneline --graph --all` to inspect the commit history.



\## 7. Jenkins Pipeline



The root `Jenkinsfile` defines stages for source checkout, Maven testing, and packaging. It publishes JUnit test reports and archives the packaged JAR after a successful pipeline.



The pipeline uses Windows `bat` steps and therefore requires a compatible Windows Jenkins agent. A successful local Maven build does not, by itself, prove that a live Jenkins execution succeeded.



\## 8. Kubernetes Deployment



Start Minikube and verify the cluster:



```powershell

minikube start --driver=docker

kubectl get nodes

```



Apply the Kubernetes resources:



```powershell

kubectl apply -f deployment.yaml

kubectl apply -f service.yaml

kubectl rollout status deployment/course-registration-nginx

```



Inspect the resources:



```powershell

kubectl get deployments

kubectl get pods

kubectl get services

```



Access the Nginx service:



```powershell

minikube service course-registration-service --url

```



Scale the deployment to three replicas:



```powershell

kubectl scale deployment course-registration-nginx --replicas=3

kubectl rollout status deployment/course-registration-nginx

kubectl get deployments

kubectl get pods

```



To demonstrate self-healing, delete one actual Pod by name:



```powershell

kubectl delete pod <actual-pod-name>

kubectl get pods -w

```



The Deployment controller creates a replacement Pod to restore the desired replica count. Press Ctrl+C to stop watching the Pods.



\## 9. Verification and Evidence



Record the actual output of:

\- `git log --oneline --graph --all`

\- `git status`

\- `mvn clean test`

\- `mvn package`

\- `kubectl get nodes`

\- `kubectl get deployments`

\- `kubectl get pods`

\- `kubectl get services`

\- Nginx service access

\- Scaling and Pod replacement



\## 10. Limitations



Local Maven build success and Kubernetes deployment success do not establish that a live Jenkins pipeline executed successfully. Include Jenkins build evidence only if the pipeline was actually run.

