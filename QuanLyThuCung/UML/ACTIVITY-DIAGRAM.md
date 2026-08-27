# Activity Diagram

## 1. Bán sản phẩm tại quầy

```mermaid
flowchart TD
    A([Bắt đầu]) --> B[Chọn hoặc tạo khách hàng]
    B --> C[Quét hoặc chọn sản phẩm]
    C --> D[Kiểm tra tồn từ stock_lots]
    D --> E{Tồn đủ?}
    E -- Không --> X[Hiển thị lỗi và rollback] --> Z([Kết thúc])
    E -- Có --> F[Áp dụng promotion nếu hợp lệ]
    F --> G[Áp dụng loyalty nếu có]
    G --> H[Begin transaction]
    H --> I[Tạo orders]
    I --> J[Tạo order_items]
    J --> K[Tạo payment_transactions]
    K --> L[Trừ stock_lots]
    L --> M[Ghi stock_movements]
    M --> N[Ghi promotion_redemptions]
    N --> O[Ghi loyalty_points]
    O --> P[Ghi activity_logs]
    P --> Q{Có lỗi?}
    Q -- Có --> R[Rollback transaction] --> Z
    Q -- Không --> S[Commit transaction] --> Z
```

## 2. Nhập kho

```mermaid
flowchart TD
    A([Bắt đầu]) --> B[Chọn supplier]
    B --> C[Begin transaction]
    C --> D[Tạo purchase_receipts draft]
    D --> E[Nhập purchase_receipt_items]
    E --> F[Kiểm tra batch expiry quantity]
    F --> G{Hợp lệ?}
    G -- Không --> X[Rollback transaction]
    X --> Y[Hiển thị lỗi]
    Y --> Z([Kết thúc])
    G -- Có --> H[Confirm receipt]
    H --> I[Tạo stock_lots]
    I --> J[Tạo stock_movements]
    J --> K[Cập nhật total_amount]
    K --> L[Ghi activity_logs]
    L --> M{Có lỗi khi ghi?}
    M -- Có --> N[Rollback transaction] --> Z
    M -- Không --> O[Commit transaction] --> Z
```

## 3. Khách hàng đặt lịch chăm sóc

```mermaid
flowchart TD
    A([Bắt đầu]) --> B[Chọn pet]
    B --> C[Kiểm tra pet_owners.can_book]
    C --> D{Được đặt lịch?}
    D -- Không --> X[Thông báo từ chối] --> Z([Kết thúc])
    D -- Có --> E[Chọn service]
    E --> F[Chọn addon]
    F --> G[Tìm service_price]
    G --> H[Kiểm tra xung đột lịch]
    H --> I{Có xung đột?}
    I -- Có --> Y[Thông báo xung đột] --> Z
    I -- Không --> J[Begin transaction]
    J --> K[Tạo appointments]
    K --> L[Tạo appointment_services]
    L --> M[Tạo appointment_service_addons]
    M --> N[Tạo payment cọc nếu có]
    N --> O[Tạo appointment_histories]
    O --> P[Tạo notifications]
    P --> Q[Tạo activity_logs]
    Q --> R{Có lỗi?}
    R -- Có --> S[Rollback transaction] --> Z
    R -- Không --> T[Commit transaction] --> Z
```

## 4. Tiếp nhận và thực hiện chăm sóc

```mermaid
flowchart TD
    A([Bắt đầu]) --> B[Kiểm tra appointment]
    B --> C[Check-in]
    C --> D[Ghi pet_service_intakes]
    D --> E[Xác nhận consent]
    E --> F[Kiểm tra kỹ năng nhân viên]
    F --> G[Kiểm tra employee_schedules]
    G --> H[Kiểm tra giao ca]
    H --> I{Đủ điều kiện?}
    I -- Không --> X[Thông báo lỗi và dừng] --> Z([Kết thúc])
    I -- Có --> J[Begin transaction]
    J --> K[Tạo appointment_staff]
    K --> L[Tạo service_jobs]
    L --> M[Cập nhật appointment_services]
    M --> N[Cập nhật appointments]
    N --> O[Ghi appointment_histories]
    O --> P[Ghi notifications]
    P --> Q[Ghi activity_logs]
    Q --> R{Có lỗi?}
    R -- Có --> S[Rollback transaction] --> Z
    R -- Không --> T[Commit transaction] --> Z
```

## 5. Thanh toán và bàn giao thú

```mermaid
flowchart TD
    A([Bắt đầu]) --> B[Tổng hợp service và addon]
    B --> C[Begin transaction]
    C --> D[Tạo order]
    D --> E[Tạo order_items]
    E --> F[Trừ cọc bằng deposit_deduction item]
    F --> G[Thêm sản phẩm bán kèm nếu có]
    G --> H[Kiểm tra tồn]
    H --> I{Tồn đủ?}
    I -- Không --> X[Rollback transaction]
    X --> Y[Hiển thị lỗi]
    Y --> Z([Kết thúc])
    I -- Có --> J[Áp dụng promotion]
    J --> K[Áp dụng loyalty]
    K --> L[Tạo payment_transactions]
    L --> M[Cập nhật tồn]
    M --> N[Ghi stock_movements]
    N --> O[Cập nhật appointments completed]
    O --> P[Tạo appointment_histories]
    P --> Q[Tạo notifications]
    Q --> R[Ghi activity_logs]
    R --> S{Có lỗi?}
    S -- Có --> T[Rollback transaction] --> Z
    S -- Không --> U[Commit transaction]
    U --> V[Bàn giao thú] --> Z
```

## 6. Đổi lịch, hủy lịch và no-show

```mermaid
flowchart TD
    A([Bắt đầu]) --> B[Kiểm tra trạng thái hiện tại]
    B --> C[Kiểm tra chính sách]
    C --> D{Chọn nhánh}
    D -- Đổi lịch --> E[Kiểm tra xung đột lịch mới]
    E --> F[Cập nhật appointment]
    F --> G[Cập nhật appointment_staff nếu cần]
    D -- Hủy lịch --> H[Cập nhật appointment cancelled]
    H --> I[Refund hoặc void payment cọc nếu cần]
    D -- No-show --> J[Cập nhật appointment no_show]
    J --> K[Refund hoặc void payment cọc nếu cần]
    G --> L[Tạo appointment_histories]
    I --> L
    K --> L
    L --> M[Tạo notifications]
    M --> N[Tạo activity_logs]
    N --> O([Kết thúc])
```
