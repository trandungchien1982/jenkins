
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
# Ví dụ [04.PipelineCI4Java]
==============================================================

**Tham khảo**
- https://www.jenkins.io/doc/pipeline/tour/running-multiple-steps/
- https://www.baeldung.com/ops/running-stages-in-parallel-jenkins-workflow-pipeline
- https://www.jenkins.io/doc/pipeline/tour/tests-and-artifacts/


**Tạo một Pipeline đơn giản `CI-Java` sử dụng Docker để xử lý 1 Java App:**
- Tham khảo từ ví dụ HelloWorld : https://github.com/trandungchien1982/spring/tree/01.HelloWorld
- Pipeline được mô tả trong file `Jenkinsfile`, sử dụng `Declarative Pipeline`
- Yêu cầu phải install Docker Pipeline plugin trước:
<br/>https://plugins.jenkins.io/docker-workflow/
<br/>From your Jenkins dashboard navigate to Manage Jenkins > Manage Plugins and select the Available tab. Locate this plugin by searching for docker-workflow.
- Các steps bao gồm:
  - Check Java Lint + Syntax + Code Smell [TODO: Chưa impl hoàn chỉnh, chỉ để tạm 1 message để bypass mà thôi]
  - Build codes + Execute JUnit Tests/Integration Tests
  - Build Docker Images and push it to Docker Registry [TODO: Chưa impl hoàn chỉnh vì cần Login Docker Credentials ... ]
  - Hoàn tất Pipeline
  

**Nội dung file : `Jenkinsfile`:**
<br/>(Nằm ở folder gốc của Git/ branch mà ta đang refer tới trong kịch bản Pipeline)
```shell
/* Requires the Docker Pipeline plugin */
pipeline {
    agent any
    stages {
        /* Excecute script in Alpine docker */
        stage('Executing script in Alpine docker for testing only ...') {
            agent { docker { image 'alpine:3.18.4' } }
            steps {
                sh 'ls -l'
                sh 'pwd'
                sh 'chmod +x ./jenkins-ci-java/simple-script.sh'
                sh './jenkins-ci-java/simple-script.sh'
                sh 'echo Finish Check code style Java using Linter [Java+Maven], [PENDING] ...'
            }
        }

        /* TODO: Check code style/Code Smell by sonarlint */
        stage('Check code style in Java, using SonarLint [PENDING] ... ') {
            agent { docker { image 'maven:3.9.5-eclipse-temurin-11-alpine' } }
            steps {
                sh 'mvn --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'echo Finish Check code style Java using Linter [Java+Maven], [PENDING] ... '
            }
        }

        /* Build Jar file using Gradle */
        stage('Build source code Java into JAR file') {
            agent { docker { image 'gradle:7-jdk11-alpine' } }
            steps {
                sh 'ls -l'
                sh 'pwd'
                sh 'echo Try to build the Java Project into JAR file ...'
                sh 'gradle --version'
                sh 'gradle clean build --build-file ./hello-world-app/build.gradle'
                sh 'echo Finish build Java project using Gradle, we will have the ./hello-world-app/build/libs/hello-world-0.0.1-SNAPSHOT.jar'
                sh 'ls -l ./hello-world-app/build/libs'
            }
        }

        /* Build Docker Image */
        stage('Build project and push to Docker Registry') {
            agent { docker { image 'docker:24.0.6-dind-alpine3.18' } }
            steps {
                sh 'docker --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./jenkins-ci-java/simple-script.sh'
                sh 'chmod +x ./jenkins-ci-java/simple-script.sh'
                sh './jenkins-ci-java/simple-script.sh'
                sh 'echo Try to list all artifacts in previous steps ...'
                sh 'ls -l ./hello-world-app/build/libs'
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
Running on Jenkins in /var/jenkins_home/workspace/PipelineCI4Java
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Declarative: Checkout SCM)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/PipelineCI4Java/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/04.PipelineCI4Java^{commit} # timeout=10
Checking out Revision 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 (refs/remotes/origin/04.PipelineCI4Java)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 # timeout=10
Commit message: "Add files to [04.PipelineCI4Java]"
 > git rev-list --no-walk 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 # timeout=10
[Pipeline] }
[Pipeline] // stage
[Pipeline] withEnv
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Executing script in Alpine docker for testing only ...)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/PipelineCI4Java@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/04.PipelineCI4Java^{commit} # timeout=10
Checking out Revision 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 (refs/remotes/origin/04.PipelineCI4Java)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 # timeout=10
Commit message: "Add files to [04.PipelineCI4Java]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . alpine:3.18.4

Error: No such object: alpine:3.18.4
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker pull alpine:3.18.4
3.18.4: Pulling from library/alpine
96526aa774ef: Already exists
Digest: sha256:eece025e432126ce23f223450a0326fbebde39cdf496a85d8c016293fc851978
Status: Downloaded newer image for alpine:3.18.4
docker.io/library/alpine:3.18.4
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/PipelineCI4Java@2 --volumes-from b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** alpine:3.18.4 cat
$ docker top 8b3caac860844816445467b52b9a52bae627783d8ce855f270a505b1f904a025 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          2554 Oct 25 03:28 Jenkinsfile
-rw-r--r--    1 1000     1000          2222 Oct 25 03:28 README.md
-rwxr-xr-x    1 1000     1000           780 Oct 25 03:28 build-docker.sh
-rw-r--r--    1 1000     1000           656 Oct 25 03:28 docker-compose.yaml
drwxr-xr-x   10 1000     1000           320 Oct 25 03:28 hello-world-app
drwxr-xr-x    3 1000     1000            96 Oct 25 03:28 jenkins-ci-java
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] sh
+ chmod +x ./jenkins-ci-java/simple-script.sh
[Pipeline] sh
+ ./jenkins-ci-java/simple-script.sh
Hello! This is the content of script in Jenkins Java Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish Check code style Java using Linter '[Java+Maven],' '[PENDING]' ...
Finish Check code style Java using Linter [Java+Maven], [PENDING] ...
[Pipeline] }
$ docker stop --time=1 8b3caac860844816445467b52b9a52bae627783d8ce855f270a505b1f904a025
$ docker rm -f --volumes 8b3caac860844816445467b52b9a52bae627783d8ce855f270a505b1f904a025
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Check code style in Java, using SonarLint [PENDING] ... )
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/PipelineCI4Java@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/04.PipelineCI4Java^{commit} # timeout=10
Checking out Revision 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 (refs/remotes/origin/04.PipelineCI4Java)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 # timeout=10
Commit message: "Add files to [04.PipelineCI4Java]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . maven:3.9.5-eclipse-temurin-11-alpine

Error: No such object: maven:3.9.5-eclipse-temurin-11-alpine
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker pull maven:3.9.5-eclipse-temurin-11-alpine
3.9.5-eclipse-temurin-11-alpine: Pulling from library/maven
96526aa774ef: Already exists
f42efc8ffaa9: Pulling fs layer
e8889f3a7977: Pulling fs layer
4859c4719abf: Pulling fs layer
98b582a93389: Pulling fs layer
342a6acd212a: Pulling fs layer
54c3815183a6: Pulling fs layer
8d0cbd7d86d1: Pulling fs layer
45b50e043cf3: Pulling fs layer
4910b4a2c628: Pulling fs layer
54c3815183a6: Waiting
8d0cbd7d86d1: Waiting
45b50e043cf3: Waiting
4910b4a2c628: Waiting
98b582a93389: Waiting
342a6acd212a: Waiting
4859c4719abf: Verifying Checksum
4859c4719abf: Download complete
98b582a93389: Verifying Checksum
98b582a93389: Download complete
f42efc8ffaa9: Verifying Checksum
f42efc8ffaa9: Download complete
342a6acd212a: Verifying Checksum
342a6acd212a: Download complete
8d0cbd7d86d1: Verifying Checksum
8d0cbd7d86d1: Download complete
45b50e043cf3: Verifying Checksum
45b50e043cf3: Download complete
54c3815183a6: Verifying Checksum
54c3815183a6: Download complete
4910b4a2c628: Verifying Checksum
4910b4a2c628: Download complete
f42efc8ffaa9: Pull complete
e8889f3a7977: Verifying Checksum
e8889f3a7977: Download complete
e8889f3a7977: Pull complete
4859c4719abf: Pull complete
98b582a93389: Pull complete
342a6acd212a: Pull complete
54c3815183a6: Pull complete
8d0cbd7d86d1: Pull complete
45b50e043cf3: Pull complete
4910b4a2c628: Pull complete
Digest: sha256:e68a3bb98b1fd7dbcd36ef9004db3eabff2fbea41704d20eb7d42c74ae133ffa
Status: Downloaded newer image for maven:3.9.5-eclipse-temurin-11-alpine
docker.io/library/maven:3.9.5-eclipse-temurin-11-alpine
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/PipelineCI4Java@2 --volumes-from b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** maven:3.9.5-eclipse-temurin-11-alpine cat
$ docker top 56d1cee3f1d21ef4b54ec9c3916dee23aef55f9a271fe65887fb73ebe4a19418 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ mvn --version
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: /usr/share/maven
Java version: 11.0.20.1, vendor: Eclipse Adoptium, runtime: /opt/java/openjdk
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.10.104-linuxkit", arch: "amd64", family: "unix"
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          2554 Oct 25 03:28 Jenkinsfile
-rw-r--r--    1 1000     1000          2222 Oct 25 03:28 README.md
-rwxr-xr-x    1 1000     1000           780 Oct 25 03:28 build-docker.sh
-rw-r--r--    1 1000     1000           656 Oct 25 03:28 docker-compose.yaml
drwxr-xr-x   10 1000     1000           320 Oct 25 03:28 hello-world-app
drwxr-xr-x    3 1000     1000            96 Oct 25 03:38 jenkins-ci-java
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] sh
+ echo Finish Check code style Java using Linter '[Java+Maven],' '[PENDING]' ...
Finish Check code style Java using Linter [Java+Maven], [PENDING] ...
[Pipeline] }
$ docker stop --time=1 56d1cee3f1d21ef4b54ec9c3916dee23aef55f9a271fe65887fb73ebe4a19418
$ docker rm -f --volumes 56d1cee3f1d21ef4b54ec9c3916dee23aef55f9a271fe65887fb73ebe4a19418
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Build source code Java into JAR file)
[Pipeline] node
Running on Jenkins in /var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/PipelineCI4Java@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/04.PipelineCI4Java^{commit} # timeout=10
Checking out Revision 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 (refs/remotes/origin/04.PipelineCI4Java)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 # timeout=10
Commit message: "Add files to [04.PipelineCI4Java]"
[Pipeline] withEnv
[Pipeline] {
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker inspect -f . gradle:7-jdk11-alpine

Error: No such object: gradle:7-jdk11-alpine
[Pipeline] isUnix
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker pull gradle:7-jdk11-alpine
7-jdk11-alpine: Pulling from library/gradle
96526aa774ef: Already exists
f42efc8ffaa9: Already exists
e8889f3a7977: Already exists
4859c4719abf: Already exists
98b582a93389: Already exists
ccceb4fd3e1d: Pulling fs layer
0162162387d3: Pulling fs layer
79d321a4498e: Pulling fs layer
e5f44a66c1c8: Pulling fs layer
e5f44a66c1c8: Waiting
ccceb4fd3e1d: Verifying Checksum
ccceb4fd3e1d: Download complete
e5f44a66c1c8: Verifying Checksum
e5f44a66c1c8: Download complete
ccceb4fd3e1d: Pull complete
0162162387d3: Verifying Checksum
0162162387d3: Download complete
79d321a4498e: Verifying Checksum
79d321a4498e: Download complete
0162162387d3: Pull complete
79d321a4498e: Pull complete
e5f44a66c1c8: Pull complete
Digest: sha256:a8720b3b2c488d33c3e34584c7d0ee26b18edb709fb61385d2b5b4cdb4942edb
Status: Downloaded newer image for gradle:7-jdk11-alpine
docker.io/library/gradle:7-jdk11-alpine
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] withDockerContainer
Jenkins seems to be running inside container b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/PipelineCI4Java@2 --volumes-from b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** gradle:7-jdk11-alpine cat
$ docker top 91e96c606cbdbb7f76919f50e986d66f5df319b5c9053634f15d83e70c1d0045 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 gradle   gradle        2554 Oct 25 03:28 Jenkinsfile
-rw-r--r--    1 gradle   gradle        2222 Oct 25 03:28 README.md
-rwxr-xr-x    1 gradle   gradle         780 Oct 25 03:28 build-docker.sh
-rw-r--r--    1 gradle   gradle         656 Oct 25 03:28 docker-compose.yaml
drwxr-xr-x   10 gradle   gradle         320 Oct 25 03:28 hello-world-app
drwxr-xr-x    3 gradle   gradle          96 Oct 25 03:38 jenkins-ci-java
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] sh
+ echo Try to build the Java Project into JAR file ...
Try to build the Java Project into JAR file ...
[Pipeline] sh
+ gradle --version

Welcome to Gradle 7.6.3!

Here are the highlights of this release:
 - Added support for Java 19.
 - Introduced `--rerun` flag for individual task rerun.
 - Improved dependency block for test suites to be strongly typed.
 - Added a pluggable system for Java toolchains provisioning.

For more details see https://docs.gradle.org/7.6.3/release-notes.html


------------------------------------------------------------
Gradle 7.6.3
------------------------------------------------------------

Build time:   2023-10-04 15:59:47 UTC
Revision:     1694251d59e0d4752d547e1fd5b5020b798a7e71

Kotlin:       1.7.10
Groovy:       3.0.13
Ant:          Apache Ant(TM) version 1.10.11 compiled on July 10 2021
JVM:          11.0.20.1 (Eclipse Adoptium 11.0.20.1+1)
OS:           Linux 5.10.104-linuxkit amd64

[Pipeline] sh
+ gradle clean build --build-file ./hello-world-app/build.gradle
Starting a Gradle Daemon (subsequent builds will be faster)
> Task :clean UP-TO-DATE
> Task :compileJava
> Task :processResources
> Task :classes
> Task :bootJarMainClassName
> Task :bootJar
> Task :jar
> Task :assemble
> Task :compileTestJava
> Task :processTestResources NO-SOURCE
> Task :testClasses
> Task :test
03:45:49.105 [SpringApplicationShutdownHook] INFO  - Closing JPA EntityManagerFactory for persistence unit 'default'
03:45:49.106 [SpringApplicationShutdownHook] INFO  - HHH000477: Starting delayed evictData of schema as part of SessionFactory shut-down'
03:45:49.153 [SpringApplicationShutdownHook] INFO  - HikariPool-1 - Shutdown initiated...
03:45:49.195 [SpringApplicationShutdownHook] INFO  - HikariPool-1 - Shutdown completed.

> Task :check
> Task :build

Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

See https://docs.gradle.org/7.6.3/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 3m 45s
8 actionable tasks: 7 executed, 1 up-to-date
[Pipeline] sh
+ echo Finish build Java project using Gradle, we will have the ./hello-world-app/build/libs/hello-world-0.0.1-SNAPSHOT.jar
Finish build Java project using Gradle, we will have the ./hello-world-app/build/libs/hello-world-0.0.1-SNAPSHOT.jar
[Pipeline] sh
+ ls -l ./hello-world-app/build/libs
total 40008
-rw-r--r--    1 gradle   gradle        6436 Oct 25 03:44 hello-world-0.0.1-SNAPSHOT-plain.jar
-rw-r--r--    1 gradle   gradle    40635506 Oct 25 03:44 hello-world-0.0.1-SNAPSHOT.jar
[Pipeline] }
$ docker stop --time=1 91e96c606cbdbb7f76919f50e986d66f5df319b5c9053634f15d83e70c1d0045
$ docker rm -f --volumes 91e96c606cbdbb7f76919f50e986d66f5df319b5c9053634f15d83e70c1d0045
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
Running on Jenkins in /var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] {
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/PipelineCI4Java@2/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/04.PipelineCI4Java^{commit} # timeout=10
Checking out Revision 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 (refs/remotes/origin/04.PipelineCI4Java)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 7d3335fbf464708ad9bbcbe25e5b55196ddb1b78 # timeout=10
Commit message: "Add files to [04.PipelineCI4Java]"
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
63fa7f453749: Waiting
ce8f4a45972f: Waiting
a88060cc894f: Waiting
09e0ea80a90a: Waiting
6831f6f9e30f: Waiting
60190b130ff2: Waiting
9ce2dbd107eb: Waiting
c711618a8d74: Waiting
b1a9d1fa736b: Waiting
4f4fb700ef54: Verifying Checksum
4f4fb700ef54: Download complete
636b7876979d: Verifying Checksum
636b7876979d: Download complete
130a3e1e7df5: Download complete
c711618a8d74: Verifying Checksum
c711618a8d74: Download complete
60190b130ff2: Verifying Checksum
60190b130ff2: Download complete
636b7876979d: Pull complete
b1a9d1fa736b: Verifying Checksum
b1a9d1fa736b: Download complete
9ce2dbd107eb: Verifying Checksum
9ce2dbd107eb: Download complete
7a915d3d782d: Download complete
4f4fb700ef54: Pull complete
ce8f4a45972f: Verifying Checksum
ce8f4a45972f: Download complete
09e0ea80a90a: Verifying Checksum
09e0ea80a90a: Download complete
63fa7f453749: Verifying Checksum
63fa7f453749: Download complete
6831f6f9e30f: Verifying Checksum
6831f6f9e30f: Download complete
130a3e1e7df5: Pull complete
a88060cc894f: Verifying Checksum
a88060cc894f: Download complete
60190b130ff2: Pull complete
9ce2dbd107eb: Pull complete
c711618a8d74: Pull complete
b1a9d1fa736b: Pull complete
7a915d3d782d: Pull complete
63fa7f453749: Pull complete
ce8f4a45972f: Pull complete
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
Jenkins seems to be running inside container b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/PipelineCI4Java@2 --volumes-from b174082f0400d93cb894c43a0560084a7565d0ec9797e1a999acfd5567a7a9fe -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** docker:24.0.6-dind-alpine3.18 cat
$ docker top dd543e076e45c61df506c586fb1645fc4dd5c79243b46f2ea133f12891d005b5 -eo pid,comm
[Pipeline] {
[Pipeline] sh
+ docker --version
Docker version 24.0.6, build ed223bc
[Pipeline] sh
+ ls -l
total 16
-rw-r--r--    1 1000     1000          2554 Oct 25 03:28 Jenkinsfile
-rw-r--r--    1 1000     1000          2222 Oct 25 03:28 README.md
-rwxr-xr-x    1 1000     1000           780 Oct 25 03:28 build-docker.sh
-rw-r--r--    1 1000     1000           656 Oct 25 03:28 docker-compose.yaml
drwxr-xr-x   12 1000     1000           384 Oct 25 03:43 hello-world-app
drwxr-xr-x    3 1000     1000            96 Oct 25 03:38 jenkins-ci-java
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/PipelineCI4Java@2
[Pipeline] sh
+ cat ./jenkins-ci-java/simple-script.sh

echo 'Hello! This is the content of script in Jenkins Java Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./jenkins-ci-java/simple-script.sh
[Pipeline] sh
+ ./jenkins-ci-java/simple-script.sh
Hello! This is the content of script in Jenkins Java Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Try to list all artifacts 'in' previous steps ...
Try to list all artifacts in previous steps ...
[Pipeline] sh
+ ls -l ./hello-world-app/build/libs
total 40008
-rw-r--r--    1 1000     1000          6436 Oct 25 03:44 hello-world-0.0.1-SNAPSHOT-plain.jar
-rw-r--r--    1 1000     1000      40635506 Oct 25 03:44 hello-world-0.0.1-SNAPSHOT.jar
[Pipeline] sh
+ chmod +x ./build-docker.sh
[Pipeline] sh
+ ./build-docker.sh
Build Docker Image for Java HelloWorld: tdchien1982/jenkins:04.jenkins-ci-java-hello-world-1.0
Linux dd543e076e45 5.10.104-linuxkit #1 SMP Thu Mar 17 17:08:06 UTC 2022 x86_64 Linux
Check Docker Version
Client:
 Version:           24.0.6
 API version:       1.41 (downgraded from 1.43)
 Go version:        go1.20.7
 Git commit:        ed223bc
 Built:             Mon Sep  4 12:30:51 2023
 OS/Arch:           linux/amd64
 Context:           default

Server: Docker Desktop 4.10.1 (82475)
 Engine:
  Version:          20.10.17
  API version:      1.41 (minimum version 1.12)
  Go version:       go1.17.11
  Git commit:       a89b842
  Built:            Mon Jun  6 23:01:23 2022
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.6.6
  GitCommit:        10c12954828e7c7c9b6e0ea9b0c02b01407d3ae1
 runc:
  Version:          1.1.2
  GitCommit:        v1.1.2-0-ga916309
 docker-init:
  Version:          0.19.0
  GitCommit:        de40ad0
Remove the old image (if exists) tdchien1982/jenkins:04.jenkins-ci-java-hello-world-1.0
Error response from daemon: No such container: java-hello-world-demo
Error response from daemon: No such container: java-hello-world-demo
Error response from daemon: No such image: tdchien1982/jenkins:04.jenkins-ci-java-hello-world-1.0
Create Docker Image for Java Project: tdchien1982/jenkins:04.jenkins-ci-java-hello-world-1.0
ERROR: mkdir /.docker: permission denied
Push new image to Docker Registry
Error: Cannot perform an interactive login from a non TTY device
The push refers to repository [docker.io/tdchien1982/jenkins]
An image does not exist locally with the tag: tdchien1982/jenkins
Run App using Docker on localhost
Unable to find image 'tdchien1982/jenkins:04.jenkins-ci-java-hello-world-1.0' locally
04.jenkins-ci-java-hello-world-1.0: Pulling from tdchien1982/jenkins
f7dab3ab2d6e: Pulling fs layer
3342ddf0a1e6: Pulling fs layer
01a8d2f93afa: Pulling fs layer
4b92096cd0df: Pulling fs layer
5bddc182c95f: Pulling fs layer
4b92096cd0df: Waiting
5bddc182c95f: Waiting
f7dab3ab2d6e: Verifying Checksum
f7dab3ab2d6e: Download complete
3342ddf0a1e6: Verifying Checksum
3342ddf0a1e6: Download complete
4b92096cd0df: Verifying Checksum
4b92096cd0df: Download complete
f7dab3ab2d6e: Pull complete
01a8d2f93afa: Verifying Checksum
01a8d2f93afa: Download complete
5bddc182c95f: Verifying Checksum
5bddc182c95f: Download complete
3342ddf0a1e6: Pull complete
01a8d2f93afa: Pull complete
4b92096cd0df: Pull complete
5bddc182c95f: Pull complete
Digest: sha256:3f0f5466d73f96f4d04611d2a4681e58dceb615435bbf2e76029fe09ed989003
Status: Downloaded newer image for tdchien1982/jenkins:04.jenkins-ci-java-hello-world-1.0
f19c32b108e1b378d32911a7afa00874f76c20f0875bc134f8c0e6e1b5d70127
[Pipeline] sh
+ echo Finish Push package project to Docker Registry 'in' '[Docker' 'in' Docker] env ...
Finish Push package project to Docker Registry in [Docker in Docker] env ...
[Pipeline] sh
+ echo Finish Jenkins CI Pipeline with multiple stages ...
Finish Jenkins CI Pipeline with multiple stages ...
[Pipeline] }
$ docker stop --time=1 dd543e076e45c61df506c586fb1645fc4dd5c79243b46f2ea133f12891d005b5
$ docker rm -f --volumes dd543e076e45c61df506c586fb1645fc4dd5c79243b46f2ea133f12891d005b5
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
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```
