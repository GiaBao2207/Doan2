# Danh Sách Chức Năng

## 1. Chức năng đối tượng

| # | Chức năng | Bảng nguồn | Vai trò | Màn hình chính | Thao tác |
|---|---|---|---|---|---|
| 1 | Quản lý tài khoản nội bộ | `users`, `employee_profiles` | Admin | Employee Management | CRUD, phân quyền |
| 2 | Quản lý kỹ năng nhân viên | `employee_service_skills` | Admin/Staff | Employee Management | CRUD |
| 3 | Quản lý lịch làm nhân viên | `employee_schedules` | Admin/Staff | Employee Management | CRUD |
| 4 | Quản lý khách hàng | `customers`, `customer_addresses` | Admin/Staff | Customer List/Form | CRUD, search |
| 5 | Quản lý thú cưng | `pets`, `pet_owners`, `pet_categories` | Admin/Staff/Customer | Pet List/Form, My Pets | CRUD, phân quyền xem |
| 6 | Quản lý dịch vụ | `services`, `service_prices`, `service_addons`, `service_addon_mappings` | Admin/Staff | Service List/Form, Service Price & Addon Config | CRUD |
| 7 | Quản lý sản phẩm | `products`, `product_categories` | Admin/Staff | Product List/Form | CRUD, search |
| 8 | Quản lý nhà cung cấp | `suppliers` | Admin/Staff | Supplier | CRUD |
| 9 | Quản lý phiếu nhập | `purchase_receipts`, `purchase_receipt_items` | Admin/Staff | Purchase Receipt | master-detail |
| 10 | Tra cứu lô hàng và tồn kho | `stock_lots`, `stock_movements` | Admin/Staff | Stock Movement, Inventory Adjustment | tra cứu, lọc |
| 11 | Quản lý khuyến mãi | `promotions`, `promotion_redemptions` | Admin/Staff | Promotion | CRUD, kiểm tra lượt dùng |
| 12 | Quản lý lịch hẹn | `appointments`, `appointment_services`, `appointment_service_addons`, `appointment_histories` | Admin/Staff/Customer | Appointment Calendar/Form/Detail | CRUD, đổi lịch, hủy |
| 13 | Tiếp nhận và chăm sóc | `pet_service_intakes`, `appointment_staff`, `service_jobs` | Staff | Pet Intake, Assign Staff, Service Progress | check-in, phân công, tiến độ |
| 14 | Quản lý đơn hàng | `orders`, `order_items`, `payment_transactions` | Admin/Staff/Customer | POS / Order Form, Order History | thanh toán, tra cứu |
| 15 | Quản lý điểm thưởng | `loyalty_points` | Admin/Staff/Customer | Loyalty Points | tra cứu lịch sử |
| 16 | Quản lý đánh giá | `reviews` | Admin/Staff/Customer | Review | tạo, ẩn/hiện |
| 17 | Quản lý thông báo | `notifications` | Admin/Staff/Customer | Notifications | tra cứu, đánh dấu đã đọc |
| 18 | Nhật ký hoạt động | `activity_logs` | Admin | Activity Log | tra cứu, lọc, xem chi tiết |

## 2. Chức năng quy trình

| # | Quy trình | Bảng đọc | Bảng ghi | Validation chính | Transaction |
|---|---|---|---|---|---|
| 1 | Bán sản phẩm tại quầy | `customers`, `products`, `stock_lots`, `promotions`, `promotion_redemptions`, `loyalty_points` | `orders`, `order_items`, `payment_transactions`, `stock_lots`, `stock_movements`, `promotion_redemptions`, `loyalty_points`, `activity_logs` | tồn khả dụng, khuyến mãi, điểm | Có |
| 2 | Nhập kho | `suppliers`, `products` | `purchase_receipts`, `purchase_receipt_items`, `stock_lots`, `stock_movements`, `activity_logs` | batch, hạn dùng, số lượng | Có |
| 3 | Khách hàng đặt lịch chăm sóc | `pet_owners`, `pets`, `services`, `service_prices`, `service_addon_mappings`, `service_addons`, `appointments` | `appointments`, `appointment_services`, `appointment_service_addons`, `payment_transactions`, `appointment_histories`, `notifications`, `activity_logs` | `can_book`, xung đột lịch, snapshot giá | Có |
| 4 | Tiếp nhận và thực hiện chăm sóc | `appointments`, `appointment_services`, `pets`, `employee_service_skills`, `employee_schedules`, `appointment_staff` | `pet_service_intakes`, `appointment_staff`, `service_jobs`, `appointment_services`, `appointments`, `appointment_histories`, `notifications`, `activity_logs` | consent, kỹ năng, lịch làm, giao ca | Có |
| 5 | Thanh toán và bàn giao thú | `appointments`, `appointment_services`, `appointment_service_addons`, `payment_transactions`, `products`, `promotions`, `loyalty_points` | `orders`, `order_items`, `payment_transactions`, `stock_lots`, `stock_movements`, `promotion_redemptions`, `loyalty_points`, `appointments`, `appointment_histories`, `notifications`, `activity_logs` | trừ cọc, tồn kho, refund | Có |
| 6 | Đổi lịch, hủy lịch và no-show | `appointments`, `appointment_services`, `appointment_staff`, `payment_transactions` | `appointments`, `appointment_histories`, `appointment_staff`, `payment_transactions`, `notifications`, `activity_logs` | chính sách hủy, hoàn cọc, xung đột lịch | Có |

## 3. Số lượng

- Bảng: 36
- Java Entity: 36
- DAO: 36
- Repository: 18
- ViewModel: 25
- Chức năng đối tượng: 18
- Chức năng quy trình: 6
- Màn hình Admin/Staff: 24
- Màn hình Customer: 20
- Form master-detail: 3
- Report: 12

## 4. Ánh xạ bảng -> Entity -> DAO -> Repository

| Table | Java Entity | DAO | Repository/module | Chức năng sử dụng |
|---|---|---|---|---|
| `users` | `UserEntity` | `UserDao` | `AuthRepository` | Đăng nhập, quản lý tài khoản |
| `employee_profiles` | `EmployeeProfileEntity` | `EmployeeProfileDao` | `EmployeeRepository` | Hồ sơ nhân viên |
| `employee_service_skills` | `EmployeeServiceSkillEntity` | `EmployeeServiceSkillDao` | `EmployeeRepository` | Kỹ năng nhân viên |
| `employee_schedules` | `EmployeeScheduleEntity` | `EmployeeScheduleDao` | `EmployeeRepository` | Lịch làm nhân viên |
| `customers` | `CustomerEntity` | `CustomerDao` | `CustomerRepository` | Khách hàng, profile |
| `customer_addresses` | `CustomerAddressEntity` | `CustomerAddressDao` | `CustomerRepository` | Địa chỉ giao hàng |
| `activity_logs` | `ActivityLogEntity` | `ActivityLogDao` | `ActivityLogRepository` | Nhật ký hoạt động |
| `pet_categories` | `PetCategoryEntity` | `PetCategoryDao` | `PetRepository` | Loại thú cưng |
| `pets` | `PetEntity` | `PetDao` | `PetRepository` | Hồ sơ thú cưng |
| `pet_owners` | `PetOwnerEntity` | `PetOwnerDao` | `PetRepository` | Quyền quản lý thú cưng |
| `services` | `ServiceEntity` | `ServiceDao` | `ServiceRepository` | Dịch vụ chăm sóc |
| `service_prices` | `ServicePriceEntity` | `ServicePriceDao` | `ServiceRepository` | Bảng giá |
| `service_addons` | `ServiceAddonEntity` | `ServiceAddonDao` | `ServiceRepository` | Dịch vụ bổ sung |
| `service_addon_mappings` | `ServiceAddonMappingEntity` | `ServiceAddonMappingDao` | `ServiceRepository` | Mapping addon theo dịch vụ |
| `appointments` | `AppointmentEntity` | `AppointmentDao` | `AppointmentRepository` | Đặt lịch, đổi lịch, hủy lịch |
| `appointment_services` | `AppointmentServiceEntity` | `AppointmentServiceDao` | `AppointmentRepository` | Dịch vụ trong lịch |
| `appointment_service_addons` | `AppointmentServiceAddonEntity` | `AppointmentServiceAddonDao` | `AppointmentRepository` | Addon snapshot |
| `appointment_staff` | `AppointmentStaffEntity` | `AppointmentStaffDao` | `AppointmentExecutionRepository` | Gán nhân viên |
| `appointment_histories` | `AppointmentHistoryEntity` | `AppointmentHistoryDao` | `AppointmentRepository` | Lịch sử trạng thái |
| `pet_service_intakes` | `PetServiceIntakeEntity` | `PetServiceIntakeDao` | `AppointmentExecutionRepository` | Tiếp nhận |
| `service_jobs` | `ServiceJobEntity` | `ServiceJobDao` | `AppointmentExecutionRepository` | Tiến độ chăm sóc |
| `product_categories` | `ProductCategoryEntity` | `ProductCategoryDao` | `ProductRepository` | Danh mục sản phẩm |
| `products` | `ProductEntity` | `ProductDao` | `ProductRepository` | Sản phẩm |
| `suppliers` | `SupplierEntity` | `SupplierDao` | `SupplierRepository` | Nhà cung cấp |
| `purchase_receipts` | `PurchaseReceiptEntity` | `PurchaseReceiptDao` | `InventoryRepository` | Phiếu nhập |
| `purchase_receipt_items` | `PurchaseReceiptItemEntity` | `PurchaseReceiptItemDao` | `InventoryRepository` | Dòng phiếu nhập |
| `stock_lots` | `StockLotEntity` | `StockLotDao` | `InventoryRepository` | Lô hàng |
| `stock_movements` | `StockMovementEntity` | `StockMovementDao` | `InventoryRepository` | Ledger tồn kho |
| `orders` | `OrderEntity` | `OrderDao` | `OrderRepository` | Hóa đơn |
| `order_items` | `OrderItemEntity` | `OrderItemDao` | `OrderRepository` | Dòng hóa đơn |
| `payment_transactions` | `PaymentTransactionEntity` | `PaymentTransactionDao` | `PaymentRepository` | Thanh toán, cọc, refund |
| `promotions` | `PromotionEntity` | `PromotionDao` | `PromotionRepository` | Khuyến mãi |
| `promotion_redemptions` | `PromotionRedemptionEntity` | `PromotionRedemptionDao` | `PromotionRepository` | Lịch sử dùng mã |
| `loyalty_points` | `LoyaltyPointEntity` | `LoyaltyPointDao` | `LoyaltyRepository` | Điểm thưởng |
| `reviews` | `ReviewEntity` | `ReviewDao` | `ReviewRepository` | Đánh giá |
| `notifications` | `NotificationEntity` | `NotificationDao` | `NotificationRepository` | Thông báo |

## 5. Danh sách Repository và ViewModel

### Repository

- `AuthRepository`
- `EmployeeRepository`
- `CustomerRepository`
- `PetRepository`
- `ServiceRepository`
- `AppointmentRepository`
- `AppointmentExecutionRepository`
- `ProductRepository`
- `SupplierRepository`
- `InventoryRepository`
- `OrderRepository`
- `PaymentRepository`
- `PromotionRepository`
- `LoyaltyRepository`
- `ReviewRepository`
- `NotificationRepository`
- `ActivityLogRepository`
- `ReportingRepository`

### ViewModel

- `AuthViewModel`
- `DashboardViewModel`
- `CustomerViewModel`
- `CustomerAddressViewModel`
- `PetViewModel`
- `ProductViewModel`
- `SupplierViewModel`
- `ServiceViewModel`
- `PromotionViewModel`
- `InventoryViewModel`
- `AppointmentCalendarViewModel`
- `AppointmentFormViewModel`
- `AppointmentDetailViewModel`
- `PetIntakeViewModel`
- `ServiceProgressViewModel`
- `OrderViewModel`
- `OrderHistoryViewModel`
- `NotificationViewModel`
- `LoyaltyViewModel`
- `ReviewViewModel`
- `EmployeeManagementViewModel`
- `EmployeeSkillManagementViewModel`
- `EmployeeScheduleManagementViewModel`
- `ActivityLogViewModel`
- `ReportsViewModel`

ViewModel dùng chung theo module, không tạo ViewModel riêng cho từng màn hình Customer.

- `ProductViewModel` bao gồm sản phẩm và danh mục sản phẩm.
- `ServiceViewModel` bao gồm dịch vụ, bảng giá, addon và mapping addon.
- `InventoryViewModel` bao gồm phiếu nhập, dòng phiếu nhập, lô hàng, biến động kho và điều chỉnh tồn.
- `AppointmentDetailViewModel` phụ trách chi tiết lịch, phân công nhân viên và trạng thái liên quan.
- `OrderViewModel` phụ trách POS, Cart / Checkout, tính tổng đơn và điều phối thanh toán.
- `PaymentRepository` quản lý `payment_transactions`, dù không có `PaymentViewModel` riêng.

Tổng số ViewModel: **25**

## 6. Ánh xạ màn hình Customer -> ViewModel

| Màn hình Customer | ViewModel |
|---|---|
| Login, Register, Forgot Password | `AuthViewModel` |
| Profile | `CustomerViewModel` |
| Customer Addresses | `CustomerAddressViewModel` |
| My Pets, Pet Form | `PetViewModel` |
| Service List | `ServiceViewModel` |
| Booking Wizard | `AppointmentFormViewModel` |
| Appointment History | `AppointmentCalendarViewModel` |
| Appointment Detail | `AppointmentDetailViewModel` |
| Change Appointment | `AppointmentFormViewModel` |
| Cancel Appointment | `AppointmentDetailViewModel` |
| Product Catalog | `ProductViewModel` |
| Cart / Checkout | `OrderViewModel` |
| Order History | `OrderHistoryViewModel` |
| Notifications | `NotificationViewModel` |
| Loyalty Points | `LoyaltyViewModel` |
| Review | `ReviewViewModel` |
| Customer Home | `DashboardViewModel` |

## 7. Nhóm màn hình

### Admin/Staff

- Login
- Dashboard
- Product List/Form
- Product Category
- Supplier
- Purchase Receipt
- Stock Movement
- Inventory Adjustment
- Customer List/Form
- Pet List/Form
- Service List/Form
- Service Price & Addon Config
- Appointment Calendar/Form/Detail
- Pet Intake
- Assign Staff
- Service Progress
- Ready For Pickup
- POS / Order Form
- Payment
- Order History
- Promotion
- Employee Management
- Activity Log
- Reports

`Employee Management` là một màn hình có 3 tab:

1. Hồ sơ nhân viên
2. Kỹ năng dịch vụ
3. Lịch làm việc

Các tab dùng:

- `EmployeeManagementViewModel`
- `EmployeeSkillManagementViewModel`
- `EmployeeScheduleManagementViewModel`

Không tính 3 tab thành 3 màn hình độc lập.

### Customer

- Login
- Register
- Forgot Password
- Customer Home
- Profile
- Customer Addresses
- My Pets
- Pet Form
- Service List
- Booking Wizard
- Appointment History
- Appointment Detail
- Change Appointment
- Cancel Appointment
- Product Catalog
- Cart / Checkout
- Order History
- Notifications
- Loyalty Points
- Review

Tổng số màn hình Admin/Staff: **24**
Tổng số màn hình Customer: **20**

## 8. Report suy ra từ CSDL

| # | Report | Bảng nguồn |
|---|---|---|
| 1 | Doanh thu theo ngày, tháng, khoảng ngày | `orders`, `payment_transactions` |
| 2 | Doanh thu sản phẩm | `orders`, `order_items`, `products` |
| 3 | Doanh thu dịch vụ | `orders`, `order_items`, `appointment_services` |
| 4 | Top sản phẩm bán chạy | `order_items`, `products` |
| 5 | Top dịch vụ được sử dụng | `appointment_services`, `services` |
| 6 | Lịch theo trạng thái | `appointments` |
| 7 | Tỷ lệ completed, cancelled, no_show | `appointments` |
| 8 | Hiệu suất nhân viên | `appointment_staff`, `service_jobs`, `employee_profiles` |
| 9 | Tồn kho thấp | `products`, `stock_lots` |
| 10 | Lô sắp hết hạn | `stock_lots`, `products` |
| 11 | Lịch hẹn hôm nay | `appointments` |
| 12 | Khách hàng sử dụng dịch vụ nhiều nhất | `appointments`, `appointment_services`, `customers` |

## 9. Trạng thái sơ đồ UML

| Loại sơ đồ | Trạng thái | File chứa |
|---|---|---|
| ERD | Có | `DATABASE.md` |
| State machine | Có | `DATABASE.md`, `README.md` |
| Use Case Diagram | Có | `UML/USE-CASE.md` |
| Activity Diagram | Có | `UML/ACTIVITY-DIAGRAM.md` |
| Sequence Diagram | Có | `UML/SEQUENCE-DIAGRAM.md` |
| Class Diagram | Có | `UML/CLASS-DIAGRAM.md` |
