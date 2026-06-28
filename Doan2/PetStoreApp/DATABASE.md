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

## Sơ đồ quan hệ tổng thể (ERD) — 23 bảng

### Phần 1: Danh mục & Đối tượng chính

```
┌──────────────────┐      ┌───────────────────┐      ┌──────────────────┐
│   LoạiThúCưng   │      │   LoạiSảnPhẩm    │      │   DịchVụ        │
├──────────────────┤      ├───────────────────┤      ├──────────────────┤
│ loaiThuCungId PK │      │ loaiSanPhamId PK │      │ dichVuId PK     │
│ name             │      │ name              │      │ name             │
│ description      │      │ description       │      │ description      │
└────────┬─────────┘      └────────┬──────────┘      │ price            │
         │                         │                  │ duration         │
         │                         │                  │ createdAt        │
         │                         │                  └──────────────────┘
         │                         │
         ▼                         ▼
┌─────────────────────────────────────────────────────┐
│                    ThúCưng                          │
├─────────────────────────────────────────────────────┤
│ thuCungId PK      │  SảnPhẩm                       │
│ name              ├─────────────────────────────────┤
│ breed             │ sanPhamId PK                    │
│ age               │ name                            │
│ weight            │ loaiSanPhamId FK ───────────────┘
│ color             │ nhaCungCapId FK ───────────────┐
│ price             │ price, costPrice                │
│ status            │ quantity, unit, minStockLevel   │
│ microchipId       │ expiryDate, batchNumber         │
│ loaiThuCungId FK──│ createdAt                       │
│ khachHangId FK ───┼──────────────┐                  │
│ createdAt         │              │                  │
└───────────────────┘              │                  │
                                   │                  │
                      ┌────────────┴──────────┐       │
                      │     NhàCungCấp        │       │
                      ├───────────────────────┤       │
                      │ nhaCungCapId PK       │       │
                      │ name, phone, email    │       │
                      │ address, createdAt    │◄──────┘
                      └───────────────────────┘

┌───────────────────┐      ┌───────────────────────┐
│    NgườiDùng      │      │     KháchHàng         │
├───────────────────┤      ├───────────────────────┤
│ nguoiDungId PK    │      │ khachHangId PK        │
│ username (UNIQUE) │      │ fullName              │
│ password (hashed) │      │ phone (UNIQUE)        │
│ fullName          │      │ email                 │
│ role (Admin/Staff)│      │ password (hashed)     │
│ phone             │      │ membershipTier        │
│ createdAt         │      │ loyaltyPoints         │
└───────────────────┘      │ emailVerified         │
                            │ avatar                │
                            │ createdAt             │
                            └───────────────────────┘
```

### Phần 2: Giao dịch Master-Detail

```
┌───────────────────────┐      ┌──────────────────────────┐
│      ĐơnHàng          │      │    PhiếuNhậpKho         │
├───────────────────────┤      ├──────────────────────────┤
│ donHangId PK          │      │ phieuNhapId PK           │
│ khachHangId FK ───────┼──┐   │ nhaCungCapId FK ────────┤
│ nguoiDungId FK ───────┼──┤   │ nguoiDungId FK ─────────┤
│ orderDate             │  │   │ entryDate                │
│ totalAmount           │  │   │ totalCost                │
│ discount              │  │   │ note                     │
│ status                │  │   └────────────┬─────────────┘
│ notes                 │  │                │
│ shippingAddress       │  │                │ 1:N
│ deliveryStatus        │  │                │
└───────────┬───────────┘  │                ▼
            │ 1:N          │   ┌──────────────────────────┐
            ▼               │   │  ChiTiếtPhiếuNhập      │
┌───────────────────────┐   │   ├──────────────────────────┤
│   ChiTiếtĐơnHàng      │   │   │ chiTietPhieuNhapId PK   │
├───────────────────────┤   │   │ phieuNhapId FK ──────────┤
│ chiTietDonHangId PK   │   │   │ sanPhamId FK ───────────┤
│ donHangId FK ─────────┘   │   │ quantity                 │
│ sanPhamId FK ─────────┐   │   │ unitCost                 │
│ quantity               │   │   │ subtotal                 │
│ unitPrice              │   │   └──────────────────────────┘
│ subtotal               │   │
│                        │   │
│ (Master: ĐơnHàng)      │   │  (Master: PhiếuNhậpKho)
└────────────────────────┘   └──────────────────────────────

┌───────────────────────┐      ┌──────────────────────────┐
│      LịchHẹn          │      │       KhuyếnMãi          │
├───────────────────────┤      ├──────────────────────────┤
│ lichHenId PK          │      │ khuyenMaiId PK           │
│ thuCungId FK ─────────┤      │ code (UNIQUE)            │
│ khachHangId FK ───────┤      │ description              │
│ dichVuId FK ──────────┤      │ discountPercent          │
│ date, timeSlot        │      │ startDate, endDate       │
│ status                │      │ minOrderValue            │
│ notes                 │      │ status                   │
│ createdAt             │      │ createdAt                │
└───────────┬───────────┘      └──────────────────────────┘
            │ 1:N
            ▼
┌──────────────────────────┐
│    NhânViênPhụcVụ        │
├──────────────────────────┤
│ id PK                    │
│ lichHenId FK ────────────┘
│ nhanVienId FK ───────────┐
│ vaiTro                   │
│ trangThai                │
│ thoiGianBatDau           │
│ thoiGianKetThuc          │
│ createdAt                │
└──────────────────────────┘
```

### Phần 3: Hỗ trợ & Bổ sung mới

```
┌───────────────────────┐      ┌──────────────────────────┐
│      GiỏHàng          │      │    ThúCưngYêuThích       │
├───────────────────────┤      ├──────────────────────────┤
│ gioHangId PK          │      │ yeuThichId PK            │
│ khachHangId FK ───────┤      │ khachHangId FK ──────────┤
│ sanPhamId FK ─────────┤      │ thuCungId FK ────────────┤
│ quantity              │      │ createdAt                │
│ addedAt               │      └──────────────────────────┘
└───────────────────────┘

┌───────────────────────┐      ┌──────────────────────────┐
│    HồSơSứcKhỏe        │      │     ĐiểmThưởng           │
├───────────────────────┤      ├──────────────────────────┤
│ recordId PK           │      │ diemId PK                │
│ thuCungId FK ─────────┤      │ khachHangId FK ──────────┤
│ recordDate            │      │ diem (int)               │
│ recordType            │      │ loai (earn/burn)         │
│ description           │      │ referenceType            │
│ vetName               │      │ referenceId              │
│ nextDueDate           │      │ description              │
│ createdAt             │      │ createdAt                │
└───────────────────────┘      └──────────────────────────┘

┌───────────────────────┐      ┌──────────────────────────┐
│     ĐánhGiá           │      │   ThôngBáoKhách          │
├───────────────────────┤      ├──────────────────────────┤
│ danhGiaId PK          │      │ thongBaoId PK            │
│ khachHangId FK ───────┤      │ khachHangId FK ──────────┤
│ dichVuId FK ──────────┤      │ tieuDe                   │
│ donHangId FK ─────────┤      │ noiDung                  │
│ rating (1-5)          │      │ loai                     │
│ comment               │      │ daDoc (bool)             │
│ createdAt             │      │ createdAt                │
│ status                │      └──────────────────────────┘
└───────────────────────┘

┌──────────────────────────┐      ┌──────────────────────────┐
│       ThanhToán          │      │    NhậtKýHoạtĐộng       │
├──────────────────────────┤      ├──────────────────────────┤
│ thanhToanId PK           │      │ logId PK                 │
│ donHangId FK ────────────┤      │ nguoiDungId FK ──────────┤
│ soTien                   │      │ khachHangId FK ──────────┤
│ phuongThuc               │      │ hanhDong                 │
│ maGiaoDich               │      │ doiTuong                 │
│ trangThai                │      │ doiTuongId               │
│ ghiChu                   │      │ chiTiet (JSON)           │
│ nguoiTaoId FK ───────────┘      │ thietBi                  │
│ thanhToanLuc              │      │ thoiGian                 │
│ createdAt                 │      │ createdAt                │
└───────────────────────────┘      └──────────────────────────┘
```

### Tổng hợp quan hệ khóa ngoại

```
                          KháchHàng
                         ┌──────────┐
                         │          │
     ┌───────────────────┤  KH      ├───────────────────────┐
     │                   │   PK     │                       │
     │                   └────┬─────┘                      │
     │                        │                            │
     ▼                        ▼                            ▼
 ┌───────┐  ┌───────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
 │ThúCưng│  │ĐơnHàng│  │LịchHẹn   │  │ĐánhGiá   │  │GiỏHàng   │
 │KH=FK  │  │KH=FK  │  │KH=FK     │  │KH=FK     │  │KH=FK     │
 └───────┘  └───────┘  └──────────┘  └──────────┘  └──────────┘
 ┌───────┐  ┌───────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
 │YêuThic│  │Điểm   │  │ThôngBáo  │  │NhậtKý   │  │          │
 │KH=FK  │  │KH=FK  │  │KH=FK     │  │KH=FK     │  │          │
 └───────┘  └───────┘  └──────────┘  └──────────┘  └──────────┘

                          NgườiDùng
                         ┌──────────┐
                         │          │
     ┌───────────────────┤   ND     ├───────────────────────┐
     │                   │   PK     │                       │
     │                   └────┬─────┘                      │
     │                        │                            │
     ▼                        ▼                            ▼
 ┌───────┐  ┌───────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
 │ĐơnHàng│  │Thanh  │  │PhiếuNhap│  │NhậtKý   │  │NhânViên │
 │ND=FK  │  │Toán   │  │ND=FK    │  │ND=FK    │  │PhụcVụ   │
 └───────┘  │ND=FK  │  └──────────┘  └──────────┘  │ND=FK    │
            └───────┘                              └──────────┘
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
