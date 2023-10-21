
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
# Ví dụ [01.HelloWorld]
==============================================================

**Tham khảo**
- https://viblo.asia/p/tim-hieu-ve-jenkins-va-cicd-eW65GbDxlDO
- https://github.com/jenkinsci/docker/blob/master/README.md
- https://octopus.com/blog/jenkins-docker-install-guide


**Cài đặt Jenkins Jenkins 2.414.3 sử dụng Docker:**<br/>
- Sử dụng Docker Image: `jenkins/jenkins:2.414.3-lts-jdk11`, dung lượng 278.66MB
- Dùng Docker Compose để khởi chạy Jenkins:
```shell
docker compose up -d
```

**Truy cập trang Jenkins UI**
```shell
http://localhost:8080
```

- User admin mặc định: 
  - User: `admin`
  - Pass: {trong file /secret/initPassword}

- Tạo user admin mới: 
  - User: `root`
  - Pass: `root1234`

- Tạo 1 project build với kịch bản như sau:
  - Checkout Github code với URL: `https://github.com/trandungchien1982/jenkins.git`
  - Sử dụng nhánh `01.HelloWorld`
  - Tiến hành execute script ./hello-world/hello-script.sh
  - Thực thi các lệnh custom sau đây : 
```shell
echo '-----------------------------------------'
echo 'This is the Custom CLI ...'
echo 'The end of HelloWorld example. See you :) '
```
**Kết quả thực thi**
<br/>(Xem Console Output của Jenkins)
```shell
Console Output
Started by user Admin
Running as SYSTEM
Building in workspace /var/jenkins_home/workspace/HelloWorld
[WS-CLEANUP] Deleting project workspace...
[WS-CLEANUP] Done
The recommended git tool is: NONE
No credentials specified
 > git rev-parse --resolve-git-dir /var/jenkins_home/workspace/HelloWorld/.git # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/trandungchien1982/jenkins.git # timeout=10
Fetching upstream changes from https://github.com/trandungchien1982/jenkins.git
 > git --version # timeout=10
 > git --version # 'git version 2.39.2'
 > git fetch --tags --force --progress -- https://github.com/trandungchien1982/jenkins.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/01.HelloWorld^{commit} # timeout=10
Checking out Revision 1bb90ad1e060032a8e8ef96c4b00daf033576eb3 (refs/remotes/origin/01.HelloWorld)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 1bb90ad1e060032a8e8ef96c4b00daf033576eb3 # timeout=10
Commit message: "Add files to [01.HelloWorld]"
 > git rev-list --no-walk 1bb90ad1e060032a8e8ef96c4b00daf033576eb3 # timeout=10
[HelloWorld] $ /bin/sh -xe /tmp/jenkins3487219651372671719.sh
+ chmod +x ./hello-world/hello-script.sh
+ ./hello-world/hello-script.sh
Hello World from Jenkins!
Welcome to the wonderful land ...
[HelloWorld] $ /bin/sh -xe /tmp/jenkins13790186331044650330.sh
+ echo -----------------------------------------
-----------------------------------------
+ echo This is the Custom CLI ...
This is the Custom CLI ...
+ echo The end of HelloWorld example. See you :) 
The end of HelloWorld example. See you :) 
Finished: SUCCESS
```
