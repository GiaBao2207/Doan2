# Implementation Notes

## 1. Trạng thái repository

Repository hiện tại chỉ là **tài liệu thiết kế**. Chưa có project Android Studio, chưa có source code Java, XML, Gradle hoặc cấu hình build trong phạm vi hiện tại.

## 2. Nguyên tắc thiết kế đã chốt

- Thiết kế theo **DB-first**
- Room/SQLite là bản chính của đồ án Android
- MySQL chỉ là phương án mở rộng trong tương lai, không dùng trực tiếp từ Android
- Một app nhiều vai trò: `Admin`, `Staff`, `Customer`
- Tiền dùng `INTEGER` theo đơn vị VNĐ
- Datetime dùng `TEXT` chuẩn ISO-8601 UTC
- Tên bảng và cột dùng tiếng Anh không dấu, `snake_case`

## 3. Quyết định triển khai đã chốt

- `AuthViewModel` dùng chung cho `Admin`, `Staff`, `Customer`
- `OrderViewModel` dùng chung cho POS và Checkout
- ViewModel tổ chức theo module, không theo từng màn hình
- `ProductViewModel` gộp sản phẩm và danh mục sản phẩm
- `ServiceViewModel` gộp dịch vụ, bảng giá, addon và mapping
- `InventoryViewModel` gộp phiếu nhập, lô, biến động và điều chỉnh kho
- `AppointmentDetailViewModel` phụ trách chi tiết lịch và phân công nhân viên
- `order_items.product_id` và `order_items.appointment_service_id` dùng RESTRICT để giữ tham chiếu nghiệp vụ
- `payment_transactions.order_id` và `payment_transactions.appointment_id` dùng RESTRICT để giữ transaction theo đúng đối tượng
- `promotions.target_product_category_id` và `promotions.target_service_id` dùng RESTRICT để giữ target hợp lệ
- `Employee Management` là một màn hình có 3 tab
- `Activity Log` có màn hình riêng, dùng `ActivityLogRepository` và `ActivityLogViewModel`
- Tiền cọc chỉ lưu trong `order_items.item_type = 'deposit_deduction'`
- Phí giao hàng chỉ lưu trong `orders.shipping_fee`
- Refund và void phải tham chiếu payment gốc qua `original_payment_id`
- `appointment_histories` có `changed_by_type`
- `activity_logs` có đúng một actor và FK actor dùng RESTRICT
- `notifications` có CHECK giữa `reference_type` và `reference_id`
- `notifications.reference_type` là NOT NULL DEFAULT `none`
- Notification hiện là dữ liệu cục bộ Room
- Link trong `README.md` phải là link tương đối
- Chỉ đánh dấu sơ đồ UML là `Có` khi file thực sự tồn tại trong repo
- Use Case dùng Mermaid flowchart
- Activity có 6 sơ đồ tương ứng 6 quy trình
- Sequence có 6 sơ đồ tương ứng 6 quy trình
- Class Diagram có 2 sơ đồ: kiến trúc MVVM và domain/database
- Bộ UML bám theo Room/SQLite và cấu trúc module-based
- Không mô tả backend là kiến trúc hiện tại

## 4. Chỉ tiêu tài liệu

| Hạng mục | Số lượng |
|---|---:|
| Bảng | 36 |
| Java Entity | 36 |
| DAO | 36 |
| Repository | 18 |
| ViewModel | 25 |
| Màn hình Admin/Staff | 24 |
| Màn hình Customer | 20 |
| Chức năng đối tượng | 18 |
| Chức năng quy trình | 6 |
| Form master-detail | 3 |
| Report chính | 12 |

## 5. Trạng thái UML thực tế

| Loại tài liệu | Trạng thái | File |
|---|---|---|
| ERD | Có | `DATABASE.md` |
| State machine | Có | `DATABASE.md`, `README.md` |
| Use Case Diagram | Có | `UML/USE-CASE.md` |
| Activity Diagram | Có | `UML/ACTIVITY-DIAGRAM.md` |
| Sequence Diagram | Có | `UML/SEQUENCE-DIAGRAM.md` |
| Class Diagram | Có | `UML/CLASS-DIAGRAM.md` |

## 6. Ghi chú đồng bộ

- `pet_owners` là nguồn sở hữu thú cưng duy nhất
- `products` không có `quantity`
- `stock_lots` là nguồn số dư tồn kho
- `stock_movements` là ledger biến động kho
- Có `employee_service_skills`
- Có `employee_schedules`
- Có `service_addon_mappings`
- Giữ nguyên 6 quy trình nghiệp vụ
- Giữ nguyên 3 form master-detail
- Giữ nguyên 12 report chính
- Không sửa `Thangdiem.xlsx`
