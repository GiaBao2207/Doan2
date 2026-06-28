# Nhiệm Vụ — PetStoreApp

## Hướng dẫn sử dụng
- [ ] = Chưa làm
- [x] = Đã xong
- [!] = Đang làm
- [~] = Tạm dừng / Cần xem lại

---

## GIAI ĐOẠN 1: Thiết kế (Design Phase)

### 1.1 Tài liệu

- [x] README.md — Giới thiệu project
- [x] CHUC-NANG.md — Danh sách chức năng theo MVVM
- [x] DATABASE.md — Thiết kế database 23 bảng tiếng Việt
- [x] LUONG-THUC-TE.md — Mô tả luồng thực tế Staff + Customer
- [x] VD-LUONG-NGUOI-DUNG.md — Ví dụ chi tiết theo từng role
- [x] GOI-Y-TINH-NANG-KHACH-HANG.md — Gợi ý tính năng nâng cao
- [x] implementation-notes.md — Nhận xét, đánh giá tổng thể
- [ ] nhiem-vu-can-lam.md — Tổng hợp nhiệm vụ chi tiết từng module
- [ ] api-endpoints.md — Nếu có backend

### 1.2 Database Room

- [x] Liệt kê đủ 23 bảng (20 gốc + 3 bổ sung)
- [ ] Tạo file SQL script CREATE TABLE cho 23 bảng
- [ ] Định nghĩa các Entity class (Java)
- [ ] Định nghĩa các DAO interface (Java)
- [ ] Định nghĩa AppDatabase (RoomDatabase)
- [ ] Thêm index cho các cột truy vấn thường xuyên

### 1.3 Thiết kế UI (Wireframe)

- [ ] Vẽ wireframe cho Staff App (Login, Dashboard, Order, Pet, ...)
- [ ] Vẽ wireframe cho Customer App (Login, Home, Booking, History, ...)
- [ ] Xác định Navigation Graph (luồng màn hình)

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
- [ ] OrderRepository.placeOrder() — xử lý transaction
- [ ] OrderViewModel
- [ ] Kiểm tra tồn kho trước khi lưu
- [ ] Áp dụng mã khuyến mãi
- [ ] Chọn phương thức thanh toán
- [ ] Lưu đơn → tự động trừ tồn kho
- [ ] Danh sách đơn hàng (OrderListFragment)
- [ ] Chi tiết đơn hàng (OrderDetailActivity)
- [ ] In hóa đơn (PDF / Bluetooth)

### 2.9 Quản lý Lịch hẹn

- [ ] AppointmentFragment (danh sách, lọc theo ngày/tuần)
- [ ] AppointmentFormActivity (thêm/sửa lịch hẹn)
- [ ] Check-in / Check-out lịch hẹn
- [ ] Hủy / Dời lịch
- [ ] Gán nhân viên phục vụ (NhânViênPhụcVụ)
- [ ] AppointmentDao + AppointmentRepository + AppointmentViewModel

### 2.10 Nhập kho (Quy trình phụ — Master-Detail)

- [ ] InventoryActivity — Form Master: chọn nhà cung cấp
- [ ] Dialog chọn sản phẩm — Form Detail: nhập số lượng, giá
- [ ] InventoryRepository — xử lý transaction
- [ ] InventoryDao + InventoryDetailDao
- [ ] Lưu phiếu → tự động cộng tồn kho

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
- [ ] Quên mật khẩu (OTP qua SMS/Email)

### 3.2 Trang chủ khách hàng

- [ ] CustomerHomeActivity
- [ ] Banner khuyến mãi
- [ ] Dịch vụ nổi bật
- [ ] Thông báo gần đây
- [ ] Hiển thị điểm thưởng, hạng thành viên

### 3.3 Đặt lịch hẹn (Quy trình)

- [ ] BookingActivity — 3 bước: chọn dịch vụ → chọn thú cưng → chọn ngày/giờ
- [ ] Kiểm tra trùng lịch
- [ ] Xác nhận và lưu

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
| Entity class (23 bảng) | 23 | 0 | **23** |
| DAO interface | 23 | 0 | **23** |
| Repository | 15 | 0 | **15** |
| ViewModel | 17 | 0 | **17** |
| Staff UI screens | 16 | 0 | **16** |
| Customer UI screens | 11 | 0 | **11** |

> **Tổng tiến độ:** 7/7 tài liệu thiết kế ✅ | Code nguồn: chưa bắt đầu
