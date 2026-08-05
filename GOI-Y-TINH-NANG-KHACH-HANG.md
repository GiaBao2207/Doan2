# Gợi Ý Tính Năng Khách Hàng

## 1. Tính năng chính

1. Đăng ký, đăng nhập, quên mật khẩu
2. Quản lý hồ sơ cá nhân
3. Quản lý địa chỉ giao hàng
4. Quản lý hồ sơ thú cưng
5. Xem dịch vụ, bảng giá và addon hợp lệ
6. Đặt lịch nhiều bước
7. Đổi lịch và hủy lịch
8. Xem sản phẩm, giỏ hàng và checkout
9. Xem lịch sử đơn hàng
10. Theo dõi điểm thưởng
11. Nhận thông báo
12. Đánh giá dịch vụ đã hoàn thành

## 2. Booking Wizard

- Chọn thú cưng mà khách có quyền đặt lịch qua `pet_owners.can_book`
- Chọn một hoặc nhiều dịch vụ
- Chọn addon theo mapping hợp lệ từ `service_addon_mappings`
- Tính giá dự kiến từ `service_prices` và `service_addons`
- Hiển thị khung giờ trống
- Hiển thị chính sách hủy
- Thu cọc nếu cần
- Tạo `appointments`, `appointment_services`, `appointment_service_addons`

### Bảng dữ liệu dùng trong Booking Wizard

- Đọc: `pets`, `pet_owners`, `services`, `service_prices`, `service_addons`, `service_addon_mappings`
- Ghi: `appointments`, `appointment_services`, `appointment_service_addons`, `payment_transactions`, `notifications`, `appointment_histories`

## 3. Validation

- Không cho đặt lịch nếu `scheduled_end <= scheduled_start`
- Không cho đặt lịch với thú cưng mà khách không có quyền `can_book = 1`
- Không cho đánh giá dịch vụ chưa hoàn thành
- Không cho hủy lịch đã hoàn thành
- Không cho đổi lịch sang khung giờ xung đột

## 4. Ánh xạ màn hình khách hàng theo CSDL

| Màn hình | Bảng đọc chính | Bảng ghi chính | Thao tác |
|---|---|---|---|
| Profile | `customers` | `customers` | cập nhật hồ sơ |
| Customer Addresses | `customer_addresses` | `customer_addresses` | CRUD |
| My Pets | `pets`, `pet_owners`, `pet_categories` | `pets`, `pet_owners` | CRUD |
| Service List | `services`, `service_prices`, `service_addons`, `service_addon_mappings` | Không ghi | tra cứu |
| Booking Wizard | `pets`, `pet_owners`, `services`, `service_prices`, `service_addons`, `service_addon_mappings` | `appointments`, `appointment_services`, `appointment_service_addons`, `payment_transactions`, `appointment_histories`, `notifications` | booking |
| Appointment History | `appointments`, `appointment_services`, `appointment_service_addons` | Không ghi | xem lịch |
| Change Appointment | `appointments`, `appointment_histories` | `appointments`, `appointment_histories`, `notifications` | đổi lịch |
| Cancel Appointment | `appointments`, `appointment_histories`, `payment_transactions` | `appointments`, `appointment_histories`, `payment_transactions`, `notifications` | hủy lịch |
| Product Catalog | `products`, `product_categories`, `stock_lots` | Không ghi | tìm kiếm |
| Checkout | `customer_addresses`, `promotions`, `loyalty_points` | `orders`, `order_items`, `payment_transactions`, `promotion_redemptions`, `loyalty_points` | transaction |
| Notifications | `notifications` | `notifications` | đánh dấu đã đọc |
| Review | `appointment_services`, `reviews` | `reviews` | đánh giá |

## 5. Ghi chú phạm vi

- Khách chỉ xem lịch sử dịch vụ khi có quyền liên quan qua `pet_owners.can_view_history`
- Không có nghiệp vụ mua hoặc bán thú cưng sống
- Không có nghiệp vụ y tế thú y chuyên sâu
