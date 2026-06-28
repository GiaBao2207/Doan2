# Thiết Kế Cơ Sở Dữ Liệu (Bảng Tiếng Việt)

## Danh sách bảng (26 bảng)

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

### 4 bảng bổ sung (đợt 1)

| STT | Tên bảng (Tiếng Việt) | Tên gốc | Mô tả |
|-----|----------------------|---------|-------|
| 21 | **ThanhToán** | Payment | Giao dịch thanh toán cho đơn hàng |
| 22 | **NhậtKýHoạtĐộng** | ActivityLog | Ghi log hành động người dùng |
| 23 | **NhânViênPhụcVụ** | AppointmentStaff | Gán nhân viên phục vụ cho lịch hẹn |
| 24 | **Chuồng** | Cage | Quản lý chuồng nhốt thú cưng trong cửa hàng |

### 2 bảng bổ sung (đợt 2)

| STT | Tên bảng (Tiếng Việt) | Tên gốc | Mô tả |
|-----|----------------------|---------|-------|
| 25 | **BảoHànhThúCưng** | PetWarranty | Bảo hành sức khỏe thú cưng sau khi mua (7-30 ngày) |
| 26 | **HợpĐồngMuaBán** | PurchaseContract | Hợp đồng mua bán thú cưng (giấy tờ pháp lý) |

---

## Chi tiết 6 bảng bổ sung

> **Lưu ý:** Cấu trúc chi tiết của 6 bảng này đã được viết dưới dạng SQL CREATE TABLE ở phần trên.
> Phần này chỉ giữ lại mô tả nghiệp vụ và ràng buộc quan trọng.

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

### 24. Chuồng (Cage)

**Mục đích:** Quản lý vị trí nhốt thú cưng trong cửa hàng. Mỗi chuồng được gán mã riêng, phân khu vực, kích thước, phù hợp với từng loại thú cưng.

**Cấu trúc:**

```
┌──────────────────────────────────────────┐
│              Chuồng                        │
├──────────────────────────────────────────┤
│ chuồngId (PK, int, AUTOINCREMENT)         │
│ maChuồng (nvarchar(20), NOT NULL, UNIQUE) │
│   → Mã chuồng: "A-01", "B-12", "C-05"   │
│ khuVuc (nvarchar(10))                     │
│   → 'A', 'B', 'C' — khu vực trong shop   │
│ kichThuoc (nvarchar(10), DEFAULT 'medium')│
│   → 'small', 'medium', 'large'            │
│ loaiThuCungId (FK → LoạiThúCưng)          │
│   → Chuồng phù hợp với loại thú nào       │
│ trangThai (nvarchar(20), DEFAULT 'empty') │
│   → 'empty', 'occupied', 'maintenance',   │
│     'cleaning'                            │
│ ghiChu (nvarchar(500))                    │
│ createdAt (datetime, DEFAULT NOW)          │
└──────────────────────────────────────────┘
```

**Ràng buộc:**
- UNIQUE(`maChuong`) — mỗi chuồng có mã riêng
- Một thú cưng chỉ được ở 1 chuồng (quản lý qua `chuongId` trong bảng ThúCưng)

---

### 25. BảoHànhThúCưng (PetWarranty)

**Mục đích:** Quản lý bảo hành sức khỏe cho thú cưng sau khi mua. Thời gian bảo hành 7-30 ngày tùy loại thú và chính sách cửa hàng.

**Cấu trúc:**

```
┌──────────────────────────────────────────┐
│           BảoHànhThúCưng                   │
├──────────────────────────────────────────┤
│ baoHanhId (PK, int, AUTOINCREMENT)        │
│ thuCungId (FK → ThúCưng, NOT NULL)        │
│   → Thú cưng được bảo hành                │
│ donHangId (FK → ĐơnHàng, NOT NULL)        │
│   → Đơn hàng mua thú cưng                 │
│ ngayBatDau (datetime, NOT NULL)           │
│   → Ngày bắt đầu bảo hành                 │
│ ngayKetThuc (datetime, NOT NULL)          │
│   → Ngày kết thúc bảo hành                │
│ soNgayBaoHanh (int, NOT NULL,             │
│               DEFAULT 30, CHECK 7-90)     │
│ trangThai (nvarchar(20),                  │
│           DEFAULT 'active')               │
│   → 'active', 'expired', 'claimed',       │
│     'cancelled'                           │
│ ghiChu (nvarchar(500))                    │
│   → Ghi chú khiếu nại nếu có              │
│ createdAt (datetime, DEFAULT NOW)          │
└──────────────────────────────────────────┘
```

**Ràng buộc:**
- `soNgayBaoHanh` BETWEEN 7 AND 90
- `ngayKetThuc` >= `ngayBatDau`
- Một thú cưng chỉ có 1 bảo hành active tại 1 thời điểm

**Nghiệp vụ thực tế:**
```
Ví dụ: Khách mua chó Poodle ngày 01/06
  → BảoHànhThúCưng(thuCungId=5, donHangId=12,
       ngayBatDau='2026-06-01', ngayKetThuc='2026-07-01',
       soNgayBaoHanh=30, trangThai='active')

  Sau 2 tuần, thú bị bệnh → Khách khiếu nại:
  → Cập nhật trangThai='claimed', ghiChu='Thú bị viêm da'
  → Staff kiểm tra, lập HồSơSứcKhỏe cho lần khám

  Sau 30 ngày:
  → Hệ thống (hoặc Staff) cập nhật trangThai='expired'
```

---

### 26. HợpĐồngMuaBán (PurchaseContract)

**Mục đích:** Lưu trữ hợp đồng mua bán thú cưng — giấy tờ pháp lý quan trọng khi giao dịch thú cưng. Gồm thông tin bên mua, bên bán, thú cưng, giá cả, điều khoản bảo hành.

**Cấu trúc:**

```
┌──────────────────────────────────────────┐
│           HợpĐồngMuaBán                    │
├──────────────────────────────────────────┤
│ hopDongId (PK, int, AUTOINCREMENT)        │
│ maHopDong (nvarchar(50), NOT NULL,        │
│           UNIQUE)                         │
│   → Mã hợp đồng: "HD-PET-2026-00001"     │
│ thuCungId (FK → ThúCưng, NOT NULL)        │
│   → Thú cưng được mua bán                 │
│ khachHangId (FK → KháchHàng, NOT NULL)    │
│   → Người mua                              │
│ nhanVienId (FK → NgườiDùng, NOT NULL)     │
│   → Nhân viên bán hàng                    │
│ donHangId (FK → ĐơnHàng)                  │
│   → Đơn hàng liên quan                    │
│ giaBan (REAL, NOT NULL, CHECK > 0)        │
│   → Giá bán thú cưng                      │
│ ngayLap (datetime, NOT NULL)              │
│   → Ngày lập hợp đồng                     │
│ noiDung (nvarchar(2000))                  │
│   → Mô tả chi tiết giao dịch              │
│ dieuKhoan (nvarchar(2000))                │
│   → Điều khoản bảo hành, đổi trả          │
│ trangThai (nvarchar(20),                  │
│           DEFAULT 'active')               │
│   → 'active', 'completed', 'cancelled'    │
│ fileDinhKem (nvarchar(500))               │
│   → Đường dẫn file PDF (nếu có)           │
│ createdAt (datetime, DEFAULT NOW)          │
└──────────────────────────────────────────┘
```

**Ràng buộc:**
- UNIQUE(`maHopDong`)
- `giaBan` > 0
- Mỗi thú cưng chỉ có 1 hợp đồng active

**Nghiệp vụ thực tế:**
```
Ví dụ: Khách Nguyễn Văn A mua chó Poodle giá 5.000.000₫
  → HợpĐồngMuaBán(
       maHopDong='HD-PET-2026-00001',
       thuCungId=5, khachHangId=1, nhanVienId=2,
       donHangId=12, giaBan=5000000,
       ngayLap='2026-06-01',
       dieuKhoan='Bảo hành sức khỏe 30 ngày. Được đổi trả trong 7 ngày nếu phát hiện bệnh lý có sẵn.',
       trangThai='active')
```

---

## SQL CREATE TABLE — 26 bảng

> Dùng **SQLite** (Room).  
> `INTEGER PRIMARY KEY AUTOINCREMENT` = ID tự tăng.  
> `TEXT` thay cho NVARCHAR.  
> `REAL` thay cho DECIMAL.  
> `CHECK`, `DEFAULT`, `UNIQUE` viết sẵn trong câu lệnh.

---

### Nhóm 1: Danh mục (Catalog)

```sql
-- 1. LoạiThúCưng (PetCategory)
CREATE TABLE LoaiThuCung (
    loaiThuCungId  INTEGER PRIMARY KEY AUTOINCREMENT,
    name           TEXT    NOT NULL,
    description    TEXT
);

-- 2. LoạiSảnPhẩm (ProductCategory)
CREATE TABLE LoaiSanPham (
    loaiSanPhamId  INTEGER PRIMARY KEY AUTOINCREMENT,
    name           TEXT    NOT NULL,
    description    TEXT
);

-- 3. DịchVụ (Service)
CREATE TABLE DichVu (
    dichVuId     INTEGER PRIMARY KEY AUTOINCREMENT,
    name         TEXT    NOT NULL,
    description  TEXT,
    price        REAL    NOT NULL,
    duration     INTEGER,          -- phút
    createdAt    TEXT    DEFAULT (datetime('now','localtime'))
);

-- 4. NhàCungCấp (Supplier)
CREATE TABLE NhaCungCap (
    nhaCungCapId INTEGER PRIMARY KEY AUTOINCREMENT,
    name         TEXT    NOT NULL,
    phone        TEXT,
    email        TEXT,
    address      TEXT,
    createdAt    TEXT    DEFAULT (datetime('now','localtime'))
);

-- 5. SảnPhẩm (Product)
CREATE TABLE SanPham (
    sanPhamId         INTEGER PRIMARY KEY AUTOINCREMENT,
    name              TEXT    NOT NULL,
    loaiSanPhamId     INTEGER REFERENCES LoaiSanPham(loaiSanPhamId),
    nhaCungCapId      INTEGER REFERENCES NhaCungCap(nhaCungCapId),
    price             REAL    NOT NULL,
    costPrice         REAL,
    quantity          INTEGER NOT NULL DEFAULT 0,
    unit              TEXT,
    minStockLevel     INTEGER DEFAULT 0,
    expiryDate        TEXT,
    batchNumber       TEXT,
    description       TEXT,
    createdAt         TEXT    DEFAULT (datetime('now','localtime'))
);

-- 6. NgườiDùng (User)
CREATE TABLE NguoiDung (
    nguoiDungId INTEGER PRIMARY KEY AUTOINCREMENT,
    username    TEXT    NOT NULL UNIQUE,
    password    TEXT    NOT NULL,
    fullName    TEXT    NOT NULL,
    role        TEXT    NOT NULL CHECK (role IN ('Admin','Staff')),
    phone       TEXT,
    createdAt   TEXT    DEFAULT (datetime('now','localtime'))
);

-- 7. KháchHàng (Customer)
CREATE TABLE KhachHang (
    khachHangId     INTEGER PRIMARY KEY AUTOINCREMENT,
    phone           TEXT    NOT NULL UNIQUE,
    email           TEXT,
    fullName        TEXT    NOT NULL,
    password        TEXT    NOT NULL,
    membershipTier  TEXT    DEFAULT 'Bronze'
                             CHECK (membershipTier IN ('Bronze','Silver','Gold','Diamond')),
    loyaltyPoints   INTEGER DEFAULT 0,
    emailVerified   INTEGER DEFAULT 0,   -- 0=false, 1=true
    avatar          TEXT,
    createdAt       TEXT    DEFAULT (datetime('now','localtime'))
);
```

---

### Nhóm 2: Giao dịch (Transaction)

```sql
-- 8. ĐơnHàng (Order)
CREATE TABLE DonHang (
    donHangId       INTEGER PRIMARY KEY AUTOINCREMENT,
    khachHangId     INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    nguoiDungId     INTEGER NOT NULL REFERENCES NguoiDung(nguoiDungId),
    orderDate       TEXT    NOT NULL DEFAULT (datetime('now','localtime')),
    totalAmount     REAL    NOT NULL,
    discount        REAL    DEFAULT 0,
    status          TEXT    DEFAULT 'pending'
                              CHECK (status IN ('pending','paid','shipping','completed','cancelled','refunded')),
    notes           TEXT,
    shippingAddress TEXT,
    deliveryStatus  TEXT
);

-- State machine: DonHang.status
--   [draft] ──(xác nhận)──► [pending] ──(thanh toán)──► [paid]
--                                                          │
--                     ┌────────────────────────────────────┤
--                     ▼                                    ▼
--               [shipping] ──(giao xong)──► [completed]   [refunded] ◄──(hoàn tiền)
--                     │                                      ▲
--                     └──(hủy)──► [cancelled] ──(hoàn tiền)──┘

-- 9. ChiTiếtĐơnHàng (OrderDetail)
CREATE TABLE ChiTietDonHang (
    chiTietDonHangId INTEGER PRIMARY KEY AUTOINCREMENT,
    donHangId        INTEGER NOT NULL REFERENCES DonHang(donHangId),
    sanPhamId        INTEGER NOT NULL REFERENCES SanPham(sanPhamId),
    quantity         INTEGER NOT NULL CHECK (quantity > 0),
    unitPrice        REAL    NOT NULL,
    subtotal         REAL    NOT NULL
);

-- 10. PhiếuNhậpKho (Inventory)
CREATE TABLE PhieuNhapKho (
    phieuNhapId    INTEGER PRIMARY KEY AUTOINCREMENT,
    nhaCungCapId   INTEGER NOT NULL REFERENCES NhaCungCap(nhaCungCapId),
    nguoiDungId    INTEGER NOT NULL REFERENCES NguoiDung(nguoiDungId),
    entryDate      TEXT    NOT NULL DEFAULT (datetime('now','localtime')),
    totalCost      REAL    NOT NULL,
    note           TEXT
);

-- 11. ChiTiếtPhiếuNhập (InventoryDetail)
CREATE TABLE ChiTietPhieuNhap (
    chiTietPhieuNhapId INTEGER PRIMARY KEY AUTOINCREMENT,
    phieuNhapId        INTEGER NOT NULL REFERENCES PhieuNhapKho(phieuNhapId),
    sanPhamId          INTEGER NOT NULL REFERENCES SanPham(sanPhamId),
    quantity           INTEGER NOT NULL CHECK (quantity > 0),
    unitCost           REAL    NOT NULL,
    subtotal           REAL    NOT NULL
);

-- 12. LịchHẹn (Appointment)
CREATE TABLE LichHen (
    lichHenId    INTEGER PRIMARY KEY AUTOINCREMENT,
    thuCungId    INTEGER NOT NULL REFERENCES ThuCung(thuCungId),
    khachHangId  INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    dichVuId     INTEGER NOT NULL REFERENCES DichVu(dichVuId),
    date         TEXT    NOT NULL,
    timeSlot     TEXT    NOT NULL,
    status       TEXT    DEFAULT 'pending'
                         CHECK (status IN ('pending','confirmed','checked_in','done','cancelled')),
    notes        TEXT,
    createdAt    TEXT    DEFAULT (datetime('now','localtime'))
);

-- State machine: LichHen.status
--   [pending] ──(xác nhận)──► [confirmed] ──(check-in)──► [checked_in] ──(hoàn tất)──► [done]
--       │                                                      │
--       └──(hủy)──► [cancelled]   ◄────────────────────────────┘

-- 13. NhânViênPhụcVụ (AppointmentStaff)
CREATE TABLE NhanVienPhucVu (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    lichHenId        INTEGER NOT NULL REFERENCES LichHen(lichHenId),
    nhanVienId       INTEGER NOT NULL REFERENCES NguoiDung(nguoiDungId),
    vaiTro           TEXT    NOT NULL,
    ghiChu           TEXT,
    trangThai        TEXT    NOT NULL DEFAULT 'du_kien'
                              CHECK (trangThai IN ('du_kien','co_mat','dang_lam_viec','hoan_thanh','huy')),
    thoiGianBatDau   TEXT,
    thoiGianKetThuc  TEXT,
    createdAt        TEXT    DEFAULT (datetime('now','localtime')),
    UNIQUE(lichHenId, nhanVienId)
);

-- 14. KhuyếnMãi (Promotion)
CREATE TABLE KhuyenMai (
    khuyenMaiId     INTEGER PRIMARY KEY AUTOINCREMENT,
    code            TEXT    NOT NULL UNIQUE,
    description     TEXT,
    discountPercent REAL    NOT NULL CHECK (discountPercent > 0 AND discountPercent <= 100),
    startDate       TEXT    NOT NULL,
    endDate         TEXT    NOT NULL,
    minOrderValue   REAL,
    status          TEXT    DEFAULT 'active' CHECK (status IN ('active','expired','disabled')),
    createdAt       TEXT    DEFAULT (datetime('now','localtime'))
);
```

---

### Nhóm 3: Khách hàng & Hỗ trợ (Customer & Support)

```sql
-- 15. GiỏHàng (Cart)
CREATE TABLE GioHang (
    gioHangId    INTEGER PRIMARY KEY AUTOINCREMENT,
    khachHangId  INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    sanPhamId    INTEGER NOT NULL REFERENCES SanPham(sanPhamId),
    quantity     INTEGER NOT NULL CHECK (quantity > 0),
    addedAt      TEXT    DEFAULT (datetime('now','localtime'))
);

-- 16. ThúCưngYêuThích (FavoritePet)
CREATE TABLE ThuCungYeuThich (
    yeuThichId   INTEGER PRIMARY KEY AUTOINCREMENT,
    khachHangId  INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    thuCungId    INTEGER NOT NULL REFERENCES ThuCung(thuCungId),
    createdAt    TEXT    DEFAULT (datetime('now','localtime'))
);

-- 17. HồSơSứcKhỏe (PetHealthRecord)
CREATE TABLE HoSoSucKhoe (
    recordId     INTEGER PRIMARY KEY AUTOINCREMENT,
    thuCungId    INTEGER NOT NULL REFERENCES ThuCung(thuCungId),
    recordDate   TEXT    NOT NULL,
    recordType   TEXT    NOT NULL CHECK (recordType IN ('vaccine','checkup','treatment')),
    description  TEXT,
    vetName      TEXT,
    nextDueDate  TEXT,
    createdAt    TEXT    DEFAULT (datetime('now','localtime'))
);

-- 18. ĐiểmThưởng (LoyaltyPoint)
CREATE TABLE DiemThuong (
    diemId         INTEGER PRIMARY KEY AUTOINCREMENT,
    khachHangId    INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    diem           INTEGER NOT NULL,
    loai           TEXT    NOT NULL CHECK (loai IN ('earn','burn')),
    referenceType  TEXT,
    referenceId    INTEGER,
    description    TEXT,
    createdAt      TEXT    DEFAULT (datetime('now','localtime'))
);

-- 19. ĐánhGiá (Review)
CREATE TABLE DanhGia (
    danhGiaId    INTEGER PRIMARY KEY AUTOINCREMENT,
    khachHangId  INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    dichVuId     INTEGER NOT NULL REFERENCES DichVu(dichVuId),
    donHangId    INTEGER REFERENCES DonHang(donHangId),
    rating       INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment      TEXT    NOT NULL,
    status       TEXT    DEFAULT 'shown' CHECK (status IN ('shown','hidden')),
    createdAt    TEXT    DEFAULT (datetime('now','localtime'))
);

-- 20. ThôngBáoKhách (CustomerNotification)
CREATE TABLE ThongBaoKhach (
    thongBaoId   INTEGER PRIMARY KEY AUTOINCREMENT,
    khachHangId  INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    tieuDe       TEXT    NOT NULL,
    noiDung      TEXT    NOT NULL,
    loai         TEXT,
    daDoc        INTEGER DEFAULT 0,    -- 0=false, 1=true
    createdAt    TEXT    DEFAULT (datetime('now','localtime'))
);

-- 21. ThúCưng (Pet)
CREATE TABLE ThuCung (
    thuCungId      INTEGER PRIMARY KEY AUTOINCREMENT,
    name           TEXT    NOT NULL,
    breed          TEXT,
    age            INTEGER,
    weight         REAL,
    color          TEXT,
    price          REAL,
    status         TEXT    DEFAULT 'available'
                           CHECK (status IN ('available','sold','grooming','treatment','boarding')),
    microchipId    TEXT,
    chuongId       INTEGER REFERENCES Chuong(chuongId),
    loaiThuCungId  INTEGER REFERENCES LoaiThuCung(loaiThuCungId),
    khachHangId    INTEGER REFERENCES KhachHang(khachHangId),
    createdAt      TEXT    DEFAULT (datetime('now','localtime'))
);
```

---

### Nhóm 4: Bổ sung mới (New) — 6 bảng

```sql
-- 22. ThanhToán (Payment)
-- soTien > 0: ghi nhận thanh toán; soTien < 0: ghi nhận hoàn tiền (refund)
CREATE TABLE ThanhToan (
    thanhToanId   INTEGER PRIMARY KEY AUTOINCREMENT,
    donHangId     INTEGER NOT NULL REFERENCES DonHang(donHangId),
    soTien        REAL    NOT NULL CHECK (soTien != 0),
    phuongThuc    TEXT    NOT NULL
                          CHECK (phuongThuc IN ('tien_mat','chuyen_khoan','momo','vnpay','the_tin_dung')),
    maGiaoDich    TEXT,
    trangThai     TEXT    NOT NULL DEFAULT 'thanh_cong'
                          CHECK (trangThai IN ('cho_xu_ly','thanh_cong','that_bai','hoan_tien','hoan_tien_mot_phan')),
    ghiChu        TEXT,
    nguoiTaoId    INTEGER REFERENCES NguoiDung(nguoiDungId),
    thanhToanLuc  TEXT    NOT NULL DEFAULT (datetime('now','localtime')),
    createdAt     TEXT    DEFAULT (datetime('now','localtime'))
);

-- 23. NhậtKýHoạtĐộng (ActivityLog)
CREATE TABLE NhatKyHoatDong (
    logId        INTEGER PRIMARY KEY AUTOINCREMENT,
    nguoiDungId  INTEGER REFERENCES NguoiDung(nguoiDungId),
    khachHangId  INTEGER REFERENCES KhachHang(khachHangId),
    hanhDong     TEXT    NOT NULL,
    doiTuong     TEXT    NOT NULL,
    doiTuongId   INTEGER NOT NULL,
    chiTiet      TEXT,               -- JSON
    thietBi      TEXT,
    thoiGian     TEXT    NOT NULL DEFAULT (datetime('now','localtime'))
);

-- Danh sách hành động (hanhDong) đầy đủ:
--   DANG_NHAP, DANG_XUAT, TAO_DON, HUY_DON, HOAN_TIEN, THANH_TOAN,
--   NHAP_KHO, DIEU_CHINH_TON, TAO_LICH, HUY_LICH, GAN_NV,
--   THEM_SAN_PHAM, SUA_GIA, XOA_DU_LIEU, DANH_GIA, DOI_MAT_KHAU,
--   BAO_HANH, KY_HOP_DONG

-- 24. Chuồng (Cage) — quản lý vị trí nhốt thú trong cửa hàng
CREATE TABLE Chuong (
    chuongId         INTEGER PRIMARY KEY AUTOINCREMENT,
    maChuong         TEXT    NOT NULL UNIQUE,      -- mã chuồng: "A-01", "B-12"
    khuVuc           TEXT,                          -- khu vực: "A", "B", "C"
    kichThuoc        TEXT    DEFAULT 'medium'       -- nhỏ/vừa/lớn
                           CHECK (kichThuoc IN ('small','medium','large')),
    loaiThuCungId    INTEGER REFERENCES LoaiThuCung(loaiThuCungId),  -- phù hợp loại nào
    trangThai        TEXT    DEFAULT 'empty'
                           CHECK (trangThai IN ('empty','occupied','maintenance','cleaning')),
    ghiChu           TEXT,
    createdAt        TEXT    DEFAULT (datetime('now','localtime'))
);

-- 25. BảoHànhThúCưng (Pet Warranty) — bảo hành sức khỏe sau khi mua
CREATE TABLE BaoHanhThuCung (
    baoHanhId      INTEGER PRIMARY KEY AUTOINCREMENT,
    thuCungId      INTEGER NOT NULL REFERENCES ThuCung(thuCungId),
    donHangId      INTEGER NOT NULL REFERENCES DonHang(donHangId),
    ngayBatDau     TEXT    NOT NULL,
    ngayKetThuc    TEXT    NOT NULL,
    soNgayBaoHanh  INTEGER NOT NULL DEFAULT 30
                           CHECK (soNgayBaoHanh BETWEEN 7 AND 90),
    trangThai      TEXT    NOT NULL DEFAULT 'active'
                           CHECK (trangThai IN ('active','expired','claimed','cancelled')),
    ghiChu         TEXT,
    createdAt      TEXT    DEFAULT (datetime('now','localtime'))
);

-- 26. HợpĐồngMuaBán (Purchase Contract) — hợp đồng mua bán thú cưng
CREATE TABLE HopDongMuaBan (
    hopDongId      INTEGER PRIMARY KEY AUTOINCREMENT,
    maHopDong      TEXT    NOT NULL UNIQUE,
    thuCungId      INTEGER NOT NULL REFERENCES ThuCung(thuCungId),
    khachHangId    INTEGER NOT NULL REFERENCES KhachHang(khachHangId),
    nhanVienId     INTEGER NOT NULL REFERENCES NguoiDung(nguoiDungId),
    donHangId      INTEGER REFERENCES DonHang(donHangId),
    giaBan         REAL    NOT NULL CHECK (giaBan > 0),
    ngayLap        TEXT    NOT NULL,
    noiDung        TEXT,
    dieuKhoan      TEXT,
    trangThai      TEXT    NOT NULL DEFAULT 'active'
                           CHECK (trangThai IN ('active','completed','cancelled')),
    fileDinhKem    TEXT,
    createdAt      TEXT    DEFAULT (datetime('now','localtime'))
);
```

---

## Tổng hợp khóa ngoại

| # | Bảng | FK | Tham chiếu |
|---|------|----|-----------|
| 5 | SanPham | loaiSanPhamId | LoaiSanPham(loaiSanPhamId) |
| 5 | SanPham | nhaCungCapId | NhaCungCap(nhaCungCapId) |
| 8 | DonHang | khachHangId | KhachHang(khachHangId) |
| 8 | DonHang | nguoiDungId | NguoiDung(nguoiDungId) |
| 9 | ChiTietDonHang | donHangId | DonHang(donHangId) |
| 9 | ChiTietDonHang | sanPhamId | SanPham(sanPhamId) |
| 10 | PhieuNhapKho | nhaCungCapId | NhaCungCap(nhaCungCapId) |
| 10 | PhieuNhapKho | nguoiDungId | NguoiDung(nguoiDungId) |
| 11 | ChiTietPhieuNhap | phieuNhapId | PhieuNhapKho(phieuNhapId) |
| 11 | ChiTietPhieuNhap | sanPhamId | SanPham(sanPhamId) |
| 12 | LichHen | thuCungId | ThuCung(thuCungId) |
| 12 | LichHen | khachHangId | KhachHang(khachHangId) |
| 12 | LichHen | dichVuId | DichVu(dichVuId) |
| 13 | NhanVienPhucVu | lichHenId | LichHen(lichHenId) |
| 13 | NhanVienPhucVu | nhanVienId | NguoiDung(nguoiDungId) |
| 15 | GioHang | khachHangId | KhachHang(khachHangId) |
| 15 | GioHang | sanPhamId | SanPham(sanPhamId) |
| 16 | ThuCungYeuThich | khachHangId | KhachHang(khachHangId) |
| 16 | ThuCungYeuThich | thuCungId | ThuCung(thuCungId) |
| 17 | HoSoSucKhoe | thuCungId | ThuCung(thuCungId) |
| 18 | DiemThuong | khachHangId | KhachHang(khachHangId) |
| 19 | DanhGia | khachHangId | KhachHang(khachHangId) |
| 19 | DanhGia | dichVuId | DichVu(dichVuId) |
| 19 | DanhGia | donHangId | DonHang(donHangId) |
| 20 | ThongBaoKhach | khachHangId | KhachHang(khachHangId) |
| 21 | ThuCung | loaiThuCungId | LoaiThuCung(loaiThuCungId) |
| 21 | ThuCung | khachHangId | KhachHang(khachHangId) |
| 21 | ThuCung | chuongId | Chuong(chuongId) |
| 22 | ThanhToan | donHangId | DonHang(donHangId) |
| 22 | ThanhToan | nguoiTaoId | NguoiDung(nguoiDungId) |
| 23 | NhatKyHoatDong | nguoiDungId | NguoiDung(nguoiDungId) |
| 23 | NhatKyHoatDong | khachHangId | KhachHang(khachHangId) |
| 24 | Chuong | loaiThuCungId | LoaiThuCung(loaiThuCungId) |
| 25 | BaoHanhThuCung | thuCungId | ThuCung(thuCungId) |
| 25 | BaoHanhThuCung | donHangId | DonHang(donHangId) |
| 26 | HopDongMuaBan | thuCungId | ThuCung(thuCungId) |
| 26 | HopDongMuaBan | khachHangId | KhachHang(khachHangId) |
| 26 | HopDongMuaBan | nhanVienId | NguoiDung(nguoiDungId) |
| 26 | HopDongMuaBan | donHangId | DonHang(donHangId) |

---

## Quan hệ Master-Detail

| Master | Detail | Liên kết |
|--------|--------|----------|
| DonHang (1) | ChiTietDonHang (N) | donHangId |
| PhieuNhapKho (1) | ChiTietPhieuNhap (N) | phieuNhapId |
| LichHen (1) | NhanVienPhucVu (N) | lichHenId |
| DonHang (1) | ThanhToan (N) | donHangId |

---

## Chỉ mục (Index) đề xuất

```sql
-- Tìm kiếm nhanh
CREATE INDEX idx_sanpham_name ON SanPham(name);
CREATE INDEX idx_khachhang_phone ON KhachHang(phone);
CREATE INDEX idx_donhang_orderDate ON DonHang(orderDate);
CREATE INDEX idx_donhang_khachHangId ON DonHang(khachHangId);
CREATE INDEX idx_lichhen_date ON LichHen(date);
CREATE INDEX idx_lichhen_khachHangId ON LichHen(khachHangId);
CREATE INDEX idx_thucung_khachHangId ON ThuCung(khachHangId);
CREATE INDEX idx_nhatky_thoiGian ON NhatKyHoatDong(thoiGian);
CREATE INDEX idx_nhatky_hanhDong ON NhatKyHoatDong(hanhDong);
CREATE INDEX idx_thanhtoan_donHangId ON ThanhToan(donHangId);
CREATE INDEX idx_baohanh_thuCungId ON BaoHanhThuCung(thuCungId);
CREATE INDEX idx_hopdong_maHopDong ON HopDongMuaBan(maHopDong);
```

---

---

## Biểu đồ Use Case tổng thể

### Actors (Tác nhân)

```
┌─────────────────────────────────────────────────────────────────┐
│                        HỆ THỐNG PETSTORE                         │
│                                                                   │
│   ┌─────────────┐    ┌──────────────┐    ┌──────────────────┐   │
│   │   ADMIN     │    │    STAFF     │    │   CUSTOMER       │   │
│   │ (Quản trị)  │    │  (Nhân viên) │    │   (Khách hàng)   │   │
│   └──────┬──────┘    └──────┬───────┘    └────────┬─────────┘   │
│          │                  │                      │              │
│          └──────────────────┼──────────────────────┘              │
│                             ▼                                     │
│               ┌─────────────────────────┐                        │
│               │      HỆ THỐNG           │                        │
│               │  (Room Database + App)  │                        │
│               └─────────────────────────┘                        │
│                                                                   │
│   Tác nhân phụ:                                                  │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐      │
│   │  NhàCungCấp  │  │    FCM       │  │ VNPay / Momo     │      │
│   │  (Supplier)  │  │ (Push Notif) │  │ (Online Payment) │      │
│   └──────────────┘  └──────────────┘  └──────────────────┘      │
└─────────────────────────────────────────────────────────────────┘
```

---

### 1. Use Case — ADMIN

```
┌──────────────────────────────────────────────────────────────────┐
│                     ADMIN (Quản trị viên)                         │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │                      HỆ THỐNG                           │     │
│  │                                                         │     │
│  │  ┌──────────────────────────────────────────────────┐   │     │
│  │  │              QUẢN LÝ NHÂN SỰ                     │   │     │
│  │  │  ┌──────────┐  ┌──────────┐  ┌──────────────┐   │   │     │
│  │  │  │Thêm NV   │  │Sửa NV    │  │Khóa/Mở khóa  │   │   │     │
│  │  │  └──────────┘  └──────────┘  └──────────────┘   │   │     │
│  │  └──────────────────────────────────────────────────┘   │     │
│  │                                                         │     │
│  │  ┌──────────────────────────────────────────────────┐   │     │
│  │  │              BÁO CÁO & THỐNG KÊ                  │   │     │
│  │  │  ┌──────────┐  ┌──────────┐  ┌──────────────┐   │   │     │
│  │  │  │DT ngày   │  │DT tháng  │  │Top sản phẩm  │   │   │     │
│  │  │  ├──────────┤  ├──────────┤  ├──────────────┤   │   │     │
│  │  │  │Top NV    │  │Tồn kho   │  │Xuất Excel    │   │   │     │
│  │  │  └──────────┘  └──────────┘  └──────────────┘   │   │     │
│  │  └──────────────────────────────────────────────────┘   │     │
│  │                                                         │     │
│  │  ┌──────────────────────────────────────────────────┐   │     │
│  │  │              QUẢN LÝ DANH MỤC                    │   │     │
│  │  │  ┌──────────┐  ┌──────────┐  ┌──────────────┐   │   │     │
│  │  │  │Loại thú  │  │Loại SP   │  │Dịch vụ       │   │   │     │
│  │  │  ├──────────┤  ├──────────┤  ├──────────────┤   │   │     │
│  │  │  │NCC       │  │KM        │  │Xóa dữ liệu   │   │   │     │
│  │  │  └──────────┘  └──────────┘  └──────────────┘   │   │     │
│  │  └──────────────────────────────────────────────────┘   │     │
│  │                                                         │     │
│  │  ┌──────────────────────────────────────────────────┐   │     │
│  │  │              XEM NHẬT KÝ HOẠT ĐỘNG               │   │     │
│  │  │  ┌──────────┐  ┌──────────┐  ┌──────────────┐   │   │     │
│  │  │  │Xem log   │  │Lọc theo  │  │Xem chi tiết  │   │   │     │
│  │  │  │          │  │ngày/NV   │  │              │   │   │     │
│  │  │  └──────────┘  └──────────┘  └──────────────┘   │   │     │
│  │  └──────────────────────────────────────────────────┘   │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  Mô tả: Admin có toàn quyền: quản lý nhân viên, xem mọi báo      │
│  cáo, quản lý danh mục, xem log hoạt động. Không bán hàng.      │
│                                                                   │
│  Luồng chính: Đăng nhập → Dashboard → (Quản lý NV / Báo cáo /   │
│               Danh mục / Nhật ký)                                 │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

### 2. Use Case — STAFF

```
┌──────────────────────────────────────────────────────────────────┐
│                   STAFF (Nhân viên bán hàng)                      │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │                BÁN HÀNG TẠI QUẦY (Quy trình chính)      │     │
│  │                                                         │     │
│  │  ┌──────────┐    ┌──────────┐    ┌──────────────┐      │     │
│  │  │Tìm/chọn  │───►│Thêm SP   │───►│Áp mã KM      │      │     │
│  │  │khách     │    │vào đơn   │    │(nếu có)      │      │     │
│  │  └──────────┘    └──────────┘    └──────┬───────┘      │     │
│  │                                          │               │     │
│  │  ┌───────────────────────────────────────┘               │     │
│  │  ▼                                                        │     │
│  │  ┌──────────┐    ┌──────────┐    ┌──────────────┐      │     │
│  │  │Thanh toán│───►│Lưu đơn   │───►│In hóa đơn    │      │     │
│  │  │          │    │          │    │(PDF/Bluetooth)│      │     │
│  │  └──────────┘    └──────────┘    └──────────────┘      │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              QUẢN LÝ ĐỐI TƯỢNG                          │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Thú cưng  │  │Khách     │  │Sản phẩm  │  │Đơn     │ │     │
│  │  │CRUD      │  │hàng CRUD │  │CRUD + xem│  │hàng    │ │     │
│  │  │          │  │          │  │tồn kho   │  │xem+in  │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              LỊCH HẸN & DỊCH VỤ                         │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Xem lịch  │  │Check-in  │  │Gán NV    │  │Hủy/dời│ │     │
│  │  │hẹn       │  │          │  │phục vụ   │  │lịch   │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              NHẬP KHO (Quy trình phụ)                    │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Chọn NCC  │  │Chọn SP   │  │Nhập SL + │  │Lưu     │ │     │
│  │  │          │  │          │  │giá vốn   │  │phiếu   │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  Mô tả: Staff là role chính, vận hành mọi hoạt động bán hàng     │
│  tại quầy. Không được quản lý nhân viên khác, không xóa dữ liệu.│
│                                                                   │
│  Luồng chính: Đăng nhập → Dashboard → (Bán hàng / Quản lý /     │
│               Lịch hẹn / Nhập kho)                                │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

### 3. Use Case — CUSTOMER

```
┌──────────────────────────────────────────────────────────────────┐
│                   CUSTOMER (Khách hàng online)                    │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              TÀI KHOẢN                                  │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Đăng ký   │  │Đăng nhập │  │Quên MK   │  │Đổi MK  │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              ĐẶT LỊCH HẸN (Quy trình)                   │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Chọn      │  │Chọn thú  │  │Chọn      │  │Xác     │ │     │
│  │  │dịch vụ   │  │cưng      │  │ngày/giờ  │  │nhận    │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              MUA SẮM ONLINE                             │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Xem SP    │  │Thêm vào  │  │Áp mã KM  │  |Đặt     │ │     │
│  │  │dịch vụ   │  │giỏ hàng  │  │          │  |hàng    │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              XEM LỊCH SỬ                                │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Đơn hàng  │  │Lịch hẹn  │  │Chi tiêu  │  │Đánh    │ │     │
│  │  │đã mua    │  │đã đặt    │  │cá nhân   │  │giá DV  │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐     │
│  │              QUẢN LÝ CÁ NHÂN                            │     │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │     │
│  │  │Thú cưng  │  │Hồ sơ     │  │Điểm      │  |Thông   │ │     │
│  │  │của tôi   │  │sức khỏe  │  │thưởng    │  |báo     │ │     │
│  │  └──────────┘  └──────────┘  └──────────┘  └────────┘ │     │
│  └─────────────────────────────────────────────────────────┘     │
│                                                                   │
│  Mô tả: Khách hàng sử dụng Customer App để đặt lịch, mua hàng   │
│  online, xem lịch sử, quản lý thú cưng và tài khoản cá nhân.    │
│                                                                   │
│  Luồng chính: Đăng nhập → Trang chủ → (Đặt lịch / Mua hàng /    │
│               Lịch sử / Cá nhân)                                  │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

### 4. Ma trận Use Case — Bảng

| # | Use Case | Admin | Staff | Customer | Ghi chú |
|---|----------|-------|-------|----------|---------|
| UC01 | Đăng nhập | ✅ | ✅ | ✅ | Riêng Customer dùng SĐT |
| UC02 | Đăng ký tài khoản | ❌ | ❌ | ✅ | Customer tự đăng ký |
| UC03 | Quản lý nhân viên (CRUD) | ✅ | ❌ | ❌ | Admin-only |
| UC04 | Xem Dashboard | ✅ | ✅ | ❌ | Customer có trang chủ riêng |
| UC05 | Quản lý thú cưng (CRUD) | ✅ | ✅ | ✅ | Customer chỉ xem/sửa của mình |
| UC06 | Quản lý khách hàng (CRUD) | ✅ | ✅ | ❌ | |
| UC07 | Quản lý sản phẩm (CRUD) | ✅ | ✅ | ❌ | Customer chỉ xem |
| UC08 | Quản lý dịch vụ (CRUD) | ✅ | ✅ | ❌ | Customer chỉ xem |
| UC09 | Quản lý danh mục | ✅ | ❌ | ❌ | Admin-only |
| UC10 | Quản lý nhà cung cấp (CRUD) | ✅ | ✅ | ❌ | |
| UC11 | Quản lý khuyến mãi (CRUD) | ✅ | ✅ | ❌ | Customer chỉ nhập mã |
| UC12 | **Tạo đơn hàng (QT)** | ❌ | ✅ | ❌ | Bán tại quầy |
| UC13 | Xem danh sách đơn hàng | ✅ | ✅ | ✅ | Riêng Customer xem của mình |
| UC14 | Hủy đơn hàng | ✅ | ✅ | ❌ | |
| UC15 | **Nhập kho (QT)** | ❌ | ✅ | ❌ | |
| UC16 | **Đặt lịch hẹn (QT)** | ❌ | ❌ | ✅ | Customer App |
| UC17 | Xem lịch hẹn | ✅ | ✅ | ✅ | |
| UC18 | Check-in / Xử lý lịch hẹn | ❌ | ✅ | ❌ | |
| UC19 | Gán nhân viên phục vụ | ❌ | ✅ | ❌ | |
| UC20 | Hủy lịch hẹn | ✅ | ✅ | ✅ | Customer: trước 2h |
| UC21 | Thanh toán đơn hàng | ❌ | ✅ | ❌ | Staff thu tiền |
| UC22 | Xem báo cáo thống kê | ✅ | ✅ | ❌ | Admin: tổng quan / Staff: giới hạn |
| UC23 | Xem thống kê cá nhân | ❌ | ❌ | ✅ | Customer xem chi tiêu, DV đã dùng |
| UC24 | Đánh giá dịch vụ | ❌ | ❌ | ✅ | |
| UC25 | Quản lý giỏ hàng | ❌ | ❌ | ✅ | Customer App |
| UC26 | Mua hàng online (Cart→Order) | ❌ | ❌ | ✅ | Customer App |
| UC27 | Quản lý hồ sơ sức khỏe thú cưng | ❌ | ✅ | ✅ | Customer xem, Staff thêm |
| UC28 | Quản lý điểm thưởng / Hạng | ✅ | ✅ | ✅ | Customer xem, hệ thống tự tính |
| UC29 | Xem thông báo | ✅ | ❌ | ✅ | Staff không cần |
| UC30 | In hóa đơn | ❌ | ✅ | ❌ | Qua Bluetooth / PDF |
| UC31 | Xuất báo cáo Excel | ✅ | ❌ | ❌ | Admin-only |
| UC32 | Xem nhật ký hoạt động | ✅ | ❌ | ❌ | Admin-only |

---

### 5. Mô tả chi tiết 3 Use Case quan trọng nhất

#### UC12: Tạo đơn hàng (Quy trình chính)

```
Tên:          Tạo đơn hàng bán tại quầy
Actor:        Staff
Mô tả:        Nhân viên tạo đơn hàng cho khách tại cửa hàng
Tiền điều kiện: Staff đã đăng nhập, có khách hàng
Hậu điều kiện:  Đơn hàng được lưu, tồn kho trừ, log ghi lại

Luồng chính:
  1. Staff bấm [Tạo đơn hàng] → OrderActivity
  2. Tìm/chọn khách hàng (tìm theo SĐT hoặc tên)
  3. Nếu khách mới → thêm khách hàng (CustomerFormActivity)
  4. Thêm sản phẩm vào đơn (dialog chọn SP + nhập số lượng)
     - Kiểm tra tồn kho đủ
     - Tính thành tiền
  5. Nhập mã khuyến mãi (nếu có) → kiểm tra hợp lệ
  6. Chọn phương thức thanh toán
  7. Bấm [Lưu đơn hàng]
     → OrderViewModel.placeOrder()
     → OrderDao.insert() + OrderDetailDao.insertList()
     → ProductDao.updateQuantity() (trừ tồn)
     → ThanhToánDao.insert() (ghi nhận thanh toán)
     → NhậtKýHoạtĐộng (ghi log 'TAO_DON')
  8. Hiển thị: "Đã lưu đơn hàng #ORDxxx"
  9. [In hóa đơn] nếu cần

Luồng rẽ nhánh:
  - 2a. Không tìm thấy khách → chuyển UC06 (thêm khách)
  - 4a. Tồn kho không đủ → thông báo lỗi, yêu cầu nhập lại SL
  - 5a. Mã KM hết hạn/sai → thông báo "Mã không hợp lệ"
  - 7a. Lỗi database → rollback, hiển thị lỗi
```

#### UC16: Đặt lịch hẹn (Quy trình Customer)

```
Tên:          Đặt lịch hẹn dịch vụ
Actor:        Customer
Mô tả:        Khách hàng đặt lịch hẹn cho thú cưng qua app
Tiền điều kiện: Customer đã đăng nhập, có thú cưng trong danh sách
Hậu điều kiện:  Lịch hẹn mới với status='pending', gửi thông báo

Luồng chính:
  1. Customer bấm [Đặt lịch ngay] → BookingActivity
  2. Bước 1: Chọn dịch vụ (danh sách từ ServiceDao)
  3. Bước 2: Chọn thú cưng (danh sách pet của khách)
  4. Bước 3: Chọn ngày + giờ (DatePickerDialog + TimePickerDialog)
     - Kiểm tra không trùng lịch giờ đó
  5. Xác nhận → hiển thị tóm tắt: DV + thú cưng + ngày giờ + giá
  6. Bấm [Xác nhận đặt lịch]
     → AppointmentDao.insert(status='pending')
     → NhậtKýHoạtĐộng (ghi log 'TAO_LICH')
  7. Hiển thị: "✅ Đặt lịch thành công!"
  8. Hệ thống gửi thông báo nhắc lịch (FCM) trước 1 ngày

Luồng rẽ nhánh:
  - 4a. Giờ đã có lịch → thông báo "Khung giờ này đã có lịch"
  - 6a. Lỗi → rollback, hiển thị lỗi
```

#### UC15: Nhập kho (Quy trình phụ)

```
Tên:          Nhập kho sản phẩm
Actor:        Staff
Mô tả:        Nhân viên kho nhập hàng từ nhà cung cấp
Tiền điều kiện: Staff đã đăng nhập
Hậu điều kiện:  Phiếu nhập được lưu, tồn kho tăng, log ghi lại

Luồng chính:
  1. Staff bấm [Nhập kho] → InventoryActivity
  2. Chọn nhà cung cấp (dropdown từ SupplierDao)
  3. Thêm sản phẩm vào phiếu (dialog chọn SP + nhập SL + giá vốn)
  4. Xem danh sách sản phẩm đã thêm + tổng tiền
  5. Nhập ghi chú (nếu có)
  6. Bấm [Lưu phiếu nhập]
     → InventoryDao.insert() + InventoryDetailDao.insertList()
     → ProductDao.updateQuantity() (cộng tồn)
     → NhậtKýHoạtĐộng (ghi log 'NHAP_KHO')
  7. Hiển thị: "✅ Nhập kho thành công. Phiếu #INVxxx"

Luồng rẽ nhánh:
  - 6a. Lỗi database → rollback
```

---
