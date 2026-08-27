# Sequence Diagram

## 1. Bán sản phẩm tại quầy

```mermaid
sequenceDiagram
    actor Staff
    participant Screen as POS / Order Form
    participant VM as OrderViewModel
    participant OrderRepo as OrderRepository
    participant InventoryRepo as InventoryRepository
    participant PaymentRepo as PaymentRepository
    participant LogRepo as ActivityLogRepository
    participant DAO as DAO
    participant RoomDB as RoomDB

    Staff->>Screen: chọn khách hàng và sản phẩm
    Screen->>VM: submitOrder()
    VM->>OrderRepo: begin transaction
    OrderRepo->>InventoryRepo: checkAvailableStock()
    InventoryRepo->>DAO: query stock_lots
    DAO->>RoomDB: read
    alt tồn đủ
        OrderRepo->>DAO: insert orders + order_items
        PaymentRepo->>DAO: insert payment_transactions
        InventoryRepo->>DAO: update stock_lots + insert stock_movements
        LogRepo->>DAO: insert activity_logs
        OrderRepo->>OrderRepo: commit
        VM-->>Screen: success
    else lỗi hoặc thiếu tồn
        OrderRepo->>OrderRepo: rollback
        VM-->>Screen: failure
    end
```

## 2. Nhập kho

```mermaid
sequenceDiagram
    actor Staff
    participant Screen as Purchase Receipt
    participant VM as InventoryViewModel
    participant Repo as InventoryRepository
    participant DAO as DAO
    participant RoomDB as RoomDB

    Staff->>Screen: xác nhận phiếu nhập
    Screen->>VM: confirmReceipt()
    VM->>Repo: begin transaction
    Repo->>DAO: insert purchase_receipts
    Repo->>DAO: insert purchase_receipt_items
    Repo->>DAO: insert stock_lots
    Repo->>DAO: insert stock_movements
    alt dữ liệu hợp lệ
        Repo->>Repo: commit
        VM-->>Screen: success
    else lỗi batch expiry quantity
        Repo->>Repo: rollback
        VM-->>Screen: failure
    end
```

## 3. Khách hàng đặt lịch chăm sóc

```mermaid
sequenceDiagram
    actor Customer
    participant Screen as Booking Wizard
    participant VM as AppointmentFormViewModel
    participant Repo as AppointmentRepository
    participant PaymentRepo as PaymentRepository
    participant NotificationRepo as NotificationRepository
    participant DAO as DAO
    participant RoomDB as RoomDB

    Customer->>Screen: chọn pet service addon slot
    Screen->>VM: submitBooking()
    VM->>Repo: begin transaction
    Repo->>DAO: validate pet_owners can_book
    Repo->>DAO: check appointment conflict
    alt hợp lệ
        Repo->>DAO: insert appointments
        Repo->>DAO: insert appointment_services
        Repo->>DAO: insert appointment_service_addons
        PaymentRepo->>DAO: insert deposit payment if needed
        Repo->>DAO: insert appointment_histories
        NotificationRepo->>DAO: insert notifications
        Repo->>Repo: commit
        VM-->>Screen: success
    else lỗi validation
        Repo->>Repo: rollback
        VM-->>Screen: failure
    end
```

## 4. Tiếp nhận và thực hiện chăm sóc

```mermaid
sequenceDiagram
    actor Staff
    participant Screen as Pet Intake / Service Progress
    participant IntakeVM as PetIntakeViewModel
    participant ProgressVM as ServiceProgressViewModel
    participant Repo as AppointmentExecutionRepository
    participant LogRepo as ActivityLogRepository
    participant DAO as DAO
    participant RoomDB as RoomDB

    Staff->>Screen: check-in và cập nhật tiến độ
    Screen->>IntakeVM: submitIntake()
    IntakeVM->>Repo: begin transaction
    Repo->>DAO: validate appointment and consent
    Repo->>DAO: validate employee skill and schedule
    alt hợp lệ
        Repo->>DAO: insert pet_service_intakes
        Repo->>DAO: insert appointment_staff
        Repo->>DAO: insert service_jobs
        Repo->>DAO: update appointment_services and appointments
        Repo->>DAO: insert appointment_histories
        LogRepo->>DAO: insert activity_logs
        Repo->>Repo: commit
        ProgressVM-->>Screen: success
    else lỗi validation
        Repo->>Repo: rollback
        ProgressVM-->>Screen: failure
    end
```

## 5. Thanh toán và bàn giao thú

```mermaid
sequenceDiagram
    actor Staff
    participant Screen as POS / Order Form
    participant VM as OrderViewModel
    participant OrderRepo as OrderRepository
    participant PaymentRepo as PaymentRepository
    participant InventoryRepo as InventoryRepository
    participant NotificationRepo as NotificationRepository
    participant DAO as DAO
    participant RoomDB as RoomDB

    Staff->>Screen: xác nhận thanh toán cuối
    Screen->>VM: submitFinalPayment()
    VM->>OrderRepo: begin transaction
    OrderRepo->>DAO: insert orders and order_items
    OrderRepo->>DAO: add deposit_deduction item
    InventoryRepo->>DAO: validate stock for add-on products
    alt hợp lệ
        PaymentRepo->>DAO: insert payment_transactions
        InventoryRepo->>DAO: update stock_lots and stock_movements
        OrderRepo->>DAO: update appointments completed
        OrderRepo->>DAO: insert appointment_histories
        NotificationRepo->>DAO: insert notifications
        OrderRepo->>OrderRepo: commit
        VM-->>Screen: success
    else lỗi nghiệp vụ
        OrderRepo->>OrderRepo: rollback
        VM-->>Screen: failure
    end
```

## 6. Đổi lịch, hủy lịch và no-show

```mermaid
sequenceDiagram
    actor Customer
    participant Screen as Appointment Detail
    participant VM as AppointmentDetailViewModel
    participant Repo as AppointmentRepository
    participant PaymentRepo as PaymentRepository
    participant LogRepo as ActivityLogRepository
    participant DAO as DAO
    participant RoomDB as RoomDB

    Customer->>Screen: đổi lịch hoặc hủy lịch
    Screen->>VM: updateAppointmentStatus()
    VM->>Repo: begin transaction
    Repo->>DAO: validate current status and policy
    alt đổi lịch hợp lệ
        Repo->>DAO: update appointments
        Repo->>DAO: update appointment_staff if needed
        Repo->>DAO: insert appointment_histories
        LogRepo->>DAO: insert activity_logs
        Repo->>Repo: commit
        VM-->>Screen: success
    else hủy hoặc no_show cần refund
        PaymentRepo->>DAO: insert refund or void
        Repo->>DAO: update appointments
        Repo->>DAO: insert appointment_histories
        LogRepo->>DAO: insert activity_logs
        Repo->>Repo: commit
        VM-->>Screen: success
    else lỗi validation
        Repo->>Repo: rollback
        VM-->>Screen: failure
    end
```
