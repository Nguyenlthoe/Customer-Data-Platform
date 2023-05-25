# Customer-Data-Platform

## Hướng dẫn sử dụng backend, cd vào module book-shop nhé ae:
+ B1: Clone Repo
+ B2: Cài đặt mysql trên máy, 
+ B3: Tạo database tên: `customer-data-platform`
+ B4: Tạo user cấp quyền truy cập cho data base trên 
+ B5: vào file có đường dẫn src/main/resources/application.properties, chỉnh sửa user, password vừa tạo
+ B6: Dòng 23 file trên, khởi chạy lần đầu nên bỏ comment, nó sẽ tự tạo các bảng như phần Entity đã cấu hình, 
lưu ý: sau khi chạy lần đầu nên comment lại hoặc đổi create thành validate,
nếu không, nó sẽ tạo lại bảng và xóa hết dữ liệu
+ B6: Ch
+ B7: chạy test thử vài api trên swagger: http://localhost:8180/swagger-ui.html#

## Hướng dẫn cài đặt phần segment:

+ Khởi chạy cụm spark-yarn docker và mysql docker (ae thử thay thể mysql ở local xem sao, thử đổi địa chỉ mysql trong application.properties là 172.25.0.1 nhé, class MySqlUtils nữa)
+ Start cụm spark-yarn đọc ở readme.md trong thư mục dojcker/spark-yarn
+ API service phần customer-platform có port 8280
+ Đóng gói class: `cd customer-data-platform` sau đó ` mvn clean compile assembly:single` ta sẽ có được file jar ở thư mục target 
+ Đưa lên container docker hadoop-master:/ `docker cp linktojar hadoop-master:/`
+ Tạo tài khoản admin(insert vào bảng cdp_admin nhé)
+ Sinh ngẫu nhiên các tập người dùng: Chạy class VietnamseNameGenerator(Đang để 1000 thì phải)
+ Tạo segment và chạy thử: api post segment, vô swagger coi thử nhé `(birthday ứng với datetime, province_code ứng với integer, gender ứng với interger)` Các toán tử trong class `config/ConditionConfig`
+ Chạy tất cả segment với tất cả user: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser customer-platform-2.2.7.RELEASE-jar-with-dependencies.jar all`
+ Chạy những segment tạo mới, hoặc có sự cập nhật: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser customer-platform-2.2.7.RELEASE-jar-with-dependencies.jar new`
+ Chạy những user mới cập nhật: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser customer-platform-2.2.7.RELEASE-jar-with-dependencies.jar update`