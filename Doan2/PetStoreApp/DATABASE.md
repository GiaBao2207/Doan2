# Thiết Kế Cơ Sở Dữ Liệu (Bảng Tiếng Việt)

## Danh sách bảng (23 bảng)

### 20 bảng gốc

| STT | Tên cũ | Tên mới (Tiếng Việt) | Mô tả |
|-----|--------|----------------------|-------|
| 1 | User | **NgườiDùng** | Người dùng hệ thống (Admin, Staff) |
| 2 | PetCategory | **LoạiThúCưng** | Loại thú cưng (Chó, Mèo, Hamster) |
| 3 | Pet | **ThúCưng** | Thú cưng |
| 4 | Customer | **KháchHàng** | Khách hàng (có đăng nhập, tích điểm, hạng) |
| 5 | ProductCategory | **LoạiSảnPhẩm** | Loại sản phẩm (Thức ăn, Phụ kiện, Thuốc) |
| 6 | Product | **SảnPhẩm** | Sản phẩm bán |
| 7 | Supplier | **NhàCungCấp** | Nhà cung cấp |
| 8 | Service | **DịchVụ** | Dịch vụ (Tắm, Cắt tỉa, Khám) |
| 9 | Appointment | **LịchHẹn** | Lịch hẹn |
| 10 | Order | **ĐơnHàng** | Đơn hàng / Hóa đơn bán |
| 11 | OrderDetail | **ChiTiếtĐơnHàng** | Chi tiết đơn hàng |
| 12 | Inventory | **PhiếuNhậpKho** | Phiếu nhập kho |
| 13 | InventoryDetail | **ChiTiếtPhiếuNhập** | Chi tiết phiếu nhập |
| 14 | Promotion | **KhuyếnMãi** | Khuyến mãi / Giảm giá |
| 15 | Review | **ĐánhGiá** | Đánh giá dịch vụ |
| 16 | PetHealthRecord | **HồSơSứcKhỏe** | Hồ sơ sức khỏe / Lịch tiêm chủng |
| 17 | LoyaltyPoint | **ĐiểmThưởng** | Lịch sử tích điểm |
| 18 | CustomerNotification | **ThôngBáoKhách** | Thông báo cho khách hàng |
| 19 | Cart | **GiỏHàng** | Giỏ hàng online |
| 20 | FavoritePet | **ThúCưngYêuThích** | Thú cưng yêu thích |

### 3 bảng bổ sung

| STT | Tên bảng (Tiếng Việt) | Tên gốc | Mô tả |
|-----|----------------------|---------|-------|
| 21 | **ThanhToán** | Payment | Giao dịch thanh toán cho đơn hàng |
| 22 | **NhậtKýHoạtĐộng** | ActivityLog | Ghi log hành động người dùng |
| 23 | **NhânViênPhụcVụ** | AppointmentStaff | Gán nhân viên phục vụ cho lịch hẹn |

---

## Chi tiết 3 bảng bổ sung

### 21. ThanhToán (Payment)

**Mục đích:** Quản lý giao dịch thanh toán cho đơn hàng. Một đơn hàng có thể có nhiều giao dịch thanh toán (thanh toán nhiều lần, nhiều phương thức, hoàn tiền một phần).

**Cấu trúc:**

```
┌──────────────────────────────────────────┐
│              ThanhToán                      │
├──────────────────────────────────────────┤
│ thanhToanId (PK, int, AUTOINCREMENT)      │
│ donHangId (FK → ĐơnHàng, NOT NULL)        │
│ soTien (decimal(12,0), NOT NULL)          │
│ phuongThuc (enum, NOT NULL)               │
│   → 'tien_mat', 'chuyen_khoan', 'momo',  │
│     'vnpay', 'the_tin_dung'              │
│ maGiaoDich (nvarchar(100))                │
│   → Mã GD từ VNPay/Momo/ngân hàng         │
│ trangThai (enum, NOT NULL, DEFAULT 'thanh_cong') │
│   → 'cho_xu_ly', 'thanh_cong', 'that_bai',       │
│     'hoan_tien', 'hoan_tien_mot_phan'            │
│ ghiChu (nvarchar(500))                     │
│ nguoiTaoId (FK → NgườiDùng)               │
│   → Ai thu tiền? (staff)                   │
│ thanhToanLuc (datetime, NOT NULL)          │
│ createdAt (datetime, DEFAULT CURRENT_TIMESTAMP) │
└──────────────────────────────────────────┘
```

**Ràng buộc:**
- `soTien` > 0 (CHECK)
- Tổng `soTien` của các giao dịch `'thanh_cong'` không được vượt quá `totalAmount` của ĐơnHàng (xử lý ở code)
- Khi `trangThai = 'hoan_tien'` thì phải có reference đến giao dịch gốc (thêm cột `hoanTuThanhToanId` nếu cần)

**Nghiệp vụ thực tế:**
```
Ví dụ: Khách mua 1,500,000đ
  - Lần 1: Thanh toán 1,000,000đ bằng tiền mặt → ThanhToán(soTien=1tr, phuongThuc='tien_mat')
  - Lần 2: Thanh toán 500,000đ bằng chuyển khoản → ThanhToán(soTien=500k, phuongThuc='chuyen_khoan')
  - Nếu hủy: Hoàn tiền 1,000,000đ → ThanhToán(soTien=-1tr, trangThai='hoan_tien')
```

---

### 22. NhậtKýHoạtĐộng (ActivityLog)

**Mục đích:** Ghi lại toàn bộ hành động quan trọng trong hệ thống. Dữ liệu **append-only** (không cho sửa/xóa). Phục vụ kiểm tra, truy vết, đối chiếu khi có tranh chấp.

**Cấu trúc:**

```
┌──────────────────────────────────────────┐
│           NhậtKýHoạtĐộng                   │
├──────────────────────────────────────────┤
│ logId (PK, int, AUTOINCREMENT)            │
│ nguoiDungId (FK → NgườiDùng)              │
│   → NULL nếu là khách vãng lai            │
│ khachHangId (FK → KháchHàng)              │
│   → NULL nếu là staff/admin               │
│ hanhDong (nvarchar(50), NOT NULL)         │
│   → 'DANG_NHAP', 'TAO_DON', 'HUY_DON',  │
│     'THANH_TOAN', 'NHAP_KHO', 'THEM_THU',│
│     'SUA_GIA', 'XOA_SP', 'DOI_LICH', ... │
│ doiTuong (nvarchar(50), NOT NULL)         │
│   → Tên bảng: 'DonHang', 'SanPham',      │
│     'ThuCung', 'KhachHang', ...          │
│ doiTuongId (int, NOT NULL)                │
│   → ID của bản ghi bị tác động           │
│ chiTiet (nvarchar(2000))                  │
│   → JSON hoặc text mô tả chi tiết         │
│   → Ví dụ: {"oldPrice": 100000,           │
│              "newPrice": 120000}          │
│ thietBi (nvarchar(100))                   │
│   → 'Staff App', 'Customer App', 'Web'   │
│ thoiGian (datetime, NOT NULL,             │
│           DEFAULT CURRENT_TIMESTAMP)      │
└──────────────────────────────────────────┘
```

**Ràng buộc:**
- `logId` là AUTOINCREMENT và **không bao giờ được UPDATE hoặc DELETE** (áp dụng ở code)
- `hanhDong` + `doiTuong` phải hợp lệ (có thể dùng CHECK hoặc validate ở code)
- `thoiGian` mặc định là thời điểm INSERT, không cho phép truyền vào

**Các hành động cần log (tối thiểu):**

| Module | Hành động cần log |
|--------|-------------------|
| **Auth** | ĐĂNG_NHẬP, ĐĂNG_XUẤT, ĐĂNG_KÝ, ĐỔI_MẬT_KHẨU |
| **Đơn hàng** | TẠO_ĐƠN, CẬP_NHẬT_ĐƠN, HỦY_ĐƠN, THANH_TOÁN |
| **Sản phẩm** | THÊM_SP, SỬA_SP, XÓA_SP, SỬA_GIÁ, NHẬP_KHO |
| **Thú cưng** | THÊM_THÚ, SỬA_THÚ, XÓA_THÚ, BÁN_THÚ |
| **Khách hàng** | THÊM_KH, SỬA_KH, XÓA_KH, TÍCH_ĐIỂM, ĐỔI_HẠNG |
| **Lịch hẹn** | TẠO_LỊCH, SỬA_LỊCH, HỦY_LỊCH, CHECK_IN |
| **Nhân viên** | THÊM_NV, KHÓA_NV, PHÂN_QUYỀN |

**Nghiệp vụ thực tế:**
```
Ví dụ 1: Staff A tạo đơn hàng #ORD125 cho khách Nguyễn Văn C
  → NhậtKýHoạtĐộng(nguoiDungId=2, hanhDong='TAO_DON',
       doiTuong='DonHang', doiTuongId=125,
       chiTiet='{"customer":"Nguyen Van C","total":801000}')

Ví dụ 2: Khách hàng đổi mật khẩu
  → NhậtKýHoạtĐộng(khachHangId=5, hanhDong='DOI_MAT_KHAU',
       doiTuong='KhachHang', doiTuongId=5)
```

---

### 23. NhânViênPhụcVụ (AppointmentStaff)

**Mục đích:** Gán nhân viên phục vụ cho từng lịch hẹn. Một lịch hẹn có thể cần nhiều nhân viên (VD: 1 người tắm, 1 người cắt tỉa). Giúp tính năng suất và theo dõi công việc của từng nhân viên.

**Cấu trúc:**

```
┌──────────────────────────────────────────┐
│           NhânViênPhụcVụ                   │
├──────────────────────────────────────────┤
│ id (PK, int, AUTOINCREMENT)               │
│ lichHenId (FK → LịchHẹn, NOT NULL)        │
│ nhanVienId (FK → NgườiDùng, NOT NULL)     │
│   → Chỉ user có role = 'Staff'            │
│ vaiTro (nvarchar(100), NOT NULL)          │
│   → 'Chính', 'Phụ', hoặc tên công việc:  │
│     'Tắm', 'Cắt tỉa', 'Khám chính',      │
│     'Trợ lý', 'Tiêm'                     │
│ ghiChu (nvarchar(500))                    │
│ trangThai (enum, NOT NULL,               │
│           DEFAULT 'du_kien')              │
│   → 'du_kien', 'co_mat', 'dang_lam_viec',│
│     'hoan_thanh', 'huy'                  │
│ thoiGianBatDau (datetime)                 │
│   → Thời gian bắt đầu thực tế             │
│ thoiGianKetThuc (datetime)                │
│   → Thời gian kết thúc thực tế            │
│ createdAt (datetime, DEFAULT CURRENT_TIMESTAMP) │
└──────────────────────────────────────────┘
```

**Ràng buộc:**
- UNIQUE(`lichHenId`, `nhanVienId`) — một nhân viên chỉ được gán 1 lần cho 1 lịch hẹn
- `nhanVienId` phải là user có `role = 'Staff'` (kiểm tra ở code)
- `thoiGianKetThuc` > `thoiGianBatDau` (nếu có)

**Nghiệp vụ thực tế:**
```
Ví dụ 1: Lịch hẹn tắm + cắt tỉa cho "Cún Miu" lúc 09:00
  → NhânViênPhụcVụ(lichHenId=1024, nhanVienId=3,
       vaiTro='Tắm', trangThai='du_kien')
  → NhânViênPhụcVụ(lichHenId=1024, nhanVienId=5,
       vaiTro='Cắt tỉa', trangThai='du_kien')

Ví dụ 2: Check-in lịch hẹn
  → Khi staff bấm [Check-in], cập nhật:
     thoiGianBatDau = NOW(), trangThai = 'dang_lam_viec'

Ví dụ 3: Hoàn thành
  → thoiGianKetThuc = NOW(), trangThai = 'hoan_thanh'
  → Tính năng suất: làm trong X phút
```

---

## Sơ đồ quan hệ tổng thể (ERD)

```
┌───────────────┐       ┌──────────────────┐      ┌───────────────────┐
│   NgườiDùng   │       │   LoạiSảnPhẩm   │      │   LoạiThúCưng    │
├───────────────┤       ├──────────────────┤      ├───────────────────┤
│ nguoiDungId   │◄────┐ │ loaiSanPhamId   │      │ loaiThuCungId    │
│ username      │     │ │ name             │      │ name              │
│ password      │     │ │ description      │      │ description       │
│ fullName      │     │ └──────────────────┘      └────────┬──────────┘
│ role          │     │                                    │
│ phone         │     │                                    │
│ createdAt     │     │                                    │
└───────┬───────┘     │   ┌──────────────┐                │
        │             │   │ NhàCungCấp  │                │
        │             │   ├──────────────┤                │
        │             │   │ nhaCungCapId │                │
        │             │   │ name         │                │
        │◄─────┐      │   │ phone        │                │
        │      │      │   │ email        │                │
  NhânViênPhụcVụ    │   │ address      │                │
  ├──────────────    │   │ createdAt    │                │
  │ lichHenId (FK)   │   └──────┬───────┘                │
  │ nhanVienId (FK)──┘          │                        │
  │ vaiTro                      │                        │
  │ trangThai          ┌────────┴────────────────┐       │
  └──────────────      │        SảnPhẩm          │       │
                       ├────────────────────────-┤       │
        ┌──────────────┤ sanPhamId               │       │
        │              │ name                     │       │
  NhậtKýHoạtĐộng      │ loaiSanPhamId (FK) ──────┤       │
  ├──────────────      │ nhaCungCapId (FK) ───────┤       │
  │ nguoiDungId (FK)───┤ price, costPrice        │       │
  │ khachHangId (FK)   │ quantity, unit           │       │
  │ hanhDong           │ minStockLevel            │       │
  │ doiTuong           │ expiryDate, batchNumber  │       │
  │ doiTuongId         │ createdAt                │       │
  └──────────────      └──────────────────────────┘       │
                                                          │
        ┌─────────────────────────────────────────────────┘
        │
        ▼
┌──────────────────┐
│     ThúCưng      │
├──────────────────┤
│ thuCungId        │
│ name             │
│ breed            │
│ age, weight      │
│ color, price     │
│ status           │
│ microchipId      │
│ loaiThuCungId(FK)│
│ khachHangId (FK) │
│ createdAt        │
└──────────────────┘

Các quan hệ còn lại (ĐơnHàng, ChiTiếtĐơnHàng, LịchHẹn, DịchVụ, ...)
giữ nguyên như thiết kế gốc, thêm:
  - LịchHẹn có thêm FK → NhânViênPhụcVụ
  - ĐơnHàng có thêm FK → ThanhToán
```

---

## Ràng buộc toàn vẹn (cập nhật)

### Khóa ngoại bổ sung cho 3 bảng mới

| Bảng (VN) | FK | Tham chiếu |
|---|---|---|
| **ThanhToán** | donHangId | ĐơnHàng(donHangId) |
| **ThanhToán** | nguoiTaoId | NgườiDùng(nguoiDungId) |
| **NhậtKýHoạtĐộng** | nguoiDungId | NgườiDùng(nguoiDungId) |
| **NhậtKýHoạtĐộng** | khachHangId | KháchHàng(khachHangId) |
| **NhânViênPhụcVụ** | lichHenId | LịchHẹn(lichHenId) |
| **NhânViênPhụcVụ** | nhanVienId | NgườiDùng(nguoiDungId) |

### Ràng buộc NOT NULL bổ sung

- **ThanhToán:** soTien, phuongThuc, trangThai, thanhToanLuc
- **NhậtKýHoạtĐộng:** hanhDong, doiTuong, doiTuongId, thoiGian
- **NhânViênPhụcVụ:** lichHenId, nhanVienId, vaiTro, trangThai

### Ràng buộc DEFAULT & CHECK bổ sung

- ThanhToán.trangThai DEFAULT 'thanh_cong'
- ThanhToán.soTien CHECK (> 0) — cho phép âm khi hoàn tiền
- ThanhToán.phuongThuc IN ('tien_mat', 'chuyen_khoan', 'momo', 'vnpay', 'the_tin_dung')
- NhânViênPhụcVụ.trangThai IN ('du_kien', 'co_mat', 'dang_lam_viec', 'hoan_thanh', 'huy')

---

## Mối quan hệ Master-Detail (cập nhật)

### 3. ThanhToán (Detail) ← ĐơnHàng (Master)

```
ĐơnHàng (Master)                 ThanhToán (Detail)
┌────────────────────┐           ┌─────────────────────────────┐
│ donHangId (PK)     │◄──────────│ donHangId (FK)              │
│ totalAmount: 1.5tr │   1 : N   │ soTien: 1tr + 0.5tr          │
│ status: paid       │           │ phuongThuc: tien_mat + ck   │
└────────────────────┘           │ trangThai: thanh_cong       │
                                  └─────────────────────────────┘
```

### 4. NhânViênPhụcVụ (Detail) ← LịchHẹn (Master)

```
LịchHẹn (Master)                 NhânViênPhụcVụ (Detail)
┌────────────────────┐           ┌─────────────────────────────┐
│ lichHenId (PK)     │◄──────────│ lichHenId (FK)              │
│ dịch vụ: Tắm+Cắt  │   1 : N   │ NV1: vaiTro='Tắm'          │
│ giờ: 09:00         │           │ NV2: vaiTro='Cắt tỉa'      │
└────────────────────┘           └─────────────────────────────┘
```

---

## Tổng kết sau khi bổ sung

| Tiêu chí | Trước | Sau |
|---|---|---|
| Tổng số bảng | 20 | **23** |
| Master-Detail | 2 (Order, Inventory) | **4** (thêm ThanhToán, NhânViênPhụcVụ) |
| Bảng thanh toán | ❌ Không có | ✅ Có: trace được từng giao dịch, hoàn tiền |
| Audit log | ❌ Không có | ✅ Có: biết ai làm gì, lúc nào |
| Gán nhân viên lịch hẹn | ❌ Không có | ✅ Có: biết ai phục vụ, tính năng suất |
