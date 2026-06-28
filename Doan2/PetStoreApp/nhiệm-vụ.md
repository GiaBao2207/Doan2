# Nhiệm Vụ — PetStoreApp

## Hướng dẫn sử dụng
- [ ] = Chưa làm
- [x] = Đã xong
- [!] = Đang làm
- [~] = Tạm dừng / Cần xem lại

## Việc cần làm ngay (Priority — thứ tự ưu tiên)

> ✅ **Thiết kế đã hoàn tất: 7/7 tài liệu, 24 bảng SQL, 8 luồng, 5 quy trình, 34 form.**
> Bắt đầu code theo thứ tự dưới đây:

### Bước 1: Tạo project Android + Gradle
- Tạo project mới trên Android Studio, đặt tên `PetStoreApp`
- Cài dependencies: Room, LiveData, RecyclerView, Material
- Tạo package structure (model, data, ui, viewmodel)

### Bước 2: Entity + DAO + Database (24 bảng)
- Viết 24 Entity class Java (`@Entity(tableName = "...")`)
- Viết 24 DAO interface (`@Dao` + `@Insert`/`@Update`/`@Delete`/`@Query`)
- Viết AppDatabase (`@Database(entities = {...}, version = 1)`)
- Kiểm tra biên dịch không lỗi

### Bước 3: Core (DI, Preference, Password)
- ServiceLocator / Hilt
- PreferenceManager (session login)
- PasswordUtils (SHA-256 + Salt)

### Bước 4: Staff App (21 màn hình) — theo thứ tự
1. Login → 2. Dashboard → 3. PetCategory/ProductCategory/Service/Supplier (CRUD)
4. Pet → 5. Customer → 6. Product → 7. Cage → 8. Order → 9. Appointment
10. Inventory → 11. Promotion → 12. Statistic → 13. Payment
14. CancelOrder → 15. InventoryAdjustment → 16. AssignStaff → 17. ActivityLog

### Bước 5: Customer App (13 màn hình)
- Login → ForgotPassword → Home → Booking → CancelBooking
- History → Statistic → Review → Cart → MyPet → Profile → Notification

### Bước 6: Test + Hoàn thiện

---

## GIAI ĐOẠN 1: Thiết kế (Design Phase)

### 1.1 Tài liệu

- [x] README.md — Giới thiệu project
- [x] CHUC-NANG.md — Danh sách chức năng theo MVVM
- [x] DATABASE.md — Thiết kế database 24 bảng tiếng Việt
- [x] LUONG-THUC-TE.md — Mô tả luồng thực tế Staff + Customer
- [x] VD-LUONG-NGUOI-DUNG.md — Ví dụ chi tiết theo từng role
- [x] GOI-Y-TINH-NANG-KHACH-HANG.md — Gợi ý tính năng nâng cao
- [x] implementation-notes.md — Nhận xét, đánh giá tổng thể
- [ ] nhiem-vu-can-lam.md — Tổng hợp nhiệm vụ chi tiết từng module
- [ ] api-endpoints.md — Nếu có backend

### 1.2 Database Room

- [x] Liệt kê đủ 24 bảng (20 gốc + 4 bổ sung: ThanhToán, NhậtKýHoạtĐộng, NhânViênPhụcVụ, Chuồng)
- [x] Tạo SQL CREATE TABLE cho 24 bảng (trong DATABASE.md)
- [x] Thêm 10 index cho các cột truy vấn thường xuyên
- [x] Thêm state machine cho DonHang (6 trạng thái) + LichHen (5 trạng thái)
- [x] ThanhToán CHECK (soTien != 0) hỗ trợ số âm hoàn tiền
- [x] ActivityLog: 16 hành động đầy đủ
- [!] **Định nghĩa các Entity class (Java)** — cần code
- [!] **Định nghĩa các DAO interface (Java)** — cần code
- [!] **Định nghĩa AppDatabase (RoomDatabase)** — cần code

### 1.3 Thiết kế UI (Wireframe)

- [ ] Vẽ wireframe cho Staff App (Login, Dashboard, Order, Pet, ...)
- [ ] Vẽ wireframe cho Customer App (Login, Home, Booking, History, ...)
- [ ] Xác định Navigation Graph (luồng màn hình)
> Có thể bỏ qua wireframe nếu code trực tiếp theo UI mô tả trong LUONG-THUC-TE.md + VD-LUONG-NGUOI-DUNG.md (đã có ASCII mockup chi tiết)

---

## GIAI ĐOẠN 2: Code — Staff App (App Nhân viên)

### 2.1 Xương sống (Core)

- [ ] Thiết lập Gradle (dependencies: Room, LiveData, RecyclerView, Material)
- [ ] Tạo package structure theo MVVM
- [ ] DI: Cài đặt Hilt / Dagger / ServiceLocator
- [ ] Navigation: Thiết lập Navigation Component
- [ ] PreferenceManager: Lưu session đăng nhập
- [ ] PasswordUtils: Mã hóa mật khẩu SHA-256 + Salt

### 2.2 Đăng nhập & Phân quyền

- [ ] LoginActivity UI (username, password, login button)
- [ ] LoginViewModel (kiểm tra thông tin, phân quyền)
- [ ] UserDao (login, CRUD user)
- [ ] UserRepository
- [ ] Phân quyền: Admin → full; Staff → bán hàng
- [ ] Quản lý tài khoản (UserManagementActivity) — CRUD nhân viên
- [ ] UserFormActivity (thêm/sửa nhân viên)

### 2.3 Dashboard

- [ ] DashboardActivity — tổng quan: doanh thu hôm nay, số thú cưng, tồn kho
- [ ] DashboardViewModel
- [ ] Hiển thị lịch hẹn hôm nay (RecyclerView)

### 2.4 Quản lý Danh mục

- [ ] PetCategory — CRUD loại thú cưng
- [ ] ProductCategory — CRUD loại sản phẩm
- [ ] Service — CRUD dịch vụ
- [ ] Supplier — CRUD nhà cung cấp

### 2.5 Quản lý Thú cưng

- [ ] PetListFragment (danh sách, tìm kiếm, lọc)
- [ ] PetFormActivity (thêm/sửa thú cưng)
- [ ] PetDao + PetRepository + PetViewModel
- [ ] Hiển thị loại, chủ sở hữu

### 2.6 Quản lý Khách hàng

- [ ] CustomerListFragment (danh sách, tìm theo SĐT/tên)
- [ ] CustomerFormActivity (thêm/sửa khách hàng)
- [ ] CustomerDao + CustomerRepository + CustomerViewModel

### 2.7 Quản lý Sản phẩm

- [ ] ProductListFragment (danh sách, lọc theo danh mục)
- [ ] ProductFormActivity (thêm/sửa sản phẩm)
- [ ] ProductDao + ProductRepository + ProductViewModel

### 2.8 Tạo đơn hàng (Quy trình chính — Master-Detail)

- [ ] OrderActivity — Form Master: chọn khách, hiển thị tổng tiền
- [ ] Dialog chọn sản phẩm — Form Detail: thêm sản phẩm, số lượng
- [ ] OrderRepository.placeOrder() — xử lý transaction (Order + OrderDetail + Payment + trừ tồn + log)
- [ ] OrderViewModel
- [ ] Kiểm tra tồn kho trước khi lưu
- [ ] Áp dụng mã khuyến mãi
- [ ] Chọn phương thức thanh toán
- [ ] Lưu đơn → tự động trừ tồn kho + ghi ActivityLog
- [ ] Danh sách đơn hàng (OrderListFragment)
- [ ] Chi tiết đơn hàng (OrderDetailActivity)
- [ ] In hóa đơn (PDF / Bluetooth)

### 2.8b Hủy đơn & Hoàn tiền (Quy trình mới)

- [ ] CancelOrderDialog — chọn lý do hủy, xác nhận hủy
- [ ] Xử lý: cập nhật status = 'cancelled'/'refunded'
- [ ] Trả lại tồn kho (ProductDao.updateQuantity + qty)
- [ ] Ghi giao dịch hoàn tiền (PaymentDao.insert soTien âm, trangThai='hoan_tien')
- [ ] Ghi ActivityLog ('HUY_DON', 'HOAN_TIEN')

### 2.9 Quản lý Lịch hẹn

- [ ] AppointmentFragment (danh sách, lọc theo ngày/tuần)
- [ ] AppointmentFormActivity (thêm/sửa lịch hẹn)
- [ ] Check-in / Check-out lịch hẹn
- [ ] Hủy / Dời lịch (ghi ActivityLog 'HUY_LICH')
- [ ] Kiểm tra xung đột lịch hẹn (checkConflict: trùng thú cưng / trùng nhân viên)
- [ ] Gán nhân viên phục vụ (NhânViênPhụcVụ — AssignStaffDialog)
- [ ] AppointmentDao + AppointmentRepository + AppointmentViewModel

### 2.10 Nhập kho (Quy trình phụ — Master-Detail)

- [ ] InventoryActivity — Form Master: chọn nhà cung cấp
- [ ] Dialog chọn sản phẩm — Form Detail: nhập số lượng, giá
- [ ] InventoryRepository — xử lý transaction (Inventory + InventoryDetail + cộng tồn + log)
- [ ] InventoryDao + InventoryDetailDao
- [ ] Lưu phiếu → tự động cộng tồn kho + ghi ActivityLog

### 2.10b Điều chỉnh tồn kho (Inventory Adjustment)

- [ ] InventoryAdjustmentDialog — chọn sản phẩm, loại ĐC (hỏng, hết hạn, trả NCC, kiểm kê)
- [ ] Xử lý cập nhật số lượng tồn
- [ ] Ghi ActivityLog ('DIEU_CHINH_TON')

### 2.11 Khuyến mãi

- [ ] PromotionFragment (danh sách mã giảm giá)
- [ ] PromotionFormActivity (thêm/sửa khuyến mãi)
- [ ] Kiểm tra mã hợp lệ (còn hạn, còn lượt)

### 2.12 Thống kê / Báo cáo

- [ ] StatisticActivity với các tab:
  - [ ] Doanh thu ngày (7 ngày gần nhất)
  - [ ] Doanh thu tháng (12 tháng)
  - [ ] Top 10 sản phẩm bán chạy
  - [ ] Tồn kho thấp (<5)
- [ ] Biểu đồ cột đơn giản (MPAndroidChart / tự vẽ)
- [ ] Xuất Excel

### 2.13 Thanh toán (bảng ThanhToán)

- [ ] Khi lưu đơn → tự động tạo bản ghi ThanhToán
- [ ] Hỗ trợ thanh toán nhiều lần / nhiều phương thức
- [ ] Hoàn tiền (refund)

### 2.14 Nhật ký hoạt động

- [ ] Ghi log tự động khi: tạo đơn, sửa giá, xóa thú, ...

---

## GIAI ĐOẠN 3: Code — Customer App (App Khách hàng)

### 3.1 Đăng nhập / Đăng ký

- [ ] CustomerLoginActivity (đăng nhập bằng SĐT + mật khẩu)
- [ ] CustomerRegisterActivity (đăng ký tài khoản mới)
- [ ] ForgotPasswordActivity: nhập SĐT → OTP → đặt lại MK
- [ ] Xử lý gửi OTP (có thể giả lập trong đồ án)
- [ ] Ghi ActivityLog khi đăng nhập ('DANG_NHAP'), đổi MK ('DOI_MAT_KHAU')

### 3.2 Trang chủ khách hàng

- [ ] CustomerHomeActivity
- [ ] Banner khuyến mãi
- [ ] Dịch vụ nổi bật
- [ ] Thông báo gần đây
- [ ] Hiển thị điểm thưởng, hạng thành viên

### 3.3 Đặt lịch hẹn (Quy trình)

- [ ] BookingActivity — 3 bước: chọn dịch vụ → chọn thú cưng → chọn ngày/giờ
- [ ] Kiểm tra xung đột (checkConflict: trùng thú cưng + giờ)
- [ ] Gợi ý khung giờ trống nếu bị trùng
- [ ] Xác nhận và lưu + ghi ActivityLog ('TAO_LICH')
- [ ] Hủy lịch hẹn (CancelBookingDialog: hủy trước 2h miễn phí)

### 3.4 Lịch sử giao dịch

- [ ] OrderHistoryActivity (đơn hàng đã mua)
- [ ] BookingHistoryActivity (lịch hẹn sắp tới / đã hoàn thành / đã hủy)
- [ ] Chi tiết đơn hàng (OrderDetailActivity)
- [ ] Chi tiết lịch hẹn

### 3.5 Thống kê cá nhân

- [ ] MyStatisticActivity
  - [ ] Chi tiêu theo tháng (biểu đồ)
  - [ ] Dịch vụ đã sử dụng (thống kê)
  - [ ] Hạng thành viên + lộ trình lên hạng

### 3.6 Đánh giá dịch vụ

- [ ] ReviewActivity (đánh giá sao + nhận xét)
- [ ] Danh sách dịch vụ đã hoàn thành chờ đánh giá
- [ ] Tích điểm khi đánh giá (+10 điểm)

### 3.7 Giỏ hàng & Mua online

- [ ] CartActivity (xem, sửa số lượng, xóa)
- [ ] Thêm sản phẩm vào giỏ
- [ ] Áp mã giảm giá
- [ ] Đặt hàng → tạo đơn → xóa giỏ hàng
- [ ] Thanh toán (tiền mặt / chuyển khoản)

### 3.8 Thú cưng của tôi

- [ ] MyPetActivity (danh sách thú cưng của khách)
- [ ] Thêm/sửa thú cưng
- [ ] Hồ sơ sức khỏe (PetHealthRecord) — lịch tiêm, tái khám
- [ ] Thú cưng yêu thích (FavoritePet)

### 3.9 Hồ sơ cá nhân

- [ ] ProfileActivity (thông tin tài khoản)
- [ ] Đổi mật khẩu
- [ ] Xem hạng thành viên, điểm thưởng

### 3.10 Thông báo

- [ ] NotificationActivity (danh sách thông báo)
- [ ] Đánh dấu đã đọc
- [ ] Push notification (FCM) — nhắc lịch, khuyến mãi

---

## GIAI ĐOẠN 4: Kiểm thử & Hoàn thiện

### 4.1 Kiểm thử

- [ ] Unit Test cho ViewModel (JUnit)
- [ ] Unit Test cho Repository
- [ ] UI Test cho các màn hình chính (Espresso)
- [ ] Test luồng: Tạo đơn → Thanh toán → In hóa đơn
- [ ] Test luồng: Đặt lịch → Check-in → Hoàn thành
- [ ] Test luồng: Nhập kho → Cộng tồn → Bán → Trừ tồn
- [ ] Test phân quyền: Admin/Staff/Customer

### 4.2 Xử lý lỗi & Ngoại lệ

- [ ] Loading state cho tất cả màn hình (ProgressBar)
- [ ] Error state (Snackbar, Toast, dialog)
- [ ] Empty state (hình ảnh + text "Không có dữ liệu")
- [ ] Xử lý mất mạng (Offline mode)
- [ ] Xử lý lỗi database (try-catch)

### 4.3 Bảo mật

- [ ] Mã hóa mật khẩu SHA-256 + Salt
- [ ] Phân quyền chặt chẽ (kiểm tra role ở mọi action)
- [ ] Validate input tất cả form
- [ ] Không cho xóa/sửa NhậtKýHoạtĐộng

### 4.4 UX / UI

- [ ] Material Design toàn bộ app
- [ ] Responsive layout (điện thoại + máy tính bảng)
- [ ] Hỗ trợ i18n (Tiếng Việt + Tiếng Anh)

---

## GIAI ĐOẠN 5: Nâng cao (Điểm cộng)

- [ ] Sao lưu database (export .db ra thẻ nhớ)
- [ ] Mã QR / Barcode cho sản phẩm (ZXing)
- [ ] Push notification (Firebase Cloud Messaging)
- [ ] Thanh toán online (VNPay / Momo)
- [ ] Bản đồ cửa hàng (Google Maps)
- [ ] Gợi ý thông minh (dựa trên giống thú cưng, lịch sử)
- [ ] Minigame / Check-in nhận điểm thưởng
- [ ] Phiếu sức khỏe PDF

---

## Danh sách kiểm tra nhanh

| Khu vực | SL | Đã làm | Còn lại |
|---------|----|--------|---------|
| Tài liệu thiết kế (.md) | 7 | **7** | **0** ✅ |
| SQL CREATE TABLE | 24 | **24** | **0** ✅ |
| Entity class (Java) | 24 | 0 | **24** |
| DAO interface | 24 | 0 | **24** |
| Database class | 1 | 0 | **1** |
| Repository | 19 | 0 | **19** |
| ViewModel | 21 | 0 | **21** |
| Staff UI screens | 21 | 0 | **21** |
| Customer UI screens | 13 | 0 | **13** |

> **Tổng tiến độ:** Thiết kế 7/7 ✅ | SQL 24/24 ✅ | Code nguồn: chưa bắt đầu ❌ | **Việc tiếp theo: Code Entity → DAO → Database → Repository → ViewModel → UI**
