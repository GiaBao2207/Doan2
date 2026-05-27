# Mô Tả Luồng Thực Tế Của App

> Hệ thống có **2 app**: App Nhân viên (Staff) và App Khách hàng (Customer).  
> Mô tả luồng thực tế cho cả hai phía.

---

## Luồng 1: Bán hàng (Quy trình chính)

```
┌──────────────────────────────────────────────────────────────────┐
│                      LUỒNG BÁN HÀNG                              │
│                                                                  │
│  KHÁCH HÀNG          NHÂN VIÊN              HỆ THỐNG            │
│     │                    │                       │              │
│     │  (1) Đến quầy       │                       │              │
│     │──────────────────►  │                       │              │
│     │                     │  (2) Đăng nhập        │              │
│     │                     │──────────────────────►│              │
│     │                     │                       │─ Xác thực    │
│     │                     │◄──────────────────────│              │
│     │                     │    Đăng nhập thành    │              │
│     │                     │    công               │              │
│     │  (3) Chọn thú cưng  │                       │              │
│     │──────────────────►  │  (4) Mở form Tạo đơn  │              │
│     │                     │──────────────────────►│              │
│     │                     │  (5) Tìm/chọn khách   │              │
│     │                     │──────────────────────►│─────────────►│
│     │                     │                       │  CustomerDao │
│     │                     │◄──────────────────────│──────────────│
│     │                     │    Danh sách khách    │              │
│     │  (6) Khách mới      │                       │              │
│     │──────────────────►  │  (7) Thêm khách mới   │              │
│     │                     │──────────────────────►│─────────────►│
│     │                     │                       │  Customer    │
│     │                     │                       │  .insert()   │
│     │                     │◄──────────────────────│──────────────│
│     │  (8) Chọn sản phẩm  │                       │              │
│     │──────────────────►  │  (9) Thêm sp vào đơn  │              │
│     │                     │  (chọn Product, nhập  │              │
│     │                     │   số lượng)           │              │
│     │                     │──────────────────────►│─────────────►│
│     │                     │                       │  ProductDao  │
│     │                     │                       │  (check tồn) │
│     │                     │◄──────────────────────│──────────────│
│     │                     │  Sản phẩm hợp lệ      │              │
│     │  (10) Khách chọn    │                       │              │
│     │  dịch vụ tắm cho    │  (11) Thêm dịch vụ    │              │
│     │  thú cưng           │──────────────────────►│─────────────►│
│     │                     │                       │  ServiceDao  │
│     │                     │◄──────────────────────│──────────────│
│     │                     │                       │              │
│     │  (12) Khách có mã   │  (13) Nhập mã KM      │              │
│     │  giảm giá           │──────────────────────►│─────────────►│
│     │                     │                       │  PromoDao    │
│     │                     │◄──────────────────────│──────────────│
│     │                     │  Giảm X%              │              │
│     │  (14) Xác nhận      │  (15) Lưu đơn hàng    │              │
│     │──────────────────►  │──────────────────────►│─────────────►│
│     │                     │                       │  OrderDao    │
│     │                     │                       │  + Order     │
│     │                     │                       │  DetailDao   │
│     │                     │◄──────────────────────│──────────────│
│     │                     │  Đã lưu đơn #123      │              │
│     │  (16) Khách thanh   │  (17) Chọn thanh toán │              │
│     │  toán               │──────────────────────►│─────────────►│
│     │                     │                       │  Cập nhật    │
│     │                     │                       │  status='paid'│
│     │                     │◄──────────────────────│──────────────│
│     │                     │  In hóa đơn           │              │
│     │  (18) Nhận hóa đơn  │                       │              │
│     │◄────────────────────│                       │              │
│     │                     │                       │              │
└──────────────────────────────────────────────────────────────────┘
```

### Chi tiết các bước trên UI

#### Bước 4: Mở form Tạo đơn hàng (`OrderActivity.java`)
- Layout: **Master-Detail**
  - **Master (trái/trên):** Chọn khách hàng + thông tin đơn (ngày, ghi chú)
  - **Detail (phải/dưới):** Danh sách sản phẩm đã chọn + nút "Thêm sản phẩm"

#### Bước 5: Chọn khách hàng
```
[SearchView] ── tìm theo tên/SĐT
RecyclerView ── hiển thị danh sách khách
Nút "Thêm khách mới" ── mở CustomerFormActivity
```

#### Bước 9: Thêm sản phẩm vào đơn
```
Dialog chọn sản phẩm:
├── [SearchView] ── tìm sản phẩm
├── RecyclerView ── danh sách sản phẩm (tên, giá, tồn kho)
├── EditText ── nhập số lượng
└── Nút [Xác nhận] ── thêm vào OrderDetail list
```

#### Bước 15: Lưu đơn hàng — ViewModel xử lý
```
OrderViewModel.placeOrder(customer, productList)
  │
  ├── OrderRepository.placeOrder(customer, productList)
  │   │
  │   ├── orderDao.insert(order)          // insert Order master
  │   ├── for each product:
  │   │   ├── orderDetailDao.insert(detail)  // insert OrderDetail
  │   │   └── productDao.updateQuantity(productId, newQty) // trừ tồn
  │   │
  │   └── return orderId
  │
  └── LiveData<OrderResult> ── cập nhật UI: "Đã lưu đơn #123"
```

---

## Luồng 2: Nhập kho (Quy trình phụ)

```
NHÂN VIÊN KHO            HỆ THỐNG
    │                        │
    │ (1) Đăng nhập          │
    │───────────────────────►│
    │ (2) Mở Nhập kho        │
    │───────────────────────►│
    │ (3) Chọn nhà cung cấp  │
    │───────────────────────►│────────► SupplierDao
    │◄───────────────────────│─────────│
    │ (4) Thêm sản phẩm      │
    │───────────────────────►│────────► ProductDao
    │◄───────────────────────│─────────│
    │ (5) Nhập số lượng + giá│
    │───────────────────────►│
    │ (6) Lưu phiếu nhập     │
    │───────────────────────►│────────► InventoryDao
    │                        │         + InventoryDetailDao
    │                        │         + ProductDao (cộng tồn)
    │◄───────────────────────│─────────│
    │ Hoàn tất phiếu nhập #  │
    │                        │
```

---

## Màn hình Dashboard — Thống kê (Report)

Sau khi đăng nhập, `DashboardActivity` hiển thị:

```
┌─────────────────────────────────────┐
│  ☰ PetStore             Xin chào,  │
│                         Nhân viên A │
├─────────────────────────────────────┤
│  ┌──────┐  ┌──────┐  ┌──────────┐  │
│  │ Thú  │  │ Hàng │  │ Doanh    │  │
│  │ cưng │  │ tồn  │  │ thu hôm │  │
│  │  45  │  │ 120  │  │ nay      │  │
│  │ con  │  │ sp   │  │ 12.5tr   │  │
│  └──────┘  └──────┘  └──────────┘  │
│                                     │
│  Lịch hẹn hôm nay (3)              │
│  ┌─────────────────────────────┐   │
│  │ 09:00 - Cún Miu - Tắm       │   │
│  │ 10:30 - Mèo Mun - Cắt móng  │   │
│  │ 14:00 - Cún Bông - Khám     │   │
│  └─────────────────────────────┘   │
│                                     │
│  [Xem báo cáo doanh thu]           │
│  → StatisticActivity: biểu đồ      │
│     cột doanh thu 7 ngày / tháng   │
└─────────────────────────────────────┘
```

### StatisticActivity — Báo cáo
```
Gồm các tab:
├── [Doanh thu ngày] ── Chart cột 7 ngày gần nhất
├── [Doanh thu tháng] ── Chart cột 12 tháng
├── [Top sản phẩm] ── Top 10 sản phẩm bán chạy
└── [Tồn kho] ── Danh sách sản phẩm sắp hết hàng (qty < 5)
```

---

---

## Luồng 3: Khách hàng đặt lịch online (Customer App)

```
┌────────────────────────────────────────────────────────────────────────┐
│              LUỒNG KHÁCH HÀNG ĐẶT LỊCH TRỰC TUYẾN                        │
│                                                                          │
│   KHÁCH HÀNG (App)                   HỆ THỐNG                          │
│       │                                    │                            │
│       │  (1) Mở app → Màn hình Đăng nhập   │                            │
│       │──────────────────────────────────►  │                            │
│       │                                    │─ Đăng nhập bằng SĐT + pass │
│       │◄──────────────────────────────────  │                            │
│       │  Đăng nhập thành công               │                            │
│       │                                    │                            │
│       │  (2) Trang chủ:                     │                            │
│       │  • Banner khuyến mãi                │                            │
│       │  • Dịch vụ nổi bật (tắm, cắt tỉa)  │                            │
│       │  • Thông báo: "Còn 2 lịch hẹn sắp  │                            │
│       │    tới"                             │                            │
│       │  • Điểm thưởng: 1,200 pts          │                            │
│       │                                    │                            │
│       │  (3) Chọn "Đặt lịch hẹn"           │                            │
│       │──────────────────────────────────►  │──────────► ServiceDao     │
│       │◄──────────────────────────────────  │◄──────────│ Danh sách DV  │
│       │                                    │                            │
│       │  (4) Chọn dịch vụ: "Tắm - 150k"    │                            │
│       │──────────────────────────────────►  │──────────► PetDao          │
│       │◄──────────────────────────────────  │◄──────────│ DS thú cưng   │
│       │                                    │                            │
│       │  (5) Chọn thú cưng: "Cún Miu"      │                            │
│       │──────────────────────────────────►  │                            │
│       │  (6) Chọn ngày/giờ: 28/05 14:00    │                            │
│       │──────────────────────────────────►  │                            │
│       │  (7) Xác nhận đặt lịch             │                            │
│       │──────────────────────────────────►  │──────────► AppointmentDao │
│       │                                    │  INSERT (status='pending')│
│       │◄──────────────────────────────────  │◄──────────│ "Đã đặt lịch"  │
│       │  ✅ Đặt lịch thành công!            │                            │
│       │  "Lịch hẹn #1024 ngày 28/05       │                            │
│       │   14:00 - Tắm cho Cún Miu"         │                            │
│       │                                    │                            │
│       │  (8) Nhận thông báo (push)         │                            │
│       │◄──────────────────────────────────  │  Gửi notification:        │
│       │  🔔 "Nhắc lịch: Ngày mai 14:00     │  "Nhắc lịch hẹn"          │
│       │        Tắm cho Cún Miu"            │                            │
└────────────────────────────────────────────────────────────────────────┘
```

### Màn hình BookingActivity (Đặt lịch)

```
┌─────────────────────────────────┐
│  ← Đặt lịch hẹn                 │
├─────────────────────────────────┤
│  Bước 1: Chọn dịch vụ           │
│  ┌─○ Tắm                 150k ─┐│
│  ├─○ Cắt tỉa             200k ─┤│
│  ├─○ Khám sức khỏe       300k ─┤│
│  └─○ Tiêm phòng          250k ─┘│
│                                 │
│  Bước 2: Chọn thú cưng         │
│  ┌──────────────────────────┐  │
│  │ 🐶 Cún Miu - Chó Poodle │  │
│  │ 🐱 Mun - Mèo Anh lông   │  │
│  └──────────────────────────┘  │
│                                 │
│  Bước 3: Chọn ngày & giờ       │
│  [Th 5, 28/05/2026] [14:00 ▼] │
│                                 │
│  Bước 4: Ghi chú (tùy chọn)    │
│  [ ........................................] │
│                                 │
│  [      ✅ XÁC NHẬN ĐẶT LỊCH      ] │
└─────────────────────────────────┘
```

---

## Luồng 4: Khách hàng xem lịch sử & thống kê cá nhân

```
┌────────────────────────────────────────────────────────────────┐
│  KHÁCH HÀNG               HỆ THỐNG                             │
│      │                         │                               │
│      │ (1) Vào "Lịch sử"        │                               │
│      │────────────────────────► │                               │
│      │                          │── OrderDao (DS đơn hàng)     │
│      │                          │── AppointmentDao (DS lịch hẹn)│
│      │◄────────────────────────│                               │
│      │                          │                               │
│      │  Tab 1: Đơn hàng         │                               │
│      │  ┌─────────────────────┐ │                               │
│      │  │ #123 25/05 - 450k  │ │                               │
│      │  │ #120 20/05 - 230k  │ │                               │
│      │  │ #118 15/05 - 780k  │ │                               │
│      │  └─────────────────────┘ │                               │
│      │                          │                               │
│      │  Tab 2: Lịch hẹn         │                               │
│      │  ┌─────────────────────┐ │                               │
│      │  │ 28/05 14:00 - Tắm  │ │ ◄── (sắp tới)                │
│      │  │ 20/05 10:00 - Khám │ │ ◄── (đã hoàn thành)           │
│      │  │ 10/05 09:00 - Cắt  │ │ ◄── (đã hủy)                  │
│      │  └─────────────────────┘ │                               │
│      │                          │                               │
│      │ (2) Vào "Thống kê của    │                               │
│      │     tôi"                 │                               │
│      │────────────────────────► │── OrderDao (group by month)  │
│      │                          │── AppointmentDao (count by   │
│      │                          │    service)                  │
│      │◄────────────────────────│                               │
│      │                          │                               │
│      │  ┌─────────────────────┐ │                               │
│      │  │   Chi tiêu 6 tháng  │ │                               │
│      │  │   2tr ████████      │ │                               │
│      │  │ 1.5tr ██████        │ │                               │
│      │  │   1tr ████          │ │                               │
│      │  │   5tr ██            │ │                               │
│      │  │   T5  T4  T3  T2   │ │                               │
│      │  └─────────────────────┘ │                               │
│      │                          │                               │
│      │  Dịch vụ đã dùng:       │                               │
│      │  Tắm: 8 lần             │                               │
│      │  Cắt tỉa: 3 lần         │                               │
│      │  Khám: 2 lần            │                               │
│      │                          │                               │
│      │  Hạng thành viên:       │                               │
│      │  ⭐ SILVER (1,200 pts)  │                               │
│      │  [Chi tiết tích điểm]   │                               │
└────────────────────────────────────────────────────────────────┘
```

### Màn hình MyStatisticActivity

```
┌─────────────────────────────────┐
│  ← Thống kê cá nhân             │
├─────────────────────────────────┤
│  👋 Xin chào, Nguyễn Văn A     │
│  ⭐ Hạng: SILVER                │
│  🎯 1,200 / 3,000 điểm         │
│  [████████░░░░░░░░░░] 40%      │
│                                 │
│  [Chi tiêu tháng này]           │
│  Tổng: 2,150,000đ               │
│  ┌───┬───┬───┬───┬───┬───┐    │
│  │███│   │███│   │   │███│    │
│  │███│   │███│ █ │ █ │███│    │
│  │███│ █ │███│ █ │ █ │███│    │
│  └───┴───┴───┴───┴───┴───┘    │
│   T5  T4  T3  T2  T1  T12     │
│                                 │
│  [Dịch vụ đã sử dụng]          │
│  🛁 Tắm               8 lần    │
│  ✂️ Cắt tỉa           3 lần    │
│  💉 Khám sức khỏe     2 lần    │
│  💊 Tiêm phòng        1 lần    │
│                                 │
│  [Lịch sử giao dịch]           │
│  25/05 Mua hạt Royal Canin 450k│
│  20/05 Tắm + cắt tỉa      350k│
│  15/05 Khám + thuốc        780k│
└─────────────────────────────────┘
```

---

## Luồng 5: Đánh giá dịch vụ của khách hàng

```
┌──────────────────────────────────────────────────────┐
│   KHÁCH HÀNG                 HỆ THỐNG                 │
│       │                          │                     │
│       │ (1) Vào "Đánh giá dịch   │                     │
│       │     vụ đã sử dụng"       │                     │
│       │─────────────────────────►│                     │
│       │                          │── AppointmentDao    │
│       │                          │   WHERE status='done'│
│       │◄─────────────────────────│                     │
│       │                          │                     │
│       │  DS dịch vụ đã hoàn thành│                     │
│       │  ┌─────────────────────┐ │                     │
│       │  │ 20/05 - Tắm Cún Miu│ │                     │
│       │  │ [Chưa đánh giá]    │ │                     │
│       │  │ 15/05 - Khám Cún   │ │                     │
│       │  │ [★★★★☆ - Tốt]     │ │                     │
│       │  └─────────────────────┘ │                     │
│       │                          │                     │
│       │ (2) Chọn "Đánh giá"     │                     │
│       │─────────────────────────►│                     │
│       │                          │                     │
│       │  Màn hình đánh giá:     │                     │
│       │  Dịch vụ: Tắm           │                     │
│       │  Thú cưng: Cún Miu      │                     │
│       │  Mức độ hài lòng:       │                     │
│       │  ★★★★★ (chọn 5 sao)     │                     │
│       │  Nhận xét:               │                     │
│       │  "Rất hài lòng, bạn     │                     │
│       │   tắm kỹ, thơm lâu"     │                     │
│       │                          │                     │
│       │ (3) Gửi đánh giá        │                     │
│       │─────────────────────────►│── ReviewDao.insert()│
│       │                          │                     │
│       │◄─────────────────────────│                     │
│       │  ✅ Cảm ơn bạn đã đánh  │                     │
│       │     giá! (+10 điểm)     │── LoyaltyPoint.earn()│
│       │                          │                     │
│       │  Thông báo:              │                     │
│       │◄─────────────────────────│                     │
│       │  "Đánh giá của bạn đã   │                     │
│       │   được ghi nhận"         │                     │
└──────────────────────────────────────────────────────┘
```

### Màn hình ReviewActivity

```
┌─────────────────────────────────┐
│  ← Đánh giá dịch vụ             │
├─────────────────────────────────┤
│  Dịch vụ: ✂️ Cắt tỉa lông      │
│  Thú cưng: 🐶 Cún Miu           │
│  Ngày: 20/05/2026               │
│                                 │
│  Bạn hài lòng thế nào?          │
│  ★ ★ ★ ★ ★                      │
│  (Chạm để đánh giá sao)         │
│                                 │
│  Nhận xét của bạn:              │
│  ┌──────────────────────────┐  │
│  │ Rất hài lòng! Cún Miu     │  │
│  │ được chăm sóc kỹ, nhân   │  │
│  │ viên nhiệt tình.           │  │
│  └──────────────────────────┘  │
│                                 │
│  [     📝 GỬI ĐÁNH GIÁ       ] │
│                                 │
│  💡 Đánh giá nhận +10 điểm     │
└─────────────────────────────────┘
```

---

## Kiến trúc App: Staff & Customer chung DB

```
┌─────────────────────────────────────────────────────────────────┐
│                    HỆ THỐNG PETSTORE                              │
│                                                                   │
│  ┌─────────────────────────┐  ┌─────────────────────────┐        │
│  │   STAFF APP (Android)   │  │  CUSTOMER APP (Android) │        │
│  │   Cho nhân viên quầy    │  │  Cho khách hàng online  │        │
│  │                         │  │                         │        │
│  │  • Đăng nhập Staff      │  │  • Đăng nhập KH         │        │
│  │  • Quản lý thú cưng     │  │  • Đặt lịch hẹn         │        │
│  │  • Tạo đơn hàng         │  │  • Xem lịch sử          │        │
│  │  • Quản lý khách hàng   │  │  • Thống kê cá nhân     │        │
│  │  • Nhập kho             │  │  • Đánh giá dịch vụ     │        │
│  │  • Thống kê doanh thu   │  │  • Giỏ hàng / Mua online│        │
│  │  • Quản lý nhân viên    │  │  • Thú cưng của tôi     │        │
│  └──────────┬──────────────┘  └──────────┬──────────────┘        │
│             │                            │                        │
│             └──────────┬─────────────────┘                        │
│                        ▼                                         │
│           ┌────────────────────────────┐                         │
│           │     Room Database (SQLite)   │                         │
│           │  • 20 bảng dùng chung        │                         │
│           │  • Cả Staff & Customer app   │                         │
│           │    đều truy cập cùng DB      │                         │
│           └────────────────────────────┘                         │
└─────────────────────────────────────────────────────────────────┘
```

---

| Chức năng | Mô tả | Áp dụng |
|---|---|---|
| **Phân quyền (3 role)** | Admin: full; Staff: bán hàng; Customer: xem riêng | User.role + Customer |
| **Mã hóa mật khẩu** | Dùng SHA-256 + Salt khi lưu password | User.java, Customer |
| **Tìm kiếm nâng cao** | Lọc thú cưng theo loại, giá, trạng thái | PetViewModel |
| **Sao lưu** | Export database ra file .db vào thẻ nhớ | AppDatabase backup |
| **Mã vạch/QR Code** | Tạo mã QR cho sản phẩm (dùng thư viện ZXing) | Product |
| **Tích điểm + Phân hạng** | Tích điểm mua hàng/đánh giá; Bronze→Silver→Gold→Diamond | LoyaltyPoint + Customer.tier |
| **Thông báo Push** | Nhắc lịch hẹn, khuyến mãi, xác nhận đơn hàng | CustomerNotification + FCM |
| **Hồ sơ sức khỏe thú cưng** | Lịch tiêm, tái khám, nhắc hẹn tự động | PetHealthRecord |
| **Giỏ hàng online** | Thêm/xóa sản phẩm, áp mã giảm giá, thanh toán | Cart + Order |

---

## Luồng dữ liệu MVVM (Điển hình: Tạo đơn hàng)

```
┌──────────────────────────────────────────────────────────┐
│                    MVVM FLOW                               │
│                                                            │
│  UI (View)           ViewModel          Repository    DAO │
│     │                     │                  │          │    │
│     │ userAddProduct()    │                  │          │    │
│     │─────────────────►   │                  │          │    │
│     │                     │ addProduct()     │          │    │
│     │                     │───────────────►  │          │    │
│     │                     │                  │  insert()│    │
│     │                     │                  │─────────►│    │
│     │                     │                  │◄─────────│    │
│     │                     │◄────────────────│          │    │
│     │  LiveData update    │                  │          │    │
│     │◄────────────────────│                  │          │    │
│     │  RecyclerView       │                  │          │    │
│     │  notifyDataSetChange│                  │          │    │
│     │                     │                  │          │    │
└──────────────────────────────────────────────────────────┘
```
