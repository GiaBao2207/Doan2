# PetStoreApp - Hệ Thống Quản Lý Cửa Hàng Thú Cưng

## Giới thiệu

**PetStoreApp** là ứng dụng Android quản lý cửa hàng thú cưng (Pet Store Management) dành cho:
- **Nhân viên (Staff)**: quản lý bán hàng, nhập kho, lịch hẹn, khách hàng, thú cưng
- **Quản trị viên (Admin)**: quản lý nhân viên, xem báo cáo, giám sát hoạt động
- **Khách hàng (Customer)**: đặt lịch online, mua hàng, xem lịch sử, đánh giá dịch vụ

Ứng dụng được xây dựng bằng **Java**, kiến trúc **MVVM**, cơ sở dữ liệu **Room (SQLite)**.

---

## Công Nghệ Sử Dụng

| STT | Công nghệ / Thư viện | Mục đích |
|-----|---------------------|----------|
| 1 | **Java** | Ngôn ngữ lập trình chính (tương thích Android, dễ bảo trì) |
| 2 | **Android SDK (API 26+)** | Xây dựng ứng dụng Android, tối thiểu Android 8.0 |
| 3 | **MVVM Architecture** | Mô hình kiến trúc: Model - View - ViewModel, phân tách rõ ràng |
| 4 | **Room Database** | Lớp abstraction trên SQLite: CRUD an toàn, truy vấn mạnh mẽ |
| 5 | **LiveData** | Quan sát dữ liệu, tự động cập nhật UI khi data thay đổi |
| 6 | **ViewModel** | Lưu trữ dữ liệu xoay vòng màn hình, không gọi lại API |
| 7 | **RecyclerView** | Hiển thị danh sách hiệu quả, hỗ trợ tìm kiếm và lọc |
| 8 | **Material Design 3** | Giao diện hiện đại, theme động, component chuẩn |
| 9 | **Navigation Component** | Quản lý luồng màn hình, pass dữ liệu an toàn |
| 10 | **Hilt / Dagger** | Dependency Injection, quản lý instance toàn cục |
| 11 | **MPAndroidChart** | Biểu đồ doanh thu, thống kê chi tiêu (cột, đường, tròn) |
| 12 | **ZXing** | Mã QR/Barcode cho sản phẩm, thú cưng |
| 13 | **Firebase Cloud Messaging** | Push notification: nhắc lịch hẹn, khuyến mãi |

---

## Kiến Trúc Hệ Thống

```
┌──────────────────────────────────────────────────────────────┐
│                     PETSTORE APP                               │
│                                                                │
│  ┌─────────────────────┐  ┌─────────────────────┐           │
│  │   STAFF APP          │  │   CUSTOMER APP      │           │
│  │   (Nhân viên)        │  │   (Khách hàng)      │           │
│  │                      │  │                      │           │
│  │  • Bán hàng tại quầy │  │  • Đặt lịch online   │           │
│  │  • Quản lý kho       │  │  • Mua hàng online   │           │
│  │  • Quản lý lịch hẹn  │  │  • Xem lịch sử       │           │
│  │  • Quản lý thú cưng  │  │  • Đánh giá dịch vụ  │           │
│  │  • Báo cáo thống kê  │  │  • Hồ sơ thú cưng    │           │
│  │  • Quản lý chuồng    │  │  • Tích điểm, hạng   │           │
│  └─────────┬────────────┘  └─────────┬────────────┘           │
│            │                          │                        │
│            └──────────┬───────────────┘                        │
│                       ▼                                        │
│          ┌──────────────────────────┐                         │
│          │   Room Database (SQLite)  │                         │
│          │   24 bảng dùng chung      │                         │
│          └──────────────────────────┘                         │
└──────────────────────────────────────────────────────────────┘
```

### Luồng MVVM điển hình

```
UI (Activity/Fragment)
    │  action (click, nhập liệu)
    ▼
ViewModel
    │  gọi LiveData
    ▼
Repository
    │  DAO methods
    ▼
Room Database (SQLite)
```

---

## Nghiệp Vụ Hệ Thống

### 1. Quản lý bán hàng (Quy trình chính)
- Tạo đơn hàng: chọn khách → thêm sản phẩm → nhập số lượng → áp mã KM → thanh toán
- Kiểm tra tồn kho trước khi lưu
- Hỗ trợ nhiều phương thức thanh toán (tiền mặt, chuyển khoản, Momo, VNPay)
- Tự động trừ tồn kho khi lưu đơn
- Ghi nhận giao dịch thanh toán (một đơn có thể thanh toán nhiều lần)
- In hóa đơn (PDF / Bluetooth printer)
- **Hủy đơn + Hoàn tiền**: hủy đơn → trả lại tồn kho → ghi hoàn tiền (số âm)

### 2. Quản lý nhập kho
- Tạo phiếu nhập: chọn nhà cung cấp → thêm sản phẩm → nhập SL + giá
- Tự động cộng tồn kho khi lưu phiếu
- **Điều chỉnh tồn kho**: xử lý hàng hỏng, hết hạn, trả NCC, kiểm kê

### 3. Quản lý dịch vụ & Lịch hẹn
- Đặt lịch hẹn (khám, tắm, cắt tỉa, tiêm phòng)
- Kiểm tra xung đột: không cho đặt trùng thú cưng + khung giờ
- Check-in / Check-out lịch hẹn
- Gán nhân viên phục vụ (một lịch có thể gán nhiều NV với vai trò khác nhau)
- Khách hàng hủy lịch (hủy trước 2h miễn phí)

### 4. Quản lý thú cưng
- Thú cưng mua bán (thuộc cửa hàng, status = 'available')
- Thú cưng của khách (thuộc khách hàng, dùng cho lịch hẹn)
- **Quản lý chuồng**: theo dõi thú đang ở chuồng nào, tình trạng chuồng (trống/bận/vệ sinh)
- **Bảo hành thú cưng**: tự động tạo bảo hành 7-30 ngày khi bán thú, xử lý khiếu nại
- **Hợp đồng mua bán**: tạo hợp đồng pháp lý khi giao dịch, lưu điều khoản, in PDF
- Hồ sơ sức khỏe: lịch tiêm, tái khám, điều trị
- Theo dõi thú cưng yêu thích

### 5. Quản lý khách hàng & Tích điểm
- Đăng ký / Đăng nhập bằng SĐT
- Quên mật khẩu (OTP qua SMS)
- Phân hạng thành viên: Bronze → Silver → Gold → Diamond
- Tích điểm khi mua hàng, đánh giá dịch vụ

### 6. Quản lý sản phẩm & Khuyến mãi
- CRUD sản phẩm theo danh mục, nhà cung cấp
- Mã giảm giá: kiểm tra hạn dùng, số lượt còn lại
- Giỏ hàng online (Customer app)

### 7. Báo cáo & Thống kê
- **Staff/Admin**: doanh thu ngày/tháng, top sản phẩm, tồn kho thấp
- **Customer**: chi tiêu cá nhân, dịch vụ đã dùng, hạng thành viên
- Biểu đồ cột trực quan

### 8. Giám sát & Bảo mật
- **Nhật ký hoạt động (ActivityLog)**: ghi lại 16 loại hành động
- Phân quyền 3 role: Admin (full), Staff (bán hàng), Customer (cá nhân)
- Mã hóa mật khẩu SHA-256 + Salt
- Không cho xóa/sửa ActivityLog (append-only)

---

## Cấu trúc thư mục (Package)

```
com.petstore.app/
├── model/                    # Entity class (24 bảng)
├── data/                     # Tầng dữ liệu
│   ├── database/             # Room Database, 24 DAOs
│   ├── repository/           # 19 Repository
│   └── preference/           # SharedPreferences (session login)
├── ui/                       # Giao diện
│   ├── staff/                # === STAFF APP (23 màn hình) ===
│   │   ├── login/            # Đăng nhập
│   │   ├── dashboard/        # Tổng quan
│   │   ├── pet/              # Quản lý thú cưng
│   │   ├── cage/             # Quản lý chuồng
│   │   ├── customer/         # Quản lý khách hàng
│   │   ├── product/          # Quản lý sản phẩm
│   │   ├── order/            # Tạo đơn / Hủy đơn
│   │   ├── payment/          # Thanh toán / Hoàn tiền
│   │   ├── appointment/      # Lịch hẹn + Gán NV
│   │   ├── inventory/        # Nhập kho + Điều chỉnh tồn
│   │   ├── promotion/        # Khuyến mãi
│   │   ├── service/          # Dịch vụ
│   │   ├── supplier/         # Nhà cung cấp
│   │   ├── statistic/        # Báo cáo thống kê
│   │   ├── warranty/         # Bảo hành thú cưng
│   │   ├── contract/         # Hợp đồng mua bán
│   │   ├── activitylog/      # Nhật ký hoạt động
│   │   └── admin/            # Quản lý nhân viên
│   └── customer/             # === CUSTOMER APP (13 màn hình) ===
│       ├── login/            # Đăng nhập / Đăng ký / Quên MK
│       ├── home/             # Trang chủ
│       ├── booking/          # Đặt lịch / Hủy lịch
│       ├── history/          # Lịch sử giao dịch
│       ├── statistic/        # Thống kê cá nhân
│       ├── review/           # Đánh giá dịch vụ
│       ├── cart/             # Giỏ hàng online
│       ├── pet/              # Thú cưng của tôi
│       ├── profile/          # Hồ sơ cá nhân
│       └── notification/     # Thông báo
└── viewmodel/                # 23 ViewModel
```

---

## Cơ Sở Dữ Liệu (26 bảng)

| # | Entity | Table SQL | Ghi chú |
|---|--------|-----------|---------|
| 1 | `User` | NguoiDung | Admin + Nhân viên |
| 2 | `PetCategory` | LoaiThuCung | Chó, mèo, hamster... |
| 3 | `Pet` | ThuCung | Thú cưng (bán + của khách) |
| 4 | `Cage` | Chuong | **Quản lý chuồng nhốt** |
| 5 | `Customer` | KhachHang | Có đăng nhập riêng |
| 6 | `ProductCategory` | LoaiSanPham | Danh mục sản phẩm |
| 7 | `Product` | SanPham | Thức ăn, phụ kiện, thuốc |
| 8 | `Supplier` | NhaCungCap | Nhà cung cấp |
| 9 | `Order` | DonHang | **Master-Detail** với OrderDetail |
| 10 | `OrderDetail` | ChiTietDonHang | Detail của đơn hàng |
| 11 | `Appointment` | LichHen | Lịch hẹn dịch vụ |
| 12 | `Inventory` | PhieuNhapKho | **Master-Detail** với InventoryDetail |
| 13 | `InventoryDetail` | ChiTietPhieuNhap | Detail của phiếu nhập |
| 14 | `Service` | DichVu | Tắm, cắt tỉa, khám |
| 15 | `Promotion` | KhuyenMai | Mã giảm giá |
| 16 | `Review` | DanhGia | Đánh giá sao + nhận xét |
| 17 | `LoyaltyPoint` | DiemThuong | Tích điểm, hạng thành viên |
| 18 | `Cart` | GioHang | Giỏ hàng online |
| 19 | `FavoritePet` | ThuCungYeuThich | Thú cưng yêu thích |
| 20 | `CustomerNotification` | ThongBaoKhach | Thông báo cho khách |
| 21 | `PetHealthRecord` | HoSoSucKhoe | Hồ sơ sức khỏe, tiêm chủng |
| 22 | `Payment` | ThanhToan | **Master-Detail** với Order |
| 23 | `ActivityLog` | NhatKyHoatDong | Audit log (append-only) |
| 24 | `AppointmentStaff` | NhanVienPhucVu | **Master-Detail** với LichHen |
| 25 | `PetWarranty` | BaoHanhThuCung | **Bảo hành sức khỏe thú cưng sau khi mua** |
| 26 | `PurchaseContract` | HopDongMuaBan | **Hợp đồng mua bán thú cưng (giấy tờ pháp lý)** |

> SQL CREATE TABLE đầy đủ (PK, FK, CHECK, DEFAULT, INDEX) xem tại [`DATABASE.md`](DATABASE.md)

---

## Hướng dẫn cài đặt

1. Clone project về máy
2. Mở bằng Android Studio (Hedgehog 2023.1.1+)
3. Gradle Sync để tải dependencies
4. Chạy module `app-staff` cho nhân viên, `app-customer` cho khách hàng

## Tài khoản mặc định

| Role | Username / SĐT | Password |
|------|---------------|----------|
| Admin | `admin` | `admin123` |
| Nhân viên | `staff` | `staff123` |
| Khách hàng | `0901234567` | `customer123` |

## Yêu cầu hệ thống

- Android Studio Hedgehog (2023.1.1) trở lên
- Gradle 8.0+
- Min SDK 26, Target SDK 34
- Java 11+

---

## Tổng kết đồ án

| Thành phần | Số lượng |
|-----------|---------|
| Entity class | 26 |
| DAO interface | 26 |
| Repository | 21 |
| ViewModel | 23 |
| Staff UI screens | 23 |
| Customer UI screens | 13 |
| **Tổng form** | **36** |
| Quy trình (QT) | 5 |
| Master-Detail | 4 |
| Report / Thống kê | 2 |
| Bảo hành thú cưng | ✅ |
| Hợp đồng mua bán | ✅ |
| ActivityLog actions | 18 |
