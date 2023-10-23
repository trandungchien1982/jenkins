
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
# Ví dụ [03.MultiStages]
==============================================================

**Tham khảo**
- https://www.jenkins.io/doc/pipeline/tour/hello-world/
- https://www.jenkins.io/doc/pipeline/tour/running-multiple-steps/
- https://www.baeldung.com/ops/running-stages-in-parallel-jenkins-workflow-pipeline


**Tạo một Pipeline đơn giản `Simple-MultiStages` sử dụng Docker dành cho nhiều môi trường khác nhau:**
- Pipeline được mô tả trong file `Jenkinsfile`, sử dụng `Declarative Pipeline`
- Yêu cầu phải install Docker Pipeline plugin trước:
<br/>https://plugins.jenkins.io/docker-workflow/
<br/>From your Jenkins dashboard navigate to Manage Jenkins > Manage Plugins and select the Available tab. Locate this plugin by searching for docker-workflow.
- Các steps bao gồm:
  - Java+Maven: Build project 
  - Python: Check ES Lint 
  - NodeJS: Kiểm tra Syntax 
  - Golang: Chạy Unit Test 
  - Ruby: Đóng gói dự án thành file Jar/War 
  

**Nội dung file : `Jenkinsfile`:**
<br/>(Nằm ở folder gốc của Git/ branch mà ta đang refer tới trong kịch bản Pipeline)
```shell
/* Requires the Docker Pipeline plugin */
pipeline {
    agent none
    stages {
        /* Java + Maven env */
        stage('Build project in Java+Maven') {
            agent { docker { image 'maven:3.9.5-eclipse-temurin-11-alpine' } }
            steps {
                sh 'mvn --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Build [Java+Maven] ...'
            }
        }

        /* Python env */
        stage('Check ES Lint in Python') {
            agent { docker { image 'python:3.12.0-alpine3.18' } }
            steps {
                sh 'python --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Check ES Lint in [Python] env ...'
            }
        }

        /* NodeJS env */
        stage('Check Syntax in NodeJS env') {
            agent { docker { image 'node:18.18.2-alpine3.18' } }
            steps {
                sh 'node --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Check ES Lint in [NodeJS] env ...'
            }
        }

        /* Golang env */
        stage('Run Unit Test in Golang env') {
            agent { docker { image 'golang:1.20.10-alpine3.17' } }
            steps {
                sh 'go version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Run Unit Test in [Golang] env ...'
            }
        }

        /* Ruby env */
        stage('Package project to Jar/War in Ruby env') {
            agent { docker { image 'ruby:3.2.2-alpine3.18' } }
            steps {
                sh 'ruby --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
                sh 'echo Finish Package project to Jar/War in [Ruby] env ...'
            }
        }

        /* Ruby env */
        stage('Build project and push to Docker Registry') {
            agent { docker { image 'docker:24.0.6-dind-alpine3.18' } }
            steps {
                sh 'docker --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./multi-stages-pipeline/simple-script.sh'
                sh 'chmod +x ./multi-stages-pipeline/simple-script.sh'
                sh './multi-stages-pipeline/simple-script.sh'
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
[Pipeline] stage
[Pipeline] { (Build project in Java+Maven)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Multi-Stages
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Multi-Stages/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/03.MultiStages^{commit} # timeout=10
Checking out Revision dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 (refs/remotes/origin/03.MultiStages)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 # timeout=10
Commit message: "Add files to [03.MultiStages]"
 > git rev-list --no-walk e67a228452f8371154969de27a02772602278a1b # timeout=10
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . maven:3.9.5-eclipse-temurin-11-alpine
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Multi-Stages --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** maven:3.9.5-eclipse-temurin-11-alpine cat
$ docker top f58a0489270bc55e7590a4d2b7a1a72c2ba52e0cc8a8e452f106027319b2d773 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ mvn --version
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: /usr/share/maven
Java version: 11.0.20.1, vendor: Eclipse Adoptium, runtime: /opt/java/openjdk
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.15.0-87-generic", arch: "amd64", family: "unix"
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          3460 Oct 23 07:53 Jenkinsfile
-rw-r--r--    1 1000     1000          2427 Oct 23 07:29 README.md
-rw-r--r--    1 1000     1000           656 Oct 23 07:29 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 23 07:48 multi-stages-pipeline
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Multi-Stages
[Pipeline] sh
+ cat ./multi-stages-pipeline/simple-script.sh

echo 'Hello! This is the content of script in Jenkins MultiStages Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./multi-stages-pipeline/simple-script.sh
[Pipeline] sh
+ ./multi-stages-pipeline/simple-script.sh
Hello! This is the content of script in Jenkins MultiStages Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish Build '[Java+Maven]' ...
Finish Build [Java+Maven] ...
[Pipeline] }
$ docker stop --time=1 f58a0489270bc55e7590a4d2b7a1a72c2ba52e0cc8a8e452f106027319b2d773
$ docker rm -f --volumes f58a0489270bc55e7590a4d2b7a1a72c2ba52e0cc8a8e452f106027319b2d773
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Check ES Lint in Python)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Multi-Stages
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Multi-Stages/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/03.MultiStages^{commit} # timeout=10
Checking out Revision dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 (refs/remotes/origin/03.MultiStages)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 # timeout=10
Commit message: "Add files to [03.MultiStages]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . python:3.12.0-alpine3.18
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Multi-Stages --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** python:3.12.0-alpine3.18 cat
$ docker top be693a7611cc9321d97342b3b08aa63c40c169e04219dae5399eb677d7c217f5 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ python --version
Python 3.12.0
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          3460 Oct 23 07:53 Jenkinsfile
-rw-r--r--    1 1000     1000          2427 Oct 23 07:29 README.md
-rw-r--r--    1 1000     1000           656 Oct 23 07:29 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 23 07:53 multi-stages-pipeline
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Multi-Stages
[Pipeline] sh
+ cat ./multi-stages-pipeline/simple-script.sh

echo 'Hello! This is the content of script in Jenkins MultiStages Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./multi-stages-pipeline/simple-script.sh
[Pipeline] sh
+ ./multi-stages-pipeline/simple-script.sh
Hello! This is the content of script in Jenkins MultiStages Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish Check ES Lint 'in' '[Python]' env ...
Finish Check ES Lint in [Python] env ...
[Pipeline] }
$ docker stop --time=1 be693a7611cc9321d97342b3b08aa63c40c169e04219dae5399eb677d7c217f5
$ docker rm -f --volumes be693a7611cc9321d97342b3b08aa63c40c169e04219dae5399eb677d7c217f5
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Check Syntax in NodeJS env)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Multi-Stages
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Multi-Stages/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/03.MultiStages^{commit} # timeout=10
Checking out Revision dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 (refs/remotes/origin/03.MultiStages)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 # timeout=10
Commit message: "Add files to [03.MultiStages]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . node:18.18.2-alpine3.18
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Multi-Stages --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** node:18.18.2-alpine3.18 cat
$ docker top 442fe8f9a1aaa72d880210f983e5d76e57006a0332f58e60e9b3cd0ffe4e2181 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ node --version
v18.18.2
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 node     node          3460 Oct 23 07:53 Jenkinsfile
-rw-r--r--    1 node     node          2427 Oct 23 07:29 README.md
-rw-r--r--    1 node     node           656 Oct 23 07:29 docker-compose.yaml
drwxr-xr-x    2 node     node          4096 Oct 23 07:53 multi-stages-pipeline
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Multi-Stages
[Pipeline] sh
+ cat ./multi-stages-pipeline/simple-script.sh

echo 'Hello! This is the content of script in Jenkins MultiStages Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./multi-stages-pipeline/simple-script.sh
[Pipeline] sh
+ ./multi-stages-pipeline/simple-script.sh
Hello! This is the content of script in Jenkins MultiStages Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish Check ES Lint 'in' '[NodeJS]' env ...
Finish Check ES Lint in [NodeJS] env ...
[Pipeline] }
$ docker stop --time=1 442fe8f9a1aaa72d880210f983e5d76e57006a0332f58e60e9b3cd0ffe4e2181
$ docker rm -f --volumes 442fe8f9a1aaa72d880210f983e5d76e57006a0332f58e60e9b3cd0ffe4e2181
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Run Unit Test in Golang env)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Multi-Stages
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Multi-Stages/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/03.MultiStages^{commit} # timeout=10
Checking out Revision dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 (refs/remotes/origin/03.MultiStages)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 # timeout=10
Commit message: "Add files to [03.MultiStages]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . golang:1.20.10-alpine3.17
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Multi-Stages --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** golang:1.20.10-alpine3.17 cat
$ docker top 3fddf1f328d25c3fcd0b2780a53536eaa56ce3ba8fd9d9bf03289724ec4225e8 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ go version
go version go1.20.10 linux/amd64
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          3460 Oct 23 07:53 Jenkinsfile
-rw-r--r--    1 1000     1000          2427 Oct 23 07:29 README.md
-rw-r--r--    1 1000     1000           656 Oct 23 07:29 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 23 07:53 multi-stages-pipeline
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Multi-Stages
[Pipeline] sh
+ cat ./multi-stages-pipeline/simple-script.sh

echo 'Hello! This is the content of script in Jenkins MultiStages Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./multi-stages-pipeline/simple-script.sh
[Pipeline] sh
+ ./multi-stages-pipeline/simple-script.sh
Hello! This is the content of script in Jenkins MultiStages Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish Run Unit Test 'in' '[Golang]' env ...
Finish Run Unit Test in [Golang] env ...
[Pipeline] }
$ docker stop --time=1 3fddf1f328d25c3fcd0b2780a53536eaa56ce3ba8fd9d9bf03289724ec4225e8
$ docker rm -f --volumes 3fddf1f328d25c3fcd0b2780a53536eaa56ce3ba8fd9d9bf03289724ec4225e8
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Package project to Jar/War in Ruby env)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/Multi-Stages
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Multi-Stages/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/03.MultiStages^{commit} # timeout=10
Checking out Revision dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 (refs/remotes/origin/03.MultiStages)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 # timeout=10
Commit message: "Add files to [03.MultiStages]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . ruby:3.2.2-alpine3.18
.
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Multi-Stages --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** ruby:3.2.2-alpine3.18 cat
$ docker top 84a1b2a245562c862f5619f780aae3a725e851de2d82fd12bd5166cc6be00af7 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ ruby --version
ruby 3.2.2 (2023-03-30 revision e51014f9c0) [x86_64-linux-musl]
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          3460 Oct 23 07:53 Jenkinsfile
-rw-r--r--    1 1000     1000          2427 Oct 23 07:29 README.md
-rw-r--r--    1 1000     1000           656 Oct 23 07:29 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 23 07:53 multi-stages-pipeline
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Multi-Stages
[Pipeline] sh
+ cat ./multi-stages-pipeline/simple-script.sh

echo 'Hello! This is the content of script in Jenkins MultiStages Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./multi-stages-pipeline/simple-script.sh
[Pipeline] sh
+ ./multi-stages-pipeline/simple-script.sh
Hello! This is the content of script in Jenkins MultiStages Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish Package project to Jar/War 'in' '[Ruby]' env ...
Finish Package project to Jar/War in [Ruby] env ...
[Pipeline] }
$ docker stop --time=1 84a1b2a245562c862f5619f780aae3a725e851de2d82fd12bd5166cc6be00af7
$ docker rm -f --volumes 84a1b2a245562c862f5619f780aae3a725e851de2d82fd12bd5166cc6be00af7
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
Running on Jenkins in /var/jenkins_home/workspace/Multi-Stages
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Multi-Stages/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/03.MultiStages^{commit} # timeout=10
Checking out Revision dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 (refs/remotes/origin/03.MultiStages)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f dbaf54bbdd49ce159a6f4dbd80fc44303b3b8539 # timeout=10
Commit message: "Add files to [03.MultiStages]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . docker:24.0.6-dind-alpine3.18

Error: No such object: docker:24.0.6-dind-alpine3.18
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker pull docker:24.0.6-dind-alpine3.18
24.0.6-dind-alpine3.18: Pulling from library/docker
96526aa774ef: Already exists
636b7876979d: Pulling fs layer
4f4fb700ef54: Pulling fs layer
130a3e1e7df5: Pulling fs layer
60190b130ff2: Pulling fs layer
9ce2dbd107eb: Pulling fs layer
c711618a8d74: Pulling fs layer
b1a9d1fa736b: Pulling fs layer
7a915d3d782d: Pulling fs layer
63fa7f453749: Pulling fs layer
ce8f4a45972f: Pulling fs layer
a88060cc894f: Pulling fs layer
09e0ea80a90a: Pulling fs layer
6831f6f9e30f: Pulling fs layer
7a915d3d782d: Waiting
60190b130ff2: Waiting
9ce2dbd107eb: Waiting
c711618a8d74: Waiting
b1a9d1fa736b: Waiting
09e0ea80a90a: Waiting
63fa7f453749: Waiting
ce8f4a45972f: Waiting
a88060cc894f: Waiting
6831f6f9e30f: Waiting
4f4fb700ef54: Verifying Checksum
4f4fb700ef54: Download complete
636b7876979d: Verifying Checksum
636b7876979d: Download complete
636b7876979d: Pull complete
4f4fb700ef54: Pull complete
130a3e1e7df5: Download complete
130a3e1e7df5: Pull complete
c711618a8d74: Verifying Checksum
c711618a8d74: Download complete
9ce2dbd107eb: Verifying Checksum
9ce2dbd107eb: Download complete
b1a9d1fa736b: Verifying Checksum
b1a9d1fa736b: Download complete
7a915d3d782d: Verifying Checksum
7a915d3d782d: Download complete
60190b130ff2: Verifying Checksum
60190b130ff2: Download complete
60190b130ff2: Pull complete
ce8f4a45972f: Verifying Checksum
ce8f4a45972f: Download complete
9ce2dbd107eb: Pull complete
c711618a8d74: Pull complete
b1a9d1fa736b: Pull complete
7a915d3d782d: Pull complete
63fa7f453749: Download complete
63fa7f453749: Pull complete
ce8f4a45972f: Pull complete
09e0ea80a90a: Verifying Checksum
09e0ea80a90a: Download complete
6831f6f9e30f: Download complete
a88060cc894f: Verifying Checksum
a88060cc894f: Download complete
a88060cc894f: Pull complete
09e0ea80a90a: Pull complete
6831f6f9e30f: Pull complete
Digest: sha256:0752ca4e936da012c173c119217c0f9599b3b191c1557e53206d5d06d2627580
Status: Downloaded newer image for docker:24.0.6-dind-alpine3.18
docker.io/library/docker:24.0.6-dind-alpine3.18
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Multi-Stages --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** docker:24.0.6-dind-alpine3.18 cat
$ docker top 2596f100aca42e07ea43f7f25e48644bf5816ef2cb7f6bc0ec5de84438345450 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ docker --version
Docker version 24.0.6, build ed223bc
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          3460 Oct 23 07:53 Jenkinsfile
-rw-r--r--    1 1000     1000          2427 Oct 23 07:29 README.md
-rw-r--r--    1 1000     1000           656 Oct 23 07:29 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 23 07:53 multi-stages-pipeline
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Multi-Stages
[Pipeline] sh
+ cat ./multi-stages-pipeline/simple-script.sh

echo 'Hello! This is the content of script in Jenkins MultiStages Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./multi-stages-pipeline/simple-script.sh
[Pipeline] sh
+ ./multi-stages-pipeline/simple-script.sh
Hello! This is the content of script in Jenkins MultiStages Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish Push package project to Docker Registry 'in' '[Docker' 'in' Docker] env ...
Finish Push package project to Docker Registry in [Docker in Docker] env ...
[Pipeline] sh
+ echo Finish Jenkins CI Pipeline with multiple stages ...
Finish Jenkins CI Pipeline with multiple stages ...
[Pipeline] }
$ docker stop --time=1 2596f100aca42e07ea43f7f25e48644bf5816ef2cb7f6bc0ec5de84438345450
$ docker rm -f --volumes 2596f100aca42e07ea43f7f25e48644bf5816ef2cb7f6bc0ec5de84438345450
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] End of Pipeline
Finished: SUCCESS
```
