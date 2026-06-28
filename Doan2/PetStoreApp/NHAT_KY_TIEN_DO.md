# NHẬT KÝ TIẾN ĐỘ DỰ ÁN: PETSTOREAPP

> **Dự án:** Quản Lý Cửa Hàng Thú Cưng  
> **Mục tiêu:** Xây dựng ứng dụng Android quản lý cửa hàng thú cưng với **2 app riêng** (Staff + Customer) dùng chung database, kiến trúc **MVVM**, ngôn ngữ **Java**, **Room database**.

---

## LỘ TRÌNH CÁC GIAI ĐOẠN

| Giai đoạn | Nội dung công việc | Trạng thái | Ghi chú |
| :--- | :--- | :--- | :--- |
| **Phần 1** | **Thiết kế Database (26 bảng SQL)** | ✅ Hoàn thành | 26 bảng, 39 FK, 12 index, state machine |
| **Phần 2** | **Thiết kế Chức năng theo MVVM** | ✅ Hoàn thành | 26 ĐT, 5 QT, 36 form, 23 VM, 21 Repos |
| **Phần 3** | **Thiết kế Luồng nghiệp vụ & User flow** | ✅ Hoàn thành | 9 luồng, 6 rule, 18 ActivityLog actions |
| **Phần 4** | **Code: Entity + DAO + Database** | ⏳ Chưa bắt đầu | 26 Entity, 26 DAO, AppDatabase |
| **Phần 5** | **Code: Repository + ViewModel** | ⏳ Chưa bắt đầu | 21 Repository, 23 ViewModel |
| **Phần 6** | **Code: Staff App UI (23 màn hình)** | ⏳ Chưa bắt đầu | Login → Dashboard → CRUD → Order → Appointment → Inventory → Warranty → Contract |
| **Phần 7** | **Code: Customer App UI (13 màn hình)** | ⏳ Chưa bắt đầu | Login → Home → Booking → History → Statistic → Review → Cart |
| **Phần 8** | **Kiểm thử & Hoàn thiện** | ⏳ Chưa bắt đầu | Test 9 luồng, loading/error/empty state, bảo mật |

---

## TỔNG QUAN THIẾT KẾ

### Database: 26 bảng

| Nhóm | Bảng | Ghi chú |
|------|------|---------|
| **20 bảng gốc** | NgườiDùng, LoạiThúCưng, ThúCưng, KháchHàng, LoạiSảnPhẩm, SảnPhẩm, NhàCungCấp, DịchVụ, LịchHẹn, ĐơnHàng, ChiTiếtĐơnHàng, PhiếuNhậpKho, ChiTiếtPhiếuNhập, KhuyếnMãi, ĐánhGiá, HồSơSứcKhỏe, ĐiểmThưởng, ThôngBáoKhách, GiỏHàng, ThúCưngYêuThích | 20 |
| **Đợt 1 (4 bảng)** | ThanhToán, NhậtKýHoạtĐộng, NhânViênPhụcVụ, Chuồng | +4 |
| **Đợt 2 (2 bảng)** | BảoHànhThúCưng, HợpĐồngMuaBán | +2 |

### Kiến trúc MVVM

```
UI (Activity/Fragment)
    ↓ action (click, nhập liệu)
ViewModel
    ↓ gọi LiveData
Repository
    ↓ DAO methods
Room Database (SQLite)
```

### 2 App chung DB

```
┌──────────────┐    ┌──────────────┐
│  STAFF APP   │    │ CUSTOMER APP │
│  (23 màn hình│    │ (13 màn hình)│
│   + 23 VM)   │    │  + 0 VM mới) │
└──────┬───────┘    └──────┬───────┘
       └────────┬──────────┘
                ▼
      ┌──────────────────┐
      │ Room DB (26 bảng) │
      └──────────────────┘
```

---

## CHI TIẾT CÁC GIAI ĐOẠN

### Phần 1: Thiết kế Database

| STT | Nội dung | File |
|-----|----------|------|
| 1 | Danh sách 26 bảng (20 gốc + 4 đợt 1 + 2 đợt 2) | DATABASE.md |
| 2 | SQL CREATE TABLE đầy đủ (PK, FK, CHECK, DEFAULT, UNIQUE) | DATABASE.md |
| 3 | 39 khóa ngoại + 12 index | DATABASE.md |
| 4 | State machine: DonHang (6 trạng thái), LichHen (5 trạng thái) | DATABASE.md |
| 5 | ActivityLog: 18 hành động append-only | DATABASE.md |
| 6 | 4 Master-Detail (Order, Inventory, Payment, AppointmentStaff) | DATABASE.md |

### Phần 2: Thiết kế Chức năng MVVM

| STT | Thành phần | Số lượng |
|-----|-----------|---------|
| 1 | Entity class | 26 |
| 2 | DAO interface | 26 |
| 3 | Repository | 21 |
| 4 | ViewModel | 23 |
| 5 | Staff UI screens | 23 |
| 6 | Customer UI screens | 13 |
| 7 | **Tổng form** | **36** |

### Phần 3: Thiết kế Luồng nghiệp vụ

| Luồng | Mô tả |
|-------|-------|
| Luồng 1 | Bán hàng (Quy trình chính) |
| Luồng 2 | Nhập kho (Quy trình phụ) |
| Luồng 3 | Khách hàng đặt lịch online |
| Luồng 4 | Khách hàng xem lịch sử & thống kê |
| Luồng 5 | Đánh giá dịch vụ |
| Luồng 6 | Hủy đơn hàng & Hoàn tiền |
| Luồng 7 | Gán nhân viên phục vụ |
| Luồng 8 | Điều chỉnh tồn kho |
| Luồng 9 | Bán thú cưng có bảo hành & hợp đồng |

---

## NHẬT KÝ CHI TIẾT CÁC HOẠT ĐỘNG

### Ngày 28/06/2026

* **Hoạt động 1: Khởi tạo dự án + đọc tài liệu gốc**
  * *Chi tiết:* Đọc toàn bộ 6 file tài liệu gốc (Thangdiem.xlsx, các file .md cũ), tạo implementation-notes.md và nhiệm-vụ.md làm khung quản lý.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 2: Đổi tên 20 bảng + thêm 3 bảng mới**
  * *Chi tiết:* Đổi tên toàn bộ 20 bảng từ tiếng Anh sang tiếng Việt không dấu (User→NgườiDùng, Pet→ThúCưng...). Thêm 3 bảng: ThanhToán (Payment), NhậtKýHoạtĐộng (ActivityLog), NhânViênPhụcVụ (AppointmentStaff).
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 3: Viết ERD + Use Case**
  * *Chi tiết:* Vẽ ERD đầy đủ 23 bảng với tất cả thuộc tính + PK/FK. Thêm biểu đồ Use Case với 32 use case phân theo 3 role (Admin/Staff/Customer).
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 4: Chuyển ERD sang SQL CREATE TABLE**
  * *Chi tiết:* Chuyển toàn bộ ERD sang câu lệnh SQL CREATE TABLE sẵn sàng copy chạy (SQLite/Room). Thêm 10 index, dọn sạch phần ERD cũ bị trùng.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 5: Fix database + thêm state machine**
  * *Chi tiết:* Fix ThanhToán CHECK từ `soTien > 0` → `soTien != 0` hỗ trợ số âm hoàn tiền. Thêm state machine: DonHang (6 trạng thái), LichHen (5 trạng thái). Thêm danh sách 16 hành động ActivityLog.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 6: Viết luồng nghiệp vụ (Luồng 1-5)**
  * *Chi tiết:* Viết Luồng 1 (Bán hàng), Luồng 2 (Nhập kho), Luồng 3 (Đặt lịch online), Luồng 4 (Lịch sử & thống kê), Luồng 5 (Đánh giá dịch vụ). Kèm MVVM flow và ActivityLog cho mỗi luồng.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 7: Thêm Luồng 6-8 (xử lý real-world)**
  * *Chi tiết:* Thêm Luồng 6 (Hủy đơn + Hoàn tiền), Luồng 7 (Gán nhân viên + auto-assign), Luồng 8 (Điều chỉnh tồn kho). Xử lý xung đột lịch hẹn (checkConflict).
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 8: Viết VD-LUONG-NGUOI-DUNG + quên MK + hủy lịch**
  * *Chi tiết:* Viết 5 rule với ASCII mockup chi tiết. Thêm ForgotPasswordActivity (OTP), CancelBookingDialog, ActivityLog đăng nhập cho cả 3 role.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 9: Cập nhật CHUC-NANG.md + README.md + nhiệm-vụ.md**
  * *Chi tiết:* CHUC-NANG.md: QT 4→5, form 29→33, ViewModel 20→21, fix numbering. README.md: fix tên SQL, thêm package structure. nhiệm-vụ.md: thêm tasks cho hủy đơn, xung đột, quên MK.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 10: Thêm bảng Chuồng (Cage)**
  * *Chi tiết:* Thêm bảng Chuồng (Chuong): maChuong UNIQUE, khuVuc, kichThuoc, loaiThuCungId FK, trangThai (empty/occupied/maintenance/cleaning). Thêm chuongId FK trong ThuCung + status 'boarding'. Nâng tổng số bảng lên 24.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 11: Viết lại README.md hoàn chỉnh**
  * *Chi tiết:* Viết lại toàn bộ README.md: giới thiệu hệ thống (3 role), công nghệ (Java, MVVM, Room, LiveData, Hilt, MPAndroidChart, ZXing, FCM), 8 nhóm nghiệp vụ, package structure cập nhật, 24-table reference, bảng tổng kết đồ án.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 12: Thêm bảo hành thú cưng + hợp đồng mua bán**
  * *Chi tiết:* Thêm 2 bảng: BảoHànhThúCưng (PetWarranty, CHECK soNgayBaoHanh BETWEEN 7 AND 90), HợpĐồngMuaBán (PurchaseContract, maHopDong UNIQUE, giaBan CHECK>0, dieuKhoan, fileDinhKem). Nâng 24→26 bảng, FK 33→39, index 10→12, ActivityLog 16→18 actions.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 13: Cập nhật toàn bộ file sau khi thêm 2 bảng**
  * *Chi tiết:* CHUC-NANG.md: 26 ĐT, 26 DAO, 21 Repos, 23 VM, 36 form. LUONG-THUC-TE.md: thêm Luồng 9 (Bán thú cưng + Bảo hành + Hợp đồng). VD-LUONG-NGUOI-DUNG.md: thêm Rule 6 (2 luồng con). nhiệm-vụ.md: cập nhật counts. implementation-notes.md: changelog +6 mục.
  * *Trạng thái:* **Hoàn thành**

* **Hoạt động 14: Đồng bộ số liệu + thêm giờ VN cho changelog**
  * *Chi tiết:* Sửa tất cả số liệu cũ trong README.md (4 chỗ), implementation-notes.md (5 chỗ), LUONG-THUC-TE.md (bảng ActivityLog thiếu 2 actions). Thêm giờ Việt Nam (HH:MM) cho 30 entries trong changelog.
  * *Trạng thái:* **Hoàn thành**

---

## TÓM TẮT QUYẾT ĐỊNH THIẾT KẾ

| Quyết định | Chi tiết |
|------------|----------|
| **Ngôn ngữ** | Java |
| **Kiến trúc** | MVVM (Model-View-ViewModel) |
| **Database** | Room (SQLite) |
| **Async** | LiveData / AsyncTask |
| **UI Binding** | findViewById + RecyclerView.Adapter |
| **State Management** | LiveData + ViewModel |
| **DI** | Hilt / ServiceLocator |
| **Chart** | MPAndroidChart |
| **QR/Barcode** | ZXing |
| **Notification** | Firebase Cloud Messaging |
| **Tên bảng** | Tiếng Việt không dấu, PascalCase |
| **ActivityLog** | Append-only, 18 hành động |
| **Thanh toán** | Hỗ trợ số âm hoàn tiền |
| **Bảo hành** | 7-90 ngày, CHECK ràng buộc |

---

## TÀI LIỆU THAM KHẢO

- [README.md](./README.md) - Thông tin tổng quan dự án
- [DATABASE.md](./DATABASE.md) - Thiết kế database 26 bảng
- [CHUC-NANG.md](./CHUC-NANG.md) - Danh sách chức năng theo MVVM
- [LUONG-THUC-TE.md](./LUONG-THUC-TE.md) - Mô tả 9 luồng nghiệp vụ
- [VD-LUONG-NGUOI-DUNG.md](./VD-LUONG-NGUOI-DUNG.md) - Ví dụ chi tiết 6 rule
- [implementation-notes.md](./implementation-notes.md) - Nhận xét, đánh giá tổng thể
- [nhiệm-vụ.md](./nhiệm-vụ.md) - Task list chi tiết

---

*Nhật ký này sẽ được cập nhật liên tục qua mỗi bước làm việc.*
