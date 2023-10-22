
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
# Ví dụ [02.SimplePipeline]
==============================================================

**Tham khảo**
- https://www.jenkins.io/doc/pipeline/tour/hello-world/
- https://www.jenkins.io/doc/book/pipeline/getting-started/
- https://devopsify.co/series-jenkin-tim-hieu-ve-jenkins-pipeline/
- https://tel4vn.edu.vn/khai-niem-ve-jenkins-pipeline-cho-nguoi-moi-bat-dau/
- https://cuongquach.com/jenkins-pipeline-la-gi-tong-quan-jenkins-pipeline-ci-cd.html

**Tạo một Pipeline đơn giản sử dụng Docker dành cho môi trường Java+Maven:**
- Pipeline được mô tả trong file `Jenkinsfile`, sử dụng `Declarative Pipeline`
- Yêu cầu phải install Docker Pipeline plugin trước:
<br/>https://plugins.jenkins.io/docker-workflow/
<br/>From your Jenkins dashboard navigate to Manage Jenkins > Manage Plugins and select the Available tab. Locate this plugin by searching for docker-workflow.

**Config để khi execute Jenkins Image thì ta có thể gọi được lệnh `docker` (kiểu như Docker in Docker):** 
<br/>Ta sẽ config thêm trong file `docker-compose.yaml` 
<br/>(Ở đây đang áp dụng cho môi trường Ubuntu 20.04 LTS)
```shell
    volumes:
        - ./jenkins_home:/var/jenkins_home
        - /var/run/docker.sock:/var/run/docker.sock
        - /usr/bin/docker:/usr/bin/docker
```

**Xử lý lỗi `Permission Denied` khi gọi `docker` trong container của Jenkins Server**
<br/>Fix theo hướng dẫn dưới đây :
<br/>https://www.digitalocean.com/community/questions/how-to-fix-docker-got-permission-denied-while-trying-to-connect-to-the-docker-daemon-socket
  - Đăng nhập dưới quyền `root`, sử dụng Portainer UI vào trong container của Jenkins CI và execute CLIs:
```shell
sudo chmod 666 /var/run/docker.sock
sudo groupadd docker
sudo usermod -aG docker jenkins
```
  - Đăng nhập bình thường với user mặc định (`jenkins`) và kiểm tra thử :
```shell
docker ps
```

**Nội dung file : `Jenkinsfile`:**
<br/>(Nằm ở folder gốc của Git/ branch mà ta đang refer tới trong kịch bản Pipeline)
```shell
/* Requires the Docker Pipeline plugin */
pipeline {
    agent { docker { image 'maven:3.9.5-eclipse-temurin-11-alpine' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
                sh 'ls -l'
                sh 'pwd'
                sh 'cat ./simple-pipeline/simple-script.sh'
                sh 'chmod +x ./simple-pipeline/simple-script.sh'
                sh './simple-pipeline/simple-script.sh'
                sh 'echo Finish CI ...'
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
Running on Jenkins in /var/jenkins_home/workspace/Simple-Pipeline
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Declarative: Checkout SCM)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/Simple-Pipeline/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/02.SimplePipeline^{commit} # timeout=10
Checking out Revision 3ab7233b7f15eddd553220c867db90d6c3e43949 (refs/remotes/origin/02.SimplePipeline)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 3ab7233b7f15eddd553220c867db90d6c3e43949 # timeout=10
Commit message: "Add files to [02.SimplePipeline]"
 > git rev-list --no-walk 4c4ab9ecbff4642d3973bb4d80892a9a0b090d4f # timeout=10
[Pipeline] }
[Pipeline] // stage
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
$ docker run -t -d -u 1000:1000 -w /var/jenkins_home/workspace/Simple-Pipeline --volumes-from bd59f1be09d66954b988700fc29b2a4a5da7ba89f79c8f9d5dc316362cf0534f -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** -e ******** maven:3.9.5-eclipse-temurin-11-alpine cat
$ docker top 0d727789e28c4f24f1da504eba9575b0c4427344a63a7e664b2a1c717c195f7c -eo pid,comm
[Pipeline] {
[Pipeline] stage
[Pipeline] { (build)
[Pipeline] sh
+ mvn --version
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: /usr/share/maven
Java version: 11.0.20.1, vendor: Eclipse Adoptium, runtime: /opt/java/openjdk
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.15.0-87-generic", arch: "amd64", family: "unix"
[Pipeline] sh
+ ls -l
total 20
-rw-r--r--    1 1000     1000           521 Oct 22 15:17 Jenkinsfile
-rw-r--r--    1 1000     1000          6353 Oct 22 15:04 README.md
-rw-r--r--    1 1000     1000           656 Oct 22 14:53 docker-compose.yaml
drwxr-xr-x    2 1000     1000          4096 Oct 22 04:05 simple-pipeline
[Pipeline] sh
+ pwd
/var/jenkins_home/workspace/Simple-Pipeline
[Pipeline] sh
+ cat ./simple-pipeline/simple-script.sh

echo 'Hello! This is the content of script in Jenkins Pipeline'
echo 'Welcome to the CI/CD world ...'
[Pipeline] sh
+ chmod +x ./simple-pipeline/simple-script.sh
[Pipeline] sh
+ ./simple-pipeline/simple-script.sh
Hello! This is the content of script in Jenkins Pipeline
Welcome to the CI/CD world ...
[Pipeline] sh
+ echo Finish CI ...
Finish CI ...
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
$ docker stop --time=1 0d727789e28c4f24f1da504eba9575b0c4427344a63a7e664b2a1c717c195f7c
$ docker rm -f --volumes 0d727789e28c4f24f1da504eba9575b0c4427344a63a7e664b2a1c717c195f7c
[Pipeline] // withDockerContainer
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```
