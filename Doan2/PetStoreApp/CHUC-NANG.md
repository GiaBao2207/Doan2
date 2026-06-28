# Danh Sách Chức Năng (Phân Lớp Theo MVVM)

> **Quy ước viết tắt:**
> - ĐT = Chức năng đối tượng (Object function – CRUD một thực thể)
> - QT = Chức năng quy trình (Process function – tác vụ tổng hợp nhiều bước)

---

## 1. MODEL – Định nghĩa đối tượng dữ liệu (Entity / POJO)

| # | Class | Vai trò | Loại |
|---|---|---|---|
| 1 | `User.java` | Người dùng (Admin, Nhân viên) | ĐT |
| 2 | `Pet.java` | Thú cưng (chó, mèo,...) | ĐT |
| 3 | `PetCategory.java` | Loại thú cưng | ĐT |
| 4 | `Customer.java` | Khách hàng (có đăng nhập, lịch sử, tích điểm) | ĐT |
| 5 | `Product.java` | Sản phẩm (thức ăn, phụ kiện, thuốc) | ĐT |
| 6 | `ProductCategory.java` | Loại sản phẩm | ĐT |
| 7 | `Supplier.java` | Nhà cung cấp | ĐT |
| 8 | `Order.java` | Đơn hàng / Hóa đơn bán | ĐT |
| 9 | `OrderDetail.java` | Chi tiết đơn hàng | ĐT |
| 10 | `Appointment.java` | Lịch hẹn (khám, spa, grooming) | ĐT |
| 11 | `Inventory.java` | Phiếu nhập kho | ĐT |
| 12 | `InventoryDetail.java` | Chi tiết phiếu nhập | ĐT |
| 13 | `Service.java` | Dịch vụ (tắm, cắt tỉa, khám) | ĐT |
| 14 | `Promotion.java` | Khuyến mãi / Giảm giá | ĐT |
| 15 | **`Review.java`** | **Đánh giá dịch vụ của khách hàng** | ĐT |
| 16 | **`LoyaltyPoint.java`** | **Tích điểm / Hạng thành viên** | ĐT |
| 17 | **`Cart.java`** | **Giỏ hàng online của khách** | ĐT |
| 18 | **`FavoritePet.java`** | **Thú cưng yêu thích / Theo dõi** | ĐT |
| 19 | **`CustomerNotification.java`** | **Thông báo cho khách hàng** | ĐT |
| 20 | **`PetHealthRecord.java`** | **Hồ sơ sức khỏe / Lịch tiêm chủng** | ĐT |
| 21 | **`Payment.java`** | **Giao dịch thanh toán cho đơn hàng** | ĐT |
| 22 | **`ActivityLog.java`** | **Nhật ký hoạt động người dùng** | ĐT |
| 23 | **`AppointmentStaff.java`** | **Nhân viên phục vụ lịch hẹn** | ĐT |

**Tổng: 23 chức năng đối tượng** ✅ *(yêu cầu ≥10 ĐT)*

---

## 2. DATA – Tầng dữ liệu

### 2.1 Database (Room)

| # | DAO | Chức năng |
|---|---|---|
| 1 | `UserDao.java` | Thêm, sửa, xóa, tìm user; đăng nhập |
| 2 | `PetDao.java` | CRUD thú cưng, tìm theo tên/loại/chủ |
| 3 | `PetCategoryDao.java` | CRUD loại thú cưng |
| 4 | `CustomerDao.java` | CRUD khách hàng, tìm theo SĐT/tên, **đăng nhập khách** |
| 5 | `ProductDao.java` | CRUD sản phẩm, kiểm tra tồn kho |
| 6 | `ProductCategoryDao.java` | CRUD loại sản phẩm |
| 7 | `SupplierDao.java` | CRUD nhà cung cấp |
| 8 | `OrderDao.java` | Tạo đơn, lịch sử đơn, doanh thu |
| 9 | `OrderDetailDao.java` | CRUD chi tiết đơn hàng |
| 10 | `AppointmentDao.java` | CRUD lịch hẹn, lọc theo ngày |
| 11 | `InventoryDao.java` | CRUD phiếu nhập kho |
| 12 | `InventoryDetailDao.java` | CRUD chi tiết phiếu nhập |
| 13 | `ServiceDao.java` | CRUD dịch vụ |
| 14 | `PromotionDao.java` | CRUD khuyến mãi |
| 15 | **`ReviewDao.java`** | CRUD đánh giá, lọc theo dịch vụ/khách hàng |
| 16 | **`LoyaltyPointDao.java`** | Truy vấn điểm, hạng, lịch sử tích điểm |
| 17 | **`CartDao.java`** | CRUD giỏ hàng theo khách |
| 18 | **`FavoritePetDao.java`** | CRUD thú cưng yêu thích |
| 19 | **`CustomerNotificationDao.java`** | CRUD thông báo, đánh dấu đã đọc |
| 20 | **`PetHealthRecordDao.java`** | CRUD hồ sơ sức khỏe, lọc theo thú cưng |
| 21 | **`PaymentDao.java`** | CRUD giao dịch thanh toán, truy vấn theo đơn hàng |
| 22 | **`ActivityLogDao.java`** | INSERT log (append-only), truy vấn lọc theo ngày/người dùng |
| 23 | **`AppointmentStaffDao.java`** | CRUD nhân viên phục vụ, lọc theo lịch hẹn/nhân viên |

### 2.2 Repository

| # | Repository | Data Sources |
|---|---|---|
| 1 | `UserRepository.java` | UserDao |
| 2 | `PetRepository.java` | PetDao, PetCategoryDao |
| 3 | `CustomerRepository.java` | CustomerDao |
| 4 | `ProductRepository.java` | ProductDao, ProductCategoryDao |
| 5 | `OrderRepository.java` | OrderDao, OrderDetailDao, PaymentDao (QT) |
| 6 | `AppointmentRepository.java` | AppointmentDao, AppointmentStaffDao (QT) |
| 7 | `InventoryRepository.java` | InventoryDao, InventoryDetailDao (QT) |
| 8 | `ServiceRepository.java` | ServiceDao |
| 9 | `PromotionRepository.java` | PromotionDao |
| 10 | **`ReviewRepository.java`** | ReviewDao |
| 11 | **`LoyaltyPointRepository.java`** | LoyaltyPointDao |
| 12 | **`CartRepository.java`** | CartDao |
| 13 | **`FavoritePetRepository.java`** | FavoritePetDao |
| 14 | **`CustomerNotificationRepository.java`** | CustomerNotificationDao |
| 15 | **`PetHealthRecordRepository.java`** | PetHealthRecordDao |
| 16 | **`PaymentRepository.java`** | PaymentDao |
| 17 | **`ActivityLogRepository.java`** | ActivityLogDao |
| 18 | **`AppointmentStaffRepository.java`** | AppointmentStaffDao |

---

## 3. VIEWMODEL – Tầng trung gian

| # | ViewModel | Xử lý |
|---|---|---|
| 1 | `LoginViewModel.java` | Xác thực đăng nhập **Staff/Admin** |
| 2 | **`CustomerLoginViewModel.java`** | **Xác thực đăng nhập + đăng ký Khách hàng** |
| 2 | `DashboardViewModel.java` | Thống kê tổng quan (doanh thu, số lượng, lịch hẹn hôm nay) |
| 3 | `PetViewModel.java` | Tìm kiếm, lọc, CRUD thú cưng |
| 4 | `CustomerViewModel.java` | Tìm kiếm, CRUD khách hàng |
| 5 | `ProductViewModel.java` | CRUD sản phẩm, lọc theo danh mục |
| 6 | **`OrderViewModel.java`** | **Tạo đơn hàng (QT) + Lịch sử giao dịch** |
| 7 | `AppointmentViewModel.java` | CRUD lịch hẹn + nhắc hẹn |
| 8 | `InventoryViewModel.java` | **Nhập kho (QT) + kiểm tra tồn kho** |
| 9 | `ServiceViewModel.java` | CRUD dịch vụ |
| 10 | `PromotionViewModel.java` | CRUD khuyến mãi, áp mã giảm giá |
| 11 | `StatisticViewModel.java` | Thống kê doanh thu theo ngày/tháng/năm (Report) |
| 12 | **`CustomerStatisticViewModel.java`** | **Thống kê cá nhân: số lần sử dụng dịch vụ, chi tiêu, lịch sử** |
| 13 | **`ReviewViewModel.java`** | Tạo/xem đánh giá dịch vụ |
| 14 | **`CartViewModel.java`** | Quản lý giỏ hàng online |
| 15 | **`FavoritePetViewModel.java`** | Thêm/xóa thú cưng yêu thích |
| 16 | **`CustomerNotificationViewModel.java`** | Xem thông báo, đánh dấu đã đọc |
| 17 | **`PetHealthViewModel.java`** | Xem hồ sơ sức khỏe, lịch tiêm |
| 18 | **`PaymentViewModel.java`** | **Xử lý thanh toán, ghi giao dịch, hoàn tiền** |
| 19 | **`ActivityLogViewModel.java`** | **Xem nhật ký hoạt động (Admin)** |
| 20 | **`AppointmentStaffViewModel.java`** | **Gán nhân viên phục vụ, cập nhật trạng thái** |

---

## 4. UI – Tầng giao diện

| # | Màn hình | Activity / Fragment | Chức năng |
|---|---|---|---|
| 1 | Đăng nhập | `LoginActivity.java` | Đăng nhập, phân quyền |
| 2 | Trang chủ | `DashboardActivity.java` | Tổng quan: doanh thu, số lượng, lịch hẹn |
| 3 | Quản lý thú cưng | `PetListFragment.java` + `PetFormActivity.java` | Danh sách + CRUD thú cưng |
| 4 | Quản lý khách hàng | `CustomerListFragment.java` + `CustomerFormActivity.java` | Danh sách + CRUD khách hàng |
| 5 | Quản lý sản phẩm | `ProductListFragment.java` + `ProductFormActivity.java` | Danh sách + CRUD sản phẩm |
| 6 | **Tạo đơn hàng** | **`OrderActivity.java` (Master-Detail)** | **Form chính: chọn khách; form phụ: chọn sản phẩm (QT)** |
| 7 | Danh sách đơn hàng | `OrderListFragment.java` | Lịch sử đơn hàng |
| 8 | Chi tiết đơn hàng | `OrderDetailActivity.java` | Xem chi tiết, in hóa đơn |
| 9 | Quản lý lịch hẹn | `AppointmentFragment.java` + `AppointmentFormActivity.java` | CRUD lịch hẹn |
| 10 | **Nhập kho** | **`InventoryActivity.java` (Master-Detail)** | **Form chính: nhập kho; form phụ: chọn sản phẩm (QT)** |
| 11 | Quản lý dịch vụ | `ServiceFragment.java` + `ServiceFormActivity.java` | CRUD dịch vụ |
| 12 | Quản lý khuyến mãi | `PromotionFragment.java` + `PromotionFormActivity.java` | CRUD khuyến mãi |
| 13 | Quản lý nhà cung cấp | `SupplierFragment.java` + `SupplierFormActivity.java` | CRUD nhà cung cấp |
| 14 | **Thống kê / Báo cáo** | **`StatisticActivity.java`** | **Biểu đồ doanh thu, báo cáo (Report)** |
| 15 | Quản lý tài khoản | `UserManagementActivity.java` | CRUD người dùng (Admin) |
| 16 | **Thanh toán** | **`PaymentDialog.java`** | **Tạo giao dịch thanh toán, chọn phương thức, hoàn tiền** |
| 17 | **Nhật ký hoạt động** | **`ActivityLogActivity.java`** | **Xem log, lọc theo ngày/nhân viên/hành động (Admin)** |
| 18 | **Gán nhân viên phục vụ** | **`AssignStaffDialog.java`** | **Gán nhân viên cho lịch hẹn, chọn vai trò** |
| | **=== CUSTOMER APP (App khách hàng) ===** | | |
| 16 | **Đăng nhập / Đăng ký KH** | **`CustomerLoginActivity.java`** | **Đăng nhập bằng SĐT/mật khẩu, đăng ký tài khoản** |
| 17 | **Trang chủ khách hàng** | **`CustomerHomeActivity.java`** | **DS dịch vụ, sản phẩm, khuyến mãi, thông báo** |
| 18 | **Đặt lịch hẹn** | **`BookingActivity.java`** | **Chọn dịch vụ → chọn thú cưng → chọn ngày/giờ → xác nhận** |
| 19 | **Lịch sử giao dịch** | **`OrderHistoryActivity.java`** | **DS đơn hàng, hóa đơn đã mua, chi tiết** |
| 20 | **Lịch sử đặt lịch** | **`BookingHistoryActivity.java`** | **DS lịch hẹn (sắp tới, đã hoàn thành, đã hủy)** |
| 21 | **Thống kê cá nhân** | **`MyStatisticActivity.java`** | **Biểu đồ: chi tiêu theo tháng, dịch vụ đã dùng, số lần mua** |
| 22 | **Đánh giá dịch vụ** | **`ReviewActivity.java`** | **Đánh giá sao + nhận xét cho dịch vụ đã sử dụng** |
| 23 | **Giỏ hàng** | **`CartActivity.java`** | **Giỏ hàng online, áp mã giảm giá, thanh toán** |
| 24 | **Thú cưng của tôi** | **`MyPetActivity.java`** | **DS thú cưng, hồ sơ sức khỏe, lịch tiêm** |
| 25 | **Hồ sơ cá nhân** | **`ProfileActivity.java`** | **Thông tin tài khoản, đổi mật khẩu, hạng thành viên** |
| 26 | **Thông báo** | **`NotificationActivity.java`** | **Danh sách thông báo (nhắc lịch, khuyến mãi, …)** |

---

## Tổng kết

| Loại | Số lượng | Yêu cầu | Đạt |
|---|---|---|---|
| Chức năng đối tượng (ĐT) | 23 | ≥ 10 | ✅ |
| Chức năng quy trình (QT) | 4 (Tạo đơn hàng, Nhập kho, **Đặt lịch hẹn, Thanh toán**) | ≥ 2 | ✅ |
| Form Master-Detail | 4 (Order, Inventory, **Payment, AppointmentStaff**) | Có | ✅ |
| Tổng số form | 29 | ≥ 10 | ✅ |
| Report / Thống kê | 2 (Staff Statistic, **Customer Statistic**) | Có | ✅ |
| Audit log | 1 (ActivityLogActivity) | Cộng điểm | ✅ |
| Chức năng hỗ trợ | Phân quyền, tìm kiếm, lọc, **đánh giá, tích điểm, thông báo, audit log** | Cộng điểm | ✅ |

> Dựa theo thang điểm mục 2.1 và 2.2 (APP): đủ ≥10 chức năng đối tượng, ≥2 chức năng quy trình, ≥10 form, có form master-detail, có report.
