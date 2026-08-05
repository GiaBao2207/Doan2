# Luồng Thực Tế

## 1. Danh sách luồng chính

1. Bán sản phẩm tại quầy
2. Nhập kho
3. Khách hàng đặt lịch chăm sóc
4. Tiếp nhận và thực hiện chăm sóc
5. Thanh toán và bàn giao thú
6. Đổi lịch, hủy lịch và no-show

## 2. Bán sản phẩm tại quầy

1. Nhân viên đăng nhập
2. Chọn khách hàng hoặc khách vãng lai
3. Quét `barcode` hoặc tìm sản phẩm
4. Chọn số lượng
5. Kiểm tra tồn khả dụng từ `stock_lots`
6. Áp mã khuyến mãi hoặc điểm thưởng
7. Chọn phương thức thanh toán
8. Tạo `orders`, `order_items`, `payment_transactions`
9. Trừ tồn theo lô bằng `stock_movements`
10. Cộng hoặc trừ `loyalty_points`, ghi `activity_logs`

Thành phần dữ liệu dùng trong quy trình:

- `orders`
- `order_items`
- `payment_transactions`
- `stock_lots`
- `stock_movements`
- `loyalty_points`
- `activity_logs`

## 3. Nhập kho

1. Chọn nhà cung cấp
2. Chọn sản phẩm
3. Nhập số lượng, giá vốn, mã lô, hạn sử dụng
4. Tạo `purchase_receipts`
5. Tạo `purchase_receipt_items`
6. Tạo `stock_lots`
7. Ghi tăng kho bằng `stock_movements`
8. Ghi `activity_logs`

Thành phần dữ liệu dùng trong quy trình:

- `purchase_receipts`
- `purchase_receipt_items`
- `stock_lots`
- `stock_movements`
- `activity_logs`

## 4. Khách hàng đặt lịch chăm sóc

1. Khách đăng nhập
2. Chọn thú cưng có quyền đặt lịch qua `pet_owners`
3. Chọn một hoặc nhiều dịch vụ
4. Chọn addon hợp lệ từ `service_addon_mappings`
5. Tính giá và thời lượng dự kiến từ `service_prices`
6. Chọn khung giờ còn trống
7. Nhập yêu cầu đặc biệt
8. Thanh toán cọc nếu cần
9. Tạo `appointments`, `appointment_services`, `appointment_service_addons`
10. Ghi `appointment_histories` và `notifications`

Thành phần dữ liệu dùng trong quy trình:

- `pet_owners`
- `appointments`
- `appointment_services`
- `appointment_service_addons`
- `appointment_histories`
- `payment_transactions`
- `notifications`

## 5. Tiếp nhận và thực hiện chăm sóc

1. Tìm lịch hoặc tạo lịch walk-in hợp lệ
2. Xác nhận khách và thú cưng
3. Cân thú, ghi tình trạng ban đầu
4. Tạo `pet_service_intakes`
5. Kiểm tra nhân viên có kỹ năng phù hợp qua `employee_service_skills`
6. Kiểm tra ca làm qua `employee_schedules`
7. Gán nhân viên vào `appointment_staff`
8. Cập nhật tiến độ trong `service_jobs`
9. Cập nhật trạng thái lịch và ghi `appointment_histories`
10. Khi hoàn tất thì chuyển `ready_for_pickup` và gửi `notifications`

Thành phần dữ liệu dùng trong quy trình:

- `appointments`
- `appointment_services`
- `appointment_staff`
- `appointment_histories`
- `pet_service_intakes`
- `service_jobs`
- `employee_service_skills`
- `employee_schedules`
- `notifications`

## 6. Thanh toán và bàn giao thú

1. Tổng hợp dịch vụ đã thực hiện
2. Thêm sản phẩm bán kèm nếu có
3. Thêm phụ thu hợp lệ nếu có
4. Khấu trừ tiền cọc
5. Áp khuyến mãi hoặc điểm
6. Tạo hóa đơn và dòng hóa đơn
7. Thu tiền hoặc hoàn tiền chênh lệch
8. Trừ tồn sản phẩm bán thêm theo `stock_movements`
9. Cập nhật lịch `completed`
10. Ghi `promotion_redemptions`, `loyalty_points`, `activity_logs`, `notifications`

Thành phần dữ liệu dùng trong quy trình:

- `orders`
- `order_items`
- `payment_transactions`
- `stock_movements`
- `promotions`
- `promotion_redemptions`
- `loyalty_points`
- `appointments`
- `notifications`
- `activity_logs`

## 7. Đổi lịch, hủy lịch và no-show

1. Xác định người thao tác
2. Kiểm tra trạng thái hiện tại
3. Kiểm tra chính sách đổi hoặc hủy
4. Tính hoàn cọc hoặc phí hủy
5. Tạo `payment_transactions` loại `refund` nếu cần
6. Cập nhật `appointments`
7. Ghi `appointment_histories`
8. Gửi `notifications`
9. Ghi `activity_logs`

Thành phần dữ liệu dùng trong quy trình:

- `appointments`
- `appointment_histories`
- `payment_transactions`
- `notifications`
- `activity_logs`

## 8. Ghi chú đồng bộ

- Luồng nghiệp vụ chỉ sử dụng các bảng đã có trong `DATABASE.md`
- Không thêm quy trình ngoài 6 luồng chính
- Các thao tác màn hình phải được suy ra từ CSDL, không thiết kế ngược từ UI
