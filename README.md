
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