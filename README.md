# Customer-Data-Platform

## Hướng dẫn cài đặt
Các cài đặt sử dụng docker
+ Sử dụng docker. Các file cài đặt docker được build sẵn ở thư mục docker.
+ Có thể thực hiện cài đặt cấu hình địa chỉ các máy chủ ở các file theo từng module. 
+ bk.edu.config.Config cho các địa chỉ Kafka, Elastic Search, Airflow
+ resources/application.properties cấu hình địa chỉ cơ sở dữ liệu MySQL
+ constants/api_config cấu hình địa chỉ máy chủ backend module cdp_frontend
+ Cài đặt cụm hadoop-spark 
+ Cài đặt airflow kết nối với cụm Hadoop-Spark sử dụng kết nối ssh.
+ Các file Dag có sẵn ở phần docker
+ Tạo trigger cập nhật trường updated_time cho bảng dữ liệu người dùng.
+ Cài đặt module thu thập dữ liệu gồm snowplow, website tracking sự kiện người dùng.
```
use `customer-data-platform`;
DELIMITER $$
CREATE TRIGGER set_updated_time
BEFORE UPDATE ON bookshop_customer
FOR EACH ROW 
BEGIN SET new.updated_at = now();
 END $$
DELIMITER ;
```
## Hướng dẫn cài đặt phần segment:

+ Đóng gói mã nguồn: `cd cdp-job` sau đó ` mvn clean compile assembly:single` ta sẽ có được file jar ở thư mục target 
+ Đưa lên container docker hadoop-master:/ `docker cp linktojar hadoop-master:/`
+ Sinh ngẫu nhiên các tập người dùng: Chạy class VietnamseNameGenerator ở phần cdp-job(Có thể sửa số lượng user muốn sinh)
+ Tạo segment sử dụng website cdp ở port 3100.
+ Chạy tất cả segment với tất cả user: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser cdp-job-1.0-SNAPSHOT-jar-with-dependencies.jar all false`
+ Chạy những segment tạo mới, hoặc có sự cập nhật: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser cdp-job-1.0-SNAPSHOT-jar-with-dependencies.jar new false`
+ Chạy những user mới cập nhật(chạy liên tục không dừng, có thể chỉ định deploy-mode là cluster): `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser cdp-job-1.0-SNAPSHOT-jar-with-dependencies.jar update false`
+ Cập nhật sở thích ngắn hạn: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.UpdateShortHobbies cdp-job-1.0-SNAPSHOT-jar-with-dependencies.jar new false`
+ Cập nhật sở thích dài hạn: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.UpdateLongHobbies cdp-job-1.0-SNAPSHOT-jar-with-dependencies.jar false`
+ Chạy chương trình streaming xử lý sở thích ngắn hạn: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser cdp-job-1.0-SNAPSHOT-jar-with-dependencies.jar false hobby HandleUpdateShortHobbies`
+ Chạy chương trình streaming xử lý sự kiện xem sách: `spark-submit --master yarn --deploy-mode client --class bk.edu.job.SegmentAllUser cdp-job-1.0-SNAPSHOT-jar-with-dependencies.jar false view HandleUpdateView`

