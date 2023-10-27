
# jenkins
Các ví dụ liên quan đến Jenkins từ cơ bản đến nâng cao

Mỗi nhánh trong Repo sẽ là 1 ví dụ/ giải pháp/ project mẫu trong Jenkins

# Môi trường phát triển
- Jenkins server sẽ được triển khai lên Docker
- K8S cluster và Jenkins server sẽ có chung 1 network nên có thể contact qua lại được với nhau
- Đương nhiên Jenkins sẽ được cấu hình RBAC để có thể control được K8S Cluster/ CI systems/...
- Bản thân Jenkins server sẽ được mapping với 1 public domain để Github có thể tương tác được
<br>(sử dụng Webhook kết hợp với ngrok)

# Folder liên quan trên Windows
```
D:\Projects\jenkins
```
==============================================================
# Ví dụ [05.PipelineCI4Django]
==============================================================

**Tham khảo**
- https://www.jenkins.io/doc/pipeline/tour/running-multiple-steps/
- https://www.baeldung.com/ops/running-stages-in-parallel-jenkins-workflow-pipeline
- https://www.jenkins.io/doc/pipeline/tour/tests-and-artifacts/
- https://www.jenkins.io/doc/book/pipeline/jenkinsfile/#handling-credentials
- https://octopus.com/blog/managing-jenkins-credentials


**Tạo một Pipeline đơn giản `Pipeline4Django` sử dụng Docker để xử lý 1 Python/Django App:**
- Tham khảo từ ví dụ Django : https://github.com/trandungchien1982/django/tree/02.NavigateURLToApps
- Pipeline được mô tả trong file `Jenkinsfile`, sử dụng `Declarative Pipeline`
- Yêu cầu phải install Docker Pipeline plugin trước:
<br/>https://plugins.jenkins.io/docker-workflow/
<br/>From your Jenkins dashboard navigate to Manage Jenkins > Manage Plugins and select the Available tab. Locate this plugin by searching for docker-workflow.
- Các steps bao gồm:
  - Check Python Lint + Syntax + Code Smell
  - Build codes + Execute Unit Tests/Integration Tests
  - Build Docker Images and push it to Docker Registry
  - Hoàn tất Pipeline
  

**Nội dung file : `Jenkinsfile`:**
<br/>(Nằm ở folder gốc của Git/ branch mà ta đang refer tới trong kịch bản Pipeline)
```shell
/* Requires the Docker Pipeline plugin */
pipeline {
    agent any
    /* Thiết lập biến môi trường dùng chung cho các Stages khác nhau */
    environment {
        LOGIN_DOCKER_USR = credentials('login-docker-usr')
        LOGIN_DOCKER_PWD = credentials('login-docker-pwd')

        PUBLIC_VAR01 = 'Public Variable 01, tdc'
        PUBLIC_VAR02 = 'Public Variable 02, mnk'
    }
    stages {
        /* Excecute script in Alpine docker */
        stage('Executing script in Alpine docker for testing only ...') {
            agent { docker { image 'alpine:3.18.4' } }
            steps {
                sh 'ls -l'
                sh 'pwd'
                sh 'chmod +x ./jenkins-ci-django/simple-script.sh'
                sh './jenkins-ci-django/simple-script.sh'

                sh 'echo ------------------------------------------------- '
                sh 'echo "Print out the env var: PUBLIC_VAR01 = $PUBLIC_VAR01" '
                sh 'echo "Print out the env var: PUBLIC_VAR02 = $PUBLIC_VAR02" '
                sh 'echo ------------------------------------------------- '

                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '
                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '

                sh 'export TEST_LDU_USR=$LOGIN_DOCKER_USR'
                sh 'echo $TEST_LDU_USR'

                sh 'echo Finish run Testing Script for Django ...'
            }
        }

        /* TODO: Check code style/Code Smell pylint */
        stage('Check Python Lint + Syntax + Code Smell [PENDING] ... ') {
            agent { docker { image 'python:3.9.18-alpine3.18' } }
            steps {
                sh 'echo Check Python Lint + Syntax + Code Smell ...'
                sh 'python --version'
                sh 'ls -l'
                sh 'pwd'

                sh 'echo ------------------------------------------------- '
                sh 'echo "Print out the env var: PUBLIC_VAR01 = $PUBLIC_VAR01" '
                sh 'echo "Print out the env var: PUBLIC_VAR02 = $PUBLIC_VAR02" '
                sh 'echo ------------------------------------------------- '

                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '
                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '

                sh 'echo Finish Check code style Django using plint [PENDING] ... '
            }
        }

        /* TODO: Build Django project */
        stage('Build Django project + Execute test cases to make sure there is no error') {
            agent { docker { image 'python:3.9.18-alpine3.18' } }
            steps {
                sh 'echo "Build codes + Execute Unit Tests/Integration Tests" '
                sh 'ls -l'
                sh 'pwd'
                sh 'echo Try to build the Python/Django project ...'
                sh 'echo Finish build Python/Django project ...'
            }
        }

        /* TODO: Push the project Django to Docker Registry */
        stage('Build project and push to Docker Registry') {
            agent { docker { image 'docker:24.0.6-dind-alpine3.18' } }
            steps {
                sh 'echo "Build Docker Images and push it to Docker Registry" '
                sh 'docker --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'echo ------------------------------------------------- '
                sh 'echo "Print out the env var: PUBLIC_VAR01 = $PUBLIC_VAR01" '
                sh 'echo "Print out the env var: PUBLIC_VAR02 = $PUBLIC_VAR02" '
                sh 'echo ------------------------------------------------- '

                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '
                sh 'echo "Secret value: LOGIN_DOCKER_USR = $LOGIN_DOCKER_USR" '

                sh 'cat ./jenkins-ci-django/simple-script.sh'
                sh 'chmod +x ./jenkins-ci-django/simple-script.sh'
                sh './jenkins-ci-django/simple-script.sh'
                sh 'echo Try to build Dockerfile ...'
                sh 'chmod +x ./build-docker.sh'
                sh './build-docker.sh'
                sh 'echo Finish Push package project to Docker Registry in [Docker in Docker] env ...'
                sh 'echo Finish Jenkins CI Pipeline with multiple stages ...'
            }
        }
    }
}
```

**Kết quả chạy Pipeline (xem Console Output)**
```shell
Console Output
Started by user Admin
Obtained Jenkinsfile from git https://github.com/trandungchien1982/jenkins.git
[Pipeline] Start of Pipeline
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Pipeline4Django
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Declarative: Checkout SCM)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Pipeline4Django/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/05.PipelineCI4Django^{commit} # timeout=10
Checking out Revision 78a8361096d27a930fab60cce73a56faf2a58bf1 (refs/remotes/origin/05.PipelineCI4Django)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 78a8361096d27a930fab60cce73a56faf2a58bf1 # timeout=10
Commit message: "Add files to [05.PipelineCI4Django]"
 > git rev-list --no-walk 40591705dcd7a554c34e8eecbd5448553bc9d172 # timeout=10
[Pipeline] }
[Pipeline] // stage
[Pipeline] withEnv
[Pipeline] {
[Pipeline] withCredentials
Masking supported pattern matches of $LOGIN_DOCKER_PWD or $LOGIN_DOCKER_USR
[Pipeline] {
[Pipeline] withEnv
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Executing script in Alpine docker for testing only ...)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Pipeline4Django@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/05.PipelineCI4Django^{commit} # timeout=10
Checking out Revision 78a8361096d27a930fab60cce73a56faf2a58bf1 (refs/remotes/origin/05.PipelineCI4Django)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 78a8361096d27a930fab60cce73a56faf2a58bf1 # timeout=10
Commit message: "Add files to [05.PipelineCI4Django]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . alpine:3.18.4
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Pipeline4Django@2 --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** alpine:3.18.4 cat
$ docker top d34ef216006d23b7a429fb015c8c1a55622138075e513e793a94358a33fe26f0 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ ls -l
total 28
-rw-r--r--    1 1000     1000          4327 Oct 27 08:14 Jenkinsfile
-rw-r--r--    1 1000     1000          2304 Oct 27 07:26 README.md
-rwxr-xr-x    1 1000     1000           842 Oct 27 08:14 build-docker.sh
drwxr-xr-x    4 1000     1000          4096 Oct 27 07:26 django-app
-rw-r--r--    1 1000     1000           656 Oct 27 07:17 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 27 08:14 jenkins-ci-django
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] sh
+ chmod +x ./jenkins-ci-django/simple-script.sh
[Pipeline] sh
+ ./jenkins-ci-django/simple-script.sh
Hello! This is the content of script in Jenkins Django Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo -------------------------------------------------
-------------------------------------------------
[Pipeline] sh
+ echo 'Print out the env var: PUBLIC_VAR01 = Public Variable 01, tdc'
Print out the env var: PUBLIC_VAR01 = Public Variable 01, tdc
[Pipeline] sh
+ echo 'Print out the env var: PUBLIC_VAR02 = Public Variable 02, mnk'
Print out the env var: PUBLIC_VAR02 = Public Variable 02, mnk
[Pipeline] sh
+ echo -------------------------------------------------
-------------------------------------------------
[Pipeline] sh
+ echo 'Secret value: LOGIN_DOCKER_USR = ****'
Secret value: LOGIN_DOCKER_USR = ****
[Pipeline] sh
+ echo 'Secret value: LOGIN_DOCKER_USR = ****'
Secret value: LOGIN_DOCKER_USR = ****
[Pipeline] sh
+ export 'TEST_LDU_USR=****'
[Pipeline] sh
+ echo

[Pipeline] sh
+ echo Finish run Testing Script 'for' Django ...
Finish run Testing Script for Django ...
[Pipeline] }
$ docker stop --time=1 d34ef216006d23b7a429fb015c8c1a55622138075e513e793a94358a33fe26f0
$ docker rm -f --volumes d34ef216006d23b7a429fb015c8c1a55622138075e513e793a94358a33fe26f0
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Check Python Lint + Syntax + Code Smell [PENDING] ... )
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Pipeline4Django@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/05.PipelineCI4Django^{commit} # timeout=10
Checking out Revision 78a8361096d27a930fab60cce73a56faf2a58bf1 (refs/remotes/origin/05.PipelineCI4Django)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 78a8361096d27a930fab60cce73a56faf2a58bf1 # timeout=10
Commit message: "Add files to [05.PipelineCI4Django]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . python:3.9.18-alpine3.18
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Pipeline4Django@2 --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** python:3.9.18-alpine3.18 cat
$ docker top b16d914b0e31d9448fe11ad204c122ec7f71c1893e03d87700db060e32957e52 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ echo Check Python Lint + Syntax + Code Smell ...
Check Python Lint + Syntax + Code Smell ...
[Pipeline] sh
+ python --version
Python 3.9.18
[Pipeline] sh
+ ls -l
total 28
-rw-r--r--    1 1000     1000          4327 Oct 27 08:14 Jenkinsfile
-rw-r--r--    1 1000     1000          2304 Oct 27 07:26 README.md
-rwxr-xr-x    1 1000     1000           842 Oct 27 08:14 build-docker.sh
drwxr-xr-x    4 1000     1000          4096 Oct 27 07:26 django-app
-rw-r--r--    1 1000     1000           656 Oct 27 07:17 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 27 08:15 jenkins-ci-django
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] sh
+ echo -------------------------------------------------
-------------------------------------------------
[Pipeline] sh
+ echo 'Print out the env var: PUBLIC_VAR01 = Public Variable 01, tdc'
Print out the env var: PUBLIC_VAR01 = Public Variable 01, tdc
[Pipeline] sh
+ echo 'Print out the env var: PUBLIC_VAR02 = Public Variable 02, mnk'
Print out the env var: PUBLIC_VAR02 = Public Variable 02, mnk
[Pipeline] sh
+ echo -------------------------------------------------
-------------------------------------------------
[Pipeline] sh
+ echo 'Secret value: LOGIN_DOCKER_USR = ****'
Secret value: LOGIN_DOCKER_USR = ****
[Pipeline] sh
+ echo 'Secret value: LOGIN_DOCKER_USR = ****'
Secret value: LOGIN_DOCKER_USR = ****
[Pipeline] sh
+ echo Finish Check code style Django using plint '[PENDING]' ...
Finish Check code style Django using plint [PENDING] ...
[Pipeline] }
$ docker stop --time=1 b16d914b0e31d9448fe11ad204c122ec7f71c1893e03d87700db060e32957e52
$ docker rm -f --volumes b16d914b0e31d9448fe11ad204c122ec7f71c1893e03d87700db060e32957e52
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Build Django project + Execute test cases to make sure there is no error)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Pipeline4Django@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/05.PipelineCI4Django^{commit} # timeout=10
Checking out Revision 78a8361096d27a930fab60cce73a56faf2a58bf1 (refs/remotes/origin/05.PipelineCI4Django)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 78a8361096d27a930fab60cce73a56faf2a58bf1 # timeout=10
Commit message: "Add files to [05.PipelineCI4Django]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . python:3.9.18-alpine3.18
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Pipeline4Django@2 --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** python:3.9.18-alpine3.18 cat
$ docker top 4f2e4d5101392bd2233bf9e38f61b9812be58db2f0143be0ac53b15174d2d12f -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ echo 'Build codes + Execute Unit Tests/Integration Tests'
Build codes + Execute Unit Tests/Integration Tests
[Pipeline] sh
+ ls -l
total 28
-rw-r--r--    1 1000     1000          4327 Oct 27 08:14 Jenkinsfile
-rw-r--r--    1 1000     1000          2304 Oct 27 07:26 README.md
-rwxr-xr-x    1 1000     1000           842 Oct 27 08:14 build-docker.sh
drwxr-xr-x    4 1000     1000          4096 Oct 27 07:26 django-app
-rw-r--r--    1 1000     1000           656 Oct 27 07:17 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 27 08:15 jenkins-ci-django
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] sh
+ echo Try to build the Python/Django project ...
Try to build the Python/Django project ...
[Pipeline] sh
+ echo Finish build Python/Django project ...
Finish build Python/Django project ...
[Pipeline] }
$ docker stop --time=1 4f2e4d5101392bd2233bf9e38f61b9812be58db2f0143be0ac53b15174d2d12f
$ docker rm -f --volumes 4f2e4d5101392bd2233bf9e38f61b9812be58db2f0143be0ac53b15174d2d12f
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Build project and push to Docker Registry)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Pipeline4Django@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/05.PipelineCI4Django^{commit} # timeout=10
Checking out Revision 78a8361096d27a930fab60cce73a56faf2a58bf1 (refs/remotes/origin/05.PipelineCI4Django)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 78a8361096d27a930fab60cce73a56faf2a58bf1 # timeout=10
Commit message: "Add files to [05.PipelineCI4Django]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . docker:24.0.6-dind-alpine3.18
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Pipeline4Django@2 --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** docker:24.0.6-dind-alpine3.18 cat
$ docker top 6342cf45fb740b62bde3eb4546aa279c39d8a7a55aed9fa94219d8396aaf1815 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ echo 'Build Docker Images and push it to Docker Registry'
Build Docker Images and push it to Docker Registry
[Pipeline] sh
+ docker --version
Docker version 24.0.6, build ed223bc
[Pipeline] sh
+ ls -l
total 28
-rw-r--r--    1 1000     1000          4327 Oct 27 08:14 Jenkinsfile
-rw-r--r--    1 1000     1000          2304 Oct 27 07:26 README.md
-rwxr-xr-x    1 1000     1000           842 Oct 27 08:14 build-docker.sh
drwxr-xr-x    4 1000     1000          4096 Oct 27 07:26 django-app
-rw-r--r--    1 1000     1000           656 Oct 27 07:17 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 27 08:15 jenkins-ci-django
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Pipeline4Django@2
[Pipeline] sh
+ echo -------------------------------------------------
-------------------------------------------------
[Pipeline] sh
+ echo 'Print out the env var: PUBLIC_VAR01 = Public Variable 01, tdc'
Print out the env var: PUBLIC_VAR01 = Public Variable 01, tdc
[Pipeline] sh
+ echo 'Print out the env var: PUBLIC_VAR02 = Public Variable 02, mnk'
Print out the env var: PUBLIC_VAR02 = Public Variable 02, mnk
[Pipeline] sh
+ echo -------------------------------------------------
-------------------------------------------------
[Pipeline] sh
+ echo 'Secret value: LOGIN_DOCKER_USR = ****'
Secret value: LOGIN_DOCKER_USR = ****
[Pipeline] sh
+ echo 'Secret value: LOGIN_DOCKER_USR = ****'
Secret value: LOGIN_DOCKER_USR = ****
[Pipeline] sh
+ cat ./jenkins-ci-django/simple-script.sh

echo 'Hello! This is the content of script in Jenkins Django Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./jenkins-ci-django/simple-script.sh
[Pipeline] sh
+ ./jenkins-ci-django/simple-script.sh
Hello! This is the content of script in Jenkins Django Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Try to build Dockerfile ...
Try to build Dockerfile ...
[Pipeline] sh
+ chmod +x ./build-docker.sh
[Pipeline] sh
+ ./build-docker.sh
Build Docker Image for Python NavigateUrl: tdchien1982/jenkins:05.jenkins-ci-django-navigate-url-1.0
Linux 6342cf45fb74 5.15.0-87-generic #97~20.04.1-Ubuntu SMP Thu Oct 5 08:25:28 UTC 2023 x86_64 Linux
Check Docker Version
Client:
 Version:           24.0.6
 API version:       1.43
 Go version:        go1.20.7
 Git commit:        ed223bc
 Built:             Mon Sep  4 12:30:51 2023
 OS/Arch:           linux/amd64
 Context:           default

Server:
 Engine:
  Version:          24.0.5
  API version:      1.43 (minimum version 1.12)
  Go version:       go1.20.3
  Git commit:       24.0.5-0ubuntu1~20.04.1
  Built:            Mon Aug 21 19:50:14 2023
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.7.2
  GitCommit:        
 runc:
  Version:          1.1.7-0ubuntu1~20.04.1
  GitCommit:        
 docker-init:
  Version:          0.19.0
  GitCommit:        
Remove the old image (if exists) tdchien1982/jenkins:05.jenkins-ci-django-navigate-url-1.0
Error response from daemon: No such container: django-navigate-url-demo
Error response from daemon: No such container: django-navigate-url-demo
Error response from daemon: No such image: tdchien1982/jenkins:05.jenkins-ci-django-navigate-url-1.0
Create Docker Image for Python NavigateUrl: tdchien1982/jenkins:05.jenkins-ci-django-navigate-url-1.0
ERROR: mkdir /.docker: permission denied
Push new image to Docker Registry
Error: Cannot perform an interactive login from a non TTY device
The push refers to repository [docker.io/tdchien1982/jenkins]
tag does not exist: tdchien1982/jenkins:05.jenkins-ci-django-navigate-url-1.0
Run App using Docker on localhost
[Pipeline] sh
+ echo Finish Push package project to Docker Registry 'in' '[Docker' 'in' Docker] env ...
Finish Push package project to Docker Registry in [Docker in Docker] env ...
[Pipeline] sh
+ echo Finish Jenkins CI Pipeline with multiple stages ...
Finish Jenkins CI Pipeline with multiple stages ...
[Pipeline] }
$ docker stop --time=1 6342cf45fb740b62bde3eb4546aa279c39d8a7a55aed9fa94219d8396aaf1815
$ docker rm -f --volumes 6342cf45fb740b62bde3eb4546aa279c39d8a7a55aed9fa94219d8396aaf1815
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```
