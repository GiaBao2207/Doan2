# Implementation Notes & Review

> Phân tích và nhận xét về thiết kế PetStoreApp.  
> **Lưu ý:** File này được cập nhật sau mỗi lần thay đổi thiết kế.

## Tổng Quan

Dự án là một ứng dụng Android quản lý cửa hàng thú cưng với **2 app riêng** (Staff + Customer) dùng chung database, kiến trúc MVVM, ngôn ngữ Java, Room database.

**Trạng thái hiện tại:** Mới chỉ có tài liệu thiết kế (`.md`), chưa có code nguồn (Java/XML).

---

## ✅ Những Điểm Làm Tốt

### 1. Phân tích nghiệp vụ đầy đủ
- Bao phủ hầu hết nghiệp vụ của cửa hàng thú cưng thực tế: bán hàng, nhập kho, lịch hẹn, dịch vụ, khuyến mãi, tích điểm, đánh giá.
- Phân vai rõ ràng: Admin / Staff / Customer với quyền hạn khác nhau.
- Có cả **bán tại quầy** (Staff app) và **kênh online** (Customer app).

### 2. Kiến trúc MVVM hợp lý
- Phân tách rõ View → ViewModel → Repository → DAO → Database.
- Sử dụng LiveData để cập nhật UI tự động.

### 3. Thiết kế database toàn diện (đã cải thiện)
- Từ **20 bảng** nâng lên **24 bảng** — thêm `ThanhToán`, `NhậtKýHoạtĐộng`, `NhânViênPhụcVụ`, `Chuồng`.
- Đầy đủ khóa ngoại (33 FK), ràng buộc NOT NULL, UNIQUE, DEFAULT, CHECK.
- 4 quan hệ Master-Detail + state machine (DonHang 6 trạng thái, LichHen 5 trạng thái).
- Toàn bộ tên bảng đã đổi sang tiếng Việt.

### 4. Biểu đồ Use Case đầy đủ
- Đã bổ sung **32 use case** có ma trận phân theo 3 role.
- Mô tả chi tiết 3 use case quan trọng nhất: Tạo đơn hàng, Đặt lịch hẹn, Nhập kho.

### 5. Quy trình nghiệp vụ được mô tả chi tiết
- Các file `LUONG-THUC-TE.md` và `VD-LUONG-NGUOI-DUNG.md` mô tả từng bước người dùng thao tác, rất tốt cho việc code sau này.

---

## ⚠️ Những Điểm Cần Cải Thiện / Bổ Sung

### 1. **THIẾU CODE NGUỒN** (Quan trọng nhất)
- Toàn bộ dự án mới dừng ở thiết kế, chưa có file Java, XML layout, Gradle build.
- **Cần triển khai code để đánh giá đúng chất lượng.**

### 2. Vấn đề về Database (đã xử lý một phần)

| Vấn đề | Trạng thái |
|--------|-----------|
| **Thiếu bảng Employee** | ⚠️ Vẫn thiếu — `User` chưa có salary, shift, hireDate |
| **Không có bảng lịch sử giá** | ❌ Vẫn thiếu — chưa thêm `ProductPriceHistory` |
| **Pet gắn cứng customerId** | ❌ Vẫn chưa hỗ trợ đồng sở hữu |
| **Thiếu bảng Payment** | ✅ **Đã thêm** — `ThanhToán` với đầy đủ fields |
| **Thiếu Audit Log** | ✅ **Đã thêm** — `NhậtKýHoạtĐộng` append-only |
| **Thiếu gán nhân viên lịch hẹn** | ✅ **Đã thêm** — `NhânViênPhụcVụ` |
| **Thiếu chỉ mục (Index)** | ✅ **Đã thêm** — 10 index cho các cột thường truy vấn |
| **Product.quantity** | ⚠️ Chưa phân biệt tồn khả dụng / tồn thực tế |

### 3. Vấn đề về Chức năng Nghiệp vụ

| Vấn đề | Trạng thái | Ghi chú |
|--------|-----------|---------|
| **Quên mật khẩu** | ✅ **Đã xử lý** | ForgotPasswordActivity + OTP flow tại VD-LUONG-NGUOI-DUNG.md |
| **Hủy đơn hàng + Hoàn tiền** | ✅ **Đã xử lý** | CancelOrderDialog, state machine 6 trạng thái, hoàn tiền qua Payment.soTien âm |
| **Xung đột lịch hẹn** | ✅ **Đã xử lý** | checkConflict() kiểm tra trùng thú cưng + nhân viên |
| **Gán nhân viên phục vụ** | ✅ **Đã xử lý** | Luồng 7: AssignStaffDialog + auto-assign |
| **Điều chỉnh tồn kho** | ✅ **Đã xử lý** | InventoryAdjustmentDialog: hỏng, hết hạn, trả NCC, kiểm kê |
| **ActivityLog đầy đủ** | ✅ **Đã xử lý** | 16 hành động cho tất cả luồng |
| **Xuất Excel / PDF** | ⚠️ Đã nhắc đến | Cần spec rõ luồng export |
| **Giao hàng** | ⚠️ shippingAddress có sẵn | Chưa có bảng địa chỉ riêng |

### 4. Vấn đề về Kỹ thuật / Kiến trúc

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Staff & Customer chung DB trên cùng máy?** | Tài liệu nói "cả 2 app cùng truy cập 1 DB" → trên Android mỗi app có sandbox riêng | Cần backend REST API hoặc 2 module trong 1 APK |
| **Không có lớp Service / UseCase** | ViewModel gọi thẳng Repository | Nên thêm `UseCase` giữa ViewModel và Repository |
| **Threading** | Room yêu cầu không chạy trên main thread | Cần AsyncTask/Coroutines/RxJava + LiveData |
| **Không có Dependency Injection** | Code khó maintain | Nên dùng Hilt |
| **Không có Navigation Component** | Không có graph cho luồng màn hình | Nên dùng Android Navigation Component |
| **Không có Unit Test / UI Test** | Chưa đề cập testing | Nên có ít nhất Unit Test cho ViewModel + Repository |

### 5. Vấn đề về UX / UI

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Offline-first** | Mất mạng là không dùng được | Cơ chế cache + sync |
| **Ngôn ngữ** | Chỉ có tiếng Việt | Nên hỗ trợ i18n (VN + EN) |
| **Loading / Error / Empty state** | Chưa được thiết kế | Cần thêm vào mọi màn hình |
| **Pagination** | Danh sách dài không có phân trang | Nên thêm Paging 3 |

### 6. Vấn đề về Bảo mật

| Vấn đề | Đề xuất |
|--------|---------|
| **Mật khẩu SHA-256 + Salt** | Chấp nhận được với đồ án; thực tế cần backend + JWT |
| **Phân quyền** | Nên dùng enum + Permission-based thay vì role string |
| **SQL Injection** | Room đã an toàn, lưu ý khi dùng raw query |

---

## Nhật ký thay đổi thiết kế

| Ngày | Thay đổi | File ảnh hưởng |
|------|---------|----------------|
| 28/06/2026 | Tạo implementation-notes.md ban đầu | implementation-notes.md |
| 28/06/2026 | Đánh giá: thiếu 8 bảng, cần bổ sung | implementation-notes.md |
| 28/06/2026 | Thêm 3 bảng: ThanhToán, NhậtKýHoạtĐộng, NhânViênPhụcVụ | DATABASE.md |
| 28/06/2026 | Đổi tên toàn bộ 23 bảng sang tiếng Việt | DATABASE.md |
| 28/06/2026 | Sửa ERD đầy đủ 23 bảng (3 phần) | DATABASE.md |
| 28/06/2026 | Thêm biểu đồ Use Case + 32 use case + mô tả chi tiết 3 UC chính | DATABASE.md |
| 28/06/2026 | Tạo file nhiệm-vụ.md với 5 giai đoạn, ~100 tasks | nhiệm-vụ.md |
| 28/06/2026 | Cập nhật implementation-notes.md sau các thay đổi | implementation-notes.md |
| 28/06/2026 | Cập nhật CHUC-NANG.md: 20→23 entities, DAOs, Repos, ViewModels, UI, summary | CHUC-NANG.md |
| 28/06/2026 | Cập nhật README.md: 20→23 bảng | README.md |
| 28/06/2026 | Cập nhật LUONG-THUC-TE.md: thêm Payment, ActivityLog vào các luồng | LUONG-THUC-TE.md |
| 28/06/2026 | Cập nhật VD-LUONG-NGUOI-DUNG.md: thêm Admin xem log, Staff ghi payment+log | VD-LUONG-NGUOI-DUNG.md |
| 28/06/2026 | Cập nhật GOI-Y-TINH-NANG-KHACH-HANG.md: reference đến bảng ThanhToán | GOI-Y-TINH-NANG-KHACH-HANG.md |
| 28/06/2026 | Viết lại ERD đầy đủ 23 bảng: mỗi bảng hiển thị tất cả thuộc tính + PK/FK rõ ràng | DATABASE.md |
| 28/06/2026 | Chuyển toàn bộ ERD sang SQL CREATE TABLE (sẵn code để chạy), thêm index, dọn duplicate | DATABASE.md |
| 28/06/2026 | Fix: ThanhToán CHECK (soTien != 0) cho phép số âm hoàn tiền; thêm state machine DonHang + LichHen; thêm danh sách 16 hành động ActivityLog | DATABASE.md |
| 28/06/2026 | Thêm Luồng 6: Hủy đơn + Hoàn tiền (có CancelOrderDialog, trả tồn, hoàn tiền) | LUONG-THUC-TE.md |
| 28/06/2026 | Thêm xử lý xung đột lịch hẹn vào Luồng 2b; Luồng 7: Gán nhân viên (auto-assign) | LUONG-THUC-TE.md |
| 28/06/2026 | Thêm Luồng 8: Điều chỉnh tồn kho (hỏng, hết hạn, trả NCC, kiểm kê) + bảng 16 hành động ActivityLog | LUONG-THUC-TE.md |
| 28/06/2026 | Thêm Quên mật khẩu (ForgotPasswordActivity), Hủy lịch hẹn (CancelBookingDialog), ActivityLog đăng nhập | VD-LUONG-NGUOI-DUNG.md |
| 28/06/2026 | Cập nhật CHUC-NANG.md: QT 4→5 (thêm Hủy đơn/Hoàn tiền), form 29→33 (thêm 4 màn hình mới), fix ViewModel numbering | CHUC-NANG.md |
| 28/06/2026 | Cập nhật nhiệm-vụ.md: thêm tasks cho hủy đơn, xung đột, quên MK, điều chỉnh tồn | nhiệm-vụ.md |
| 28/06/2026 | Fix README.md: DiemThanhVien→DiemThuong, ThongBaoKhachHang→ThongBaoKhach (khớp với SQL) | README.md |
| 28/06/2026 | Fix CHUC-NANG.md: ViewModel #4 duplicate→21 VMs, summary table header | CHUC-NANG.md |
| 28/06/2026 | Verify toàn bộ 8 file: 23 tables, 10 indexes, 8 flows, 5 rules, 33 forms, 21 VMs, 18 repos | implementation-notes.md |
| 28/06/2026 | Thêm bảng Chuồng (Chuong): maChuong UNIQUE, khuVuc, kichThuoc, loaiThuCungId FK, trangThai (empty/occupied/maintenance/cleaning) + chuongId FK trong ThuCung + status 'boarding' → 24 bảng | DATABASE.md |
| 28/06/2026 | Thêm Cage entity, CageDao, CageRepository, CageActivity vào CHUC-NANG.md → 24 ĐT, 19 Repos, 34 form | CHUC-NANG.md |
| 28/06/2026 | Viết lại README.md hoàn chỉnh: mô tả hệ thống, công nghệ, 8 nghiệp vụ, package structure, 24-table ref, tổng kết đồ án | README.md |
| 28/06/2026 | Cập nhật nhiệm-vụ.md: 24 bảng, 34 form, 19 repos, staff 21 màn hình, thêm Cage vào bước 4 | nhiệm-vụ.md |
| 28/06/2026 | Chuẩn hóa anchored summary cuối mỗi phiên: mô tả toàn bộ thay đổi, tiến độ, next steps | (trong conversation) |

---

## 🧮 So Sánh Với Thực Tế

### Cửa hàng thú cưng thực tế cần thêm:
- **Quản lý giống / nhân giống** (breeding management)
- **Quản lý chuồng / lồng** ✅ **Đã thêm** — bảng Chuồng + chuồngId trong ThuCung
- **Bảo hành thú cưng** (sức khỏe 7-30 ngày sau khi mua)
- **Hợp đồng mua bán thú cưng** (giấy tờ pháp lý)
- **Dashboard realtime** (số khách trong cửa hàng, công việc đang chờ)

### Phù hợp với đồ án không?
- **Rất phù hợp** với yêu cầu đồ án môn học (≥10 ĐT, ≥2 QT, ≥10 form, master-detail, report).
- Các tính năng hiện tại đã đáp ứng vượt yêu cầu đồ án.
- Cần tập trung **code sạch, đúng MVVM, chạy được, ít bug** hơn là thêm quá nhiều tính năng.

---

## Việc cần làm ngay

> **Thiết kế đã xong 100%.** Chuyển sang code theo thứ tự:

### 🥇 Tuần 1: Xương sống + Staff cơ bản
1. Tạo project Android + Gradle (Room, LiveData, Material)
2. Code **24 Entity class** (ánh xạ @Entity → SQL table)
3. Code **24 DAO interface** (@Dao)
4. Code **AppDatabase** (RoomDatabase, version 1)
5. Code **Core**: PreferenceManager, PasswordUtils, ServiceLocator
6. Code **Login** (LoginActivity + LoginViewModel + UserDao)

### 🥇 Tuần 2: Staff CRUD + Quy trình chính
7. Dashboard + các CRUD (Pet, Customer, Product, Service...)
8. **Order** (Master-Detail: Order + OrderDetail + Payment + trừ tồn + ActivityLog)
9. **Appointment** + AppointmentStaff (có checkConflict)
10. **Inventory** (Master-Detail: nhập kho + cộng tồn + log)
11. Hủy đơn + Hoàn tiền (CancelOrderDialog)
12. Điều chỉnh tồn kho (InventoryAdjustmentDialog)

### 🥇 Tuần 3: Customer App
13. Customer login + ForgotPassword (OTP giả lập)
14. Customer Home + Booking (có checkConflict)
15. MyPet + HealthRecord + FavoritePet
16. OrderHistory + BookingHistory + Review
17. Cart + Order online
18. Profile + Notification
19. MyStatistic (biểu đồ chi tiêu)

### 🥇 Tuần 4: Hoàn thiện
20. StatisticActivity (biểu đồ doanh thu, top sản phẩm, tồn thấp)
21. Loading/Error/Empty state cho mọi màn hình
22. Kiểm thử các luồng chính
23. Bảo mật: phân quyền, validate input
24. Ghi ActivityLog ở mọi hành động

---

## Khuyến Nghị

### Priority 1 (Bắt buộc để chạy được)
1. Viết code Entities + DAOs + Database (24 bảng)
2. Viết Repositories
3. Viết ViewModels cho các màn hình chính (Login, Dashboard, Order, Pet, Customer)
4. UI cơ bản (XML layout) cho các form chính
5. Đảm bảo CRUD hoạt động đúng

### Priority 2 (Nên có)
6. Form Master-Detail: Order + OrderDetail + ThanhToán
7. Form Master-Detail: Inventory + InventoryDetail
8. Form Master-Detail: LịchHẹn + NhânViênPhụcVụ
9. Statistic / Report (biểu đồ cột đơn giản)
10. Tích điểm + Phân hạng thành viên
11. Customer App: Đặt lịch, lịch sử, đánh giá
12. Ghi NhậtKýHoạtĐộng tự động

### Priority 3 (Điểm cộng)
13. Phân quyền chi tiết
14. Tìm kiếm nâng cao + Lọc
15. Sao lưu database
16. Mã hóa mật khẩu
17. Thông báo push (FCM)
18. Thanh toán online (VNPay / Momo)

---

## Kết Luận

| Tiêu chí | Đánh giá |
|----------|----------|
| Phân tích nghiệp vụ | ✅ **Rất tốt** - bao phủ bán hàng, nhập kho, lịch hẹn, hủy đơn/hoàn tiền, điều chỉnh tồn, quên MK |
| Thiết kế database | ✅ **Rất tốt** - 24 bảng (+ bảng Chuồng), 33 FK, 10 index, state machine, ActivityLog 16 actions |
| Quy trình nghiệp vụ | ✅ **Đầy đủ** - 8 luồng chính + 3 luồng phụ (hủy đơn, gán NV, điều chỉnh tồn) |
| Xử lý ngoại lệ | ✅ **Đã bổ sung** - xung đột lịch hẹn, hoàn tiền hủy đơn, kiểm tra tồn kho |
| Audit log | ✅ **16 hành động** - bao phủ mọi thao tác quan trọng |
| Task tracking | ✅ **Có** - nhiệm-vụ.md với 5 giai đoạn + tasks cho các quy trình mới |
| Kiến trúc | ✅ **Phù hợp MVVM** - có ViewModel cho từng quy trình |
| Code nguồn | ❌ **Chưa có** - cần triển khai |
| Bảo mật | ⚠️ **Cơ bản** - cần cải thiện nếu production |
| UX / UI | ⚠️ **Cần bổ sung** loading/error/empty state |
| Khả năng mở rộng | ⚠️ **Có thể tốt hơn** - DI, Navigation, testing |
| Phù hợp đồ án | ✅ **Vượt yêu cầu** - 34 form, 5 QT, 24 ĐT, 4 Master-Detail, 2 Report, audit log, quên MK, điều chỉnh tồn, quản lý chuồng |

> **Tổng thể:** Thiết kế đã hoàn thiện gần như tất cả các quy trình thực tế của cửa hàng thú cưng. Điểm yếu duy nhất là chưa có code nguồn. Cần tập trung code đúng thiết kế, xử lý tốt các state và đảm bảo các luồng: bán hàng→thanh toán→log, nhập kho→cộng tồn, đặt lịch→gán NV, hủy đơn→trả tồn→hoàn tiền chạy trơn tru.
