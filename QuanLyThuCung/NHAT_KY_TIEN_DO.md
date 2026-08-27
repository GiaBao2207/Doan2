# Nhật Ký Tiến Độ

## 1. Mục tiêu hiện tại

Chuẩn hóa lại toàn bộ tài liệu thiết kế theo phạm vi:

- bán sản phẩm dành cho thú cưng
- dịch vụ chăm sóc thú cưng
- lịch hẹn, tiếp nhận và thanh toán
- kho, nhập hàng, lô tồn
- khuyến mãi, điểm thưởng, đánh giá, thông báo

## 2. Kết quả hiện tại

- Loại bỏ các thành phần nghiệp vụ cũ không còn thuộc phạm vi
- Chốt mô hình một app nhiều vai trò
- Chuẩn hóa 6 quy trình chính
- Thiết kế lại CSDL thành 36 bảng logic
- Đồng bộ Room/SQLite là bản chính và MySQL là hướng mở rộng
- Đối chiếu `Thangdiem.xlsx`

## 3. Chỉ tiêu đã chốt

| Hạng mục | Số lượng |
|---|---:|
| Bảng | 36 |
| Entity | 36 |
| DAO | 36 |
| Repository | 20 |
| ViewModel | 28 |
| Màn hình Admin/Staff | 27 |
| Màn hình Customer | 21 |
| Chức năng đối tượng | 18 |
| Chức năng quy trình | 6 |
| Master-detail | 3 |
| Report | 12 |

## 4. Ghi chú

- Tài liệu đã đồng bộ theo nguyên tắc DB-first
- Không còn mô hình hai ứng dụng riêng
- Không còn nghiệp vụ bán thú cưng sống
- Không có thay đổi nào vào `Thangdiem.xlsx`
