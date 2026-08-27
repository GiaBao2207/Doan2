# Ví Dụ Luồng Người Dùng Và Use Case

## 1. Actor

- `Admin`
- `Staff`
- `Customer`

## 2. Use case tổng quát

- `Admin` quản lý nhân viên, báo cáo, `activity_logs`, danh mục và cấu hình
- `Staff` bán hàng, nhập kho, tiếp nhận và chăm sóc thú cưng, thanh toán
- `Customer` đặt lịch, mua sản phẩm, xem lịch sử, đánh giá dịch vụ

## 3. Use case theo actor

### Admin

- Quản lý user nội bộ
- Quản lý hồ sơ nhân viên
- Quản lý kỹ năng và lịch làm việc nhân viên
- Xem báo cáo
- Xem `activity_logs`
- Quản lý khuyến mãi

### Staff

- Tạo đơn hàng
- Nhập kho
- Quản lý khách hàng
- Quản lý thú cưng
- Tạo và xử lý lịch hẹn
- Gán nhân viên theo kỹ năng
- Thu tiền và hoàn tiền

### Customer

- Đăng ký, đăng nhập, quên mật khẩu
- Quản lý hồ sơ, địa chỉ, thú cưng
- Đặt lịch, đổi lịch, hủy lịch
- Mua hàng, thanh toán
- Xem điểm thưởng
- Đánh giá dịch vụ

## 4. Use case theo quy trình chính

### 4.1 Bán sản phẩm tại quầy

1. `Staff` đăng nhập
2. Chọn khách hàng hoặc khách vãng lai
3. Tìm sản phẩm theo tên, `sku` hoặc `barcode`
4. Kiểm tra tồn từ `stock_lots`
5. Tạo đơn và thanh toán
6. Trừ kho, cộng điểm, ghi log

### 4.2 Nhập kho

1. Chọn nhà cung cấp
2. Chọn sản phẩm
3. Nhập số lượng, giá vốn, lô, hạn dùng
4. Xác nhận phiếu
5. Tăng kho, ghi log

### 4.3 Đặt lịch chăm sóc

1. `Customer` chọn thú cưng qua quyền trong `pet_owners`
2. Chọn dịch vụ
3. Chọn addon hợp lệ
4. Hệ thống tính giá và thời lượng
5. Chọn khung giờ
6. Thanh toán cọc nếu cần
7. Tạo lịch và gửi thông báo

## 5. Ví dụ luồng

### 5.1 Tạo đơn hàng

1. `Staff` chọn khách hàng
2. Quét `barcode`
3. Chọn số lượng
4. Áp khuyến mãi hoặc điểm
5. Thanh toán
6. Hệ thống tạo `orders`, `order_items`, `payment_transactions`, `stock_movements`, `activity_logs` trong cùng transaction

### 5.2 Đặt lịch

1. `Customer` chọn thú cưng
2. Chọn một hoặc nhiều dịch vụ
3. Chọn addon
4. Chọn khung giờ
5. Thanh toán cọc nếu có
6. Hệ thống tạo `appointments`, `appointment_services`, `appointment_service_addons`, `appointment_histories`

### 5.3 Check-in và thực hiện dịch vụ

1. `Staff` mở lịch hẹn
2. Tạo `pet_service_intakes`
3. Kiểm tra kỹ năng và ca làm nhân viên
4. Gán nhân viên
5. Cập nhật `service_jobs`
6. Chuyển trạng thái `ready_for_pickup`

### 5.4 Thanh toán

1. `Staff` tổng hợp dịch vụ và sản phẩm
2. Trừ cọc
3. Thu tiền
4. Cập nhật lịch `completed`
5. Gửi yêu cầu đánh giá

## 6. Validation

- Không cho trùng lịch
- Không cho `amount` âm
- Không cho review dịch vụ chưa hoàn thành
- Không cho `scheduled_end <= scheduled_start`
- Không cho đặt lịch nếu không có quyền `can_book`

## 7. Trạng thái yêu cầu

- `appointments.status`: `pending_confirmation`, `confirmed`, `arrived`, `checked_in`, `in_service`, `ready_for_pickup`, `completed`, `cancelled`, `no_show`
- `appointment_services.status`: `pending`, `assigned`, `in_progress`, `paused`, `completed`, `cancelled`
- `orders.status`: `draft`, `confirmed`, `completed`, `cancelled`
- `payment_transactions.transaction_type`: `payment`, `refund`, `void`
- `payment_transactions.transaction_status`: `pending`, `success`, `failed`, `cancelled`
