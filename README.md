# Customer-Data-Platform

Hướng dẫn sử dụng backend:
+ B1: Clone Repo
+ B2: Cài đặt mysql trên máy, 
+ B3: Tạo database tên: `customer-data-platform`
+ B4: Tạo user cấp quyền truy cập cho data base trên 
+ B5: vào file có đường dẫn src/main/resources/application.properties, chỉnh sửa user, password vừa tạo
+ B6: Dòng 23 file trên, khởi chạy lần đầu nên bỏ comment, nó sẽ tự tạo các bảng như phần Entity đã cấu hình, 
lưu ý: sau khi chạy lần đầu nên comment lại hoặc đổi create thành validate,
nếu không, nó sẽ tạo lại bảng và xóa hết dữ liệu
+ B7: chạy test thử vài api
