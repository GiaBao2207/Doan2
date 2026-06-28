# Mô Tả Luồng Thực Tế Của App

> Hệ thống có **2 app**: App Nhân viên (Staff) và App Khách hàng (Customer).  
> Mô tả luồng thực tế cho cả hai phía.  
> **Tổng số: 9 luồng** (Luồng 1–8 cũ + Luồng 9: Bán thú cưng có bảo hành & hợp đồng).

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
│     │                     │                       │  + ProductDao │
│     │                     │                       │  (trừ tồn)   │
│     │                     │                       │  + ActivityLog│
│     │                     │                       │  ('TAO_DON') │
│     │                     │◄──────────────────────│──────────────│
│     │                     │  Đã lưu đơn #123      │              │
│     │  (16) Khách thanh   │  (17) Ghi nhận thanh  │              │
│     │  toán               │  toán                 │              │
│     │──────────────────►  │──────────────────────►│─────────────►│
│     │                     │                       │  PaymentDao  │
│     │                     │                       │  .insert()   │
│     │                     │                       │  (tien_mat/  │
│     │                     │                       │   chuyen_khoan)│
│     │                     │                       │  + ActivityLog│
│     │                     │                       │  ('THANH_TOAN')│
│     │                     │◄──────────────────────│──────────────│
│     │                     │  ✅ Thanh toán thành  │              │
│     │                     │     công              │              │
│     │                     │  Cập nhật ĐH status=  │              │
│     │                     │  'paid'               │              │
│     │                     │                       │              │
│     │                     │  (18) In hóa đơn      │              │
│     │                     │──────────────────────►│─────────────►│
│     │                     │                       │  Export PDF  │
│     │                     │◄──────────────────────│──────────────│
│     │  (19) Nhận hóa đơn  │                       │              │
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

#### Bước 15: Lưu đơn hàng — ViewModel xử lý (cập nhật)
```
OrderViewModel.placeOrder(customer, productList, paymentInfo)
  │
  ├── OrderRepository.placeOrder(customer, productList)
  │   │
  │   ├── orderDao.insert(order)              // insert Order master
  │   ├── for each product:
  │   │   ├── orderDetailDao.insert(detail)     // insert OrderDetail
  │   │   └── productDao.updateQuantity(...)    // trừ tồn
  │   │
  │   ├── paymentDao.insert(payment)            // ghi giao dịch ThanhToán
  │   ├── orderDao.updateStatus(orderId, 'paid') // cập nhật trạng thái
  │   │
  │   ├── activityLogDao.insert(                // ghi NhậtKýHoạtĐộng
  │   │     'TAO_DON', 'DonHang', orderId, ...)
  │   │
  │   └── return orderId
  │
  └── LiveData<OrderResult> ── cập nhật UI: "Đã lưu đơn #ORD123"
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
    │                        │         + ActivityLogDao ('NHAP_KHO')
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
│       │                                    │  + ActivityLogDao          │
│       │                                    │    ('TAO_LICH')           │
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
│       │                          │── ActivityLogDao    │
│       │                          │   ('DANH_GIA')     │
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

## Luồng 6: Hủy đơn hàng & Hoàn tiền (Quy trình xử lý)

> Khi khách hàng muốn hủy đơn hoặc trả hàng, hệ thống thực hiện hoàn tiền và cập nhật tồn kho.

```
KHÁCH HÀNG           NHÂN VIÊN / ADMIN            HỆ THỐNG
     │                       │                         │
     │ (1) Yêu cầu hủy đơn   │                         │
     │──────────────────────►│                         │
     │                       │ (2) Mở OrderDetail      │
     │                       │   Kiểm tra: đã thanh    │
     │                       │   toán? còn hạn hủy?   │
     │                       │────────────────────────►│── OrderDao.getById()
     │                       │◄────────────────────────│── status = 'paid'
     │                       │                         │
     │                       │ (3) Chọn lý do hủy:     │
     │                       │   ○ Khách đổi ý         │
     │                       │   ○ Hàng lỗi            │
     │                       │   ○ Giao chậm           │
     │                       │   ○ Khác...             │
     │                       │                         │
     │                       │ (4) Xác nhận hủy        │
     │                       │────────────────────────►│
     │                       │                         ├── OrderDao.updateStatus(id, 'cancelled')
     │                       │                         ├── for each product in order:
     │                       │                         │   └── ProductDao.updateQuantity(prodId, +qty)  // trả tồn
     │                       │                         │
     │                       │ (5) Nếu đã thanh toán → │
     │                       │   Hoàn tiền:            │
     │                       │────────────────────────►│
     │                       │                         ├── PaymentDao.insert({
     │                       │                         │     donHangId, soTien: -totalAmount,
     │                       │                         │     phuongThuc, trangThai: 'hoan_tien',
     │                       │                         │     ghiChu: 'Hoàn tiền hủy đơn #ORD125'})
     │                       │                         │
     │                       │                         ├── OrderDao.updateStatus(id, 'refunded')
     │                       │                         │
     │                       │                         ├── ActivityLogDao.insert('HUY_DON', ...)
     │                       │                         ├── ActivityLogDao.insert('HOAN_TIEN', ...)
     │                       │                         │
     │                       │◄────────────────────────│
     │                       │  ✅ Đã hủy đơn #ORD125  │
     │◄──────────────────────│  💰 Đã hoàn 801,000đ    │
     │                       │                         │
```

### Màn hình xác nhận hủy đơn (CancelOrderDialog)

```
┌─ Xác nhận hủy đơn #ORD125 ─────────────────┐
│                                              │
│  Khách: Nguyễn Văn A                        │
│  Tổng tiền: 801,000đ                        │
│  Đã thanh toán: ✅ (Tiền mặt)               │
│                                              │
│  Lý do hủy:                                 │
│  ○ Khách đổi ý                              │
│  ○ Hàng lỗi / không đúng mô tả              │
│  ○ Giao hàng chậm                           │
│  ○ Khác: [________________]                 │
│                                              │
│  ☑ Hoàn tiền lại cho khách (801,000đ)       │
│  ☑ Trả lại tồn kho                          │
│                                              │
│  [     HỦY ĐƠN     ]  [Không hủy]          │
└──────────────────────────────────────────────┘
```

### Cập nhật: Luồng 2b — Lịch hẹn (xử lý xung đột)

Khi Staff hoặc Customer tạo lịch hẹn, ViewModel kiểm tra trùng trước khi lưu:

```
AppointmentViewModel.createAppointment(data)
  │
  ├── appointmentDao.checkConflict(thuCungId, date, timeSlot)
  │   ├── Nếu trùng → trả về lỗi: "Thú cưng đã có lịch hẹn khung giờ này"
  │   └── Nếu không trùng → tiếp tục
  │
  ├── [Kiểm tra nhân viên] nếu có chọn trước
  │   ├── appointmentStaffDao.checkStaffBusy(nhanVienId, date, timeSlot)
  │   │   ├── Nếu bận → gợi ý nhân viên khác
  │   │   └── Nếu rảnh → tiếp tục
  │
  ├── appointmentDao.insert(appointment)   // lưu lịch hẹn
  ├── activityLogDao.insert('TAO_LICH', ...)
  └── return appointmentId
```

---

## Luồng 7: Gán nhân viên phục vụ (Staff Assignment)

> Sau khi lịch hẹn được tạo, quản lý hoặc nhân viên trưởng ca gán người phụ trách.

```
QUẢN LÝ / TRƯỞNG CA                HỆ THỐNG
     │                                   │
     │ (1) Mở chi tiết lịch hẹn          │
     │──────────────────────────────────►│── AppointmentDao.getById()
     │◄──────────────────────────────────│
     │                                   │
     │ (2) Bấm [Gán nhân viên]           │
     │──────────────────────────────────►│── NguoiDungDao.getByRole('Staff')
     │◄──────────────────────────────────│── Danh sách nhân viên rảnh
     │                                   │
     │ (3) Chọn nhân viên + vai trò:     │
     │   ○ Nguyễn Văn A — [Tắm ▼]       │
     │   ○ Trần Thị B — [Cắt tỉa ▼]     │
     │   ○ Lê Văn C — [Phụ tá ▼]        │
     │──────────────────────────────────►│
     │                                   ├── NhanVienPhucVuDao.checkStaffBusy()
     │                                   │   (kiểm tra trùng giờ)
     │                                   ├── Nếu rảnh → insert NhanVienPhucVu
     │                                   ├── Nếu bận → báo "NV đã có lịch"
     │                                   ├── ActivityLogDao.insert('GAN_NV', ...)
     │◄──────────────────────────────────│
     │  ✅ Đã gán 3 nhân viên             │
     │                                   │
```

### Gán tự động (Auto-assign)

Nếu không gán thủ công, hệ thống có thể gán tự động dựa trên:
- Nhân viên có kỹ năng phù hợp (vai trò)
- Nhân viên ít lịch nhất trong ca
- Nhân viên có rating cao nhất

---

## Luồng 8: Điều chỉnh tồn kho (Inventory Adjustment)

> Xử lý hàng hỏng, hết hạn, thất lạc hoặc trả lại nhà cung cấp mà không qua bán hàng.

```
NHÂN VIÊN KHO / ADMIN               HỆ THỐNG
     │                                    │
     │ (1) Mở form Điều chỉnh tồn         │
     │───────────────────────────────────►│── ProductDao.getAll()
     │◄───────────────────────────────────│
     │                                    │
     │ (2) Chọn sản phẩm + loại ĐC:       │
     │   Sản phẩm: [Hạt Royal Canin ▼]    │
     │   Tồn hiện tại: 20                  │
     │   Loại điều chỉnh:                 │
     │   ○ Hàng hỏng / hết hạn            │
     │   ○ Trả NCC                        │
     │   ○ Kiểm kê chênh lệch             │
     │   ○ Hàng khuyến mãi / cho tặng     │
     │   Số lượng: [5]                    │
     │   Ghi chú: [Hết hạn T06/2026]     │
     │                                    │
     │ (3) Xác nhận                        │
     │───────────────────────────────────►│
     │                                    ├── ProductDao.updateQuantity(prodId, newQty)
     │                                    ├── ActivityLogDao.insert('DIEU_CHINH_TON',
     │                                    │     productId, type, qty, note)
     │                                    │
     │◄───────────────────────────────────│
     │  ✅ Đã điều chỉnh: -5               │
     │  Tồn mới: 15                       │
     │                                    │
```

### Các loại điều chỉnh

| Loại | Số lượng | Ghi chú |
|------|---------|---------|
| Hàng hỏng / hết hạn | Giảm | Ghi rõ lý do + ngày hết hạn |
| Trả NCC | Giảm | Kèm mã phiếu trả (có thể bổ sung sau) |
| Kiểm kê | Tăng/Giảm | Chênh lệch thực tế so với hệ thống |
| Cho tặng / KM | Giảm | Phục vụ marketing |

### Cập nhật: Bổ sung ActivityLog cho các luồng còn thiếu

| Hành động (hanhDong) | Khi nào ghi | Ghi bởi |
|---|---|---|
| `DANG_NHAP` | User/Staff đăng nhập thành công | LoginViewModel |
| `DANG_XUAT` | User/Staff đăng xuất | LoginViewModel |
| `TAO_DON` | Tạo đơn hàng mới | OrderViewModel |
| `HUY_DON` | Hủy đơn hàng | OrderViewModel |
| `HOAN_TIEN` | Hoàn tiền cho đơn hủy | OrderViewModel |
| `THANH_TOAN` | Ghi nhận thanh toán | PaymentViewModel |
| `NHAP_KHO` | Nhập kho mới | InventoryViewModel |
| `DIEU_CHINH_TON` | Điều chỉnh tồn kho (hỏng, trả, KM) | InventoryViewModel |
| `TAO_LICH` | Tạo lịch hẹn mới | AppointmentViewModel |
| `HUY_LICH` | Hủy lịch hẹn | AppointmentViewModel |
| `GAN_NV` | Gán nhân viên phục vụ | AppointmentStaffViewModel |
| `THEM_SAN_PHAM` | Thêm sản phẩm mới | ProductViewModel |
| `SUA_GIA` | Thay đổi giá sản phẩm | ProductViewModel |
| `XOA_DU_LIEU` | Xóa bất kỳ bản ghi nào (Admin) | AdminViewModel |
| `DANH_GIA` | Khách hàng đánh giá dịch vụ | ReviewViewModel |
| `DOI_MAT_KHAU` | Đổi mật khẩu | ProfileViewModel |
| `BAO_HANH` | Tạo bảo hành / xử lý khiếu nại bảo hành | PetWarrantyViewModel |
| `KY_HOP_DONG` | Ký hợp đồng mua bán thú cưng | PurchaseContractViewModel |

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
│           │  • 23 bảng dùng chung        │                         │
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
| **Thanh toán** | Ghi nhận giao dịch thanh toán, hồn tiền, trace mã GD | Payment |
| **Nhật ký hoạt động** | Ghi log mọi hành động, phục vụ kiểm tra, truy vết (18 actions) | ActivityLog |
| **Bảo hành thú cưng** | Tạo bảo hành khi bán thú, xử lý khiếu nại bảo hành | PetWarranty |
| **Hợp đồng mua bán** | Tạo hợp đồng mua bán thú cưng, in hợp đồng pháp lý | PurchaseContract |
| **Gán nhân viên phục vụ** | Gán nhân viên cho lịch hẹn, theo dõi năng suất | AppointmentStaff |

---

## Luồng 9: Bán thú cưng có bảo hành & hợp đồng (Quy trình mới)

```
┌──────────────────────────────────────────────────────────────────────────┐
│               LUỒNG BÁN THÚ CƯNG + BẢO HÀNH + HỢP ĐỒNG                   │
│                                                                          │
│  KHÁCH HÀNG          NHÂN VIÊN              HỆ THỐNG                    │
│     │                    │                       │                       │
│     │  (1) Chọn thú cưng  │                       │                       │
│     │──────────────────►  │                       │                       │
│     │                     │  (2) Kiểm tra thú     │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ ThuCungDao           │
│     │                     │                       │  (status='available') │
│     │                     │◄──────────────────────│                       │
│     │                     │  Thú còn bán được     │                       │
│     │                     │                       │                       │
│     │  (3) Thỏa thuận giá │                       │                       │
│     │◄───────────────────►│                       │                       │
│     │                     │  (4) Tạo đơn bán       │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ OrderDao.insert()    │
│     │                     │                       │  (loai='pet_sale')    │
│     │                     │                       │                       │
│     │                     │  (5) Tạo hợp đồng      │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ HopDongMuaBan        │
│     │                     │                       │  (maHopDong tự sinh,  │
│     │                     │                       │   thuCungId, giaBan,  │
│     │                     │                       │   dieuKhoan)          │
│     │                     │                       │─ Ghi ActivityLog      │
│     │                     │                       │  'KY_HOP_DONG'        │
│     │                     │                       │                       │
│     │                     │  (6) Tạo bảo hành      │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ BaoHanhThuCung       │
│     │                     │                       │  (thuCungId,          │
│     │                     │                       │   ngayBatDau=NOW,     │
│     │                     │                       │   soNgayBaoHanh=30,   │
│     │                     │                       │   trangThai='active') │
│     │                     │                       │─ Ghi ActivityLog      │
│     │                     │                       │  'BAO_HANH'           │
│     │                     │                       │                       │
│     │                     │  (7) Thanh toán        │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ ThanhToan            │
│     │                     │                       │  (soTien=giaBan)      │
│     │                     │                       │                       │
│     │                     │  (8) Cập nhật thú      │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ ThuCung              │
│     │                     │                       │  (status='sold',      │
│     │                     │                       │   khachHangId=...,    │
│     │                     │                       │   chuongId=NULL)      │
│     │                     │                       │                       │
│     │  (9) Nhận thú +      │                       │                       │
│     │      hợp đồng        │                       │                       │
│     │◄─────────────────────│                       │                       │
│     │                     │                       │                       │
│     │  (10) Khiếu nại bảo  │                       │                       │
│     │       hành (nếu có)  │                       │                       │
│     │──────────────────►   │  (11) Kiểm tra bảo    │                       │
│     │                     │       hành             │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ BaoHanhThuCung       │
│     │                     │                       │  (trangThai='claimed',│
│     │                     │                       │   ghiChu=...)         │
│     │                     │  (12) Lập hồ sơ SK    │                       │
│     │                     │──────────────────────►│                       │
│     │                     │                       │─ HoSoSucKhoe.insert() │
│     │                     │                       │                       │
│     │  (13) Kết quả xử lý  │                       │                       │
│     │◄─────────────────────│                       │                       │
└──────────────────────────────────────────────────────────────────────────┘
```

### Bước chi tiết (Staff):

1. **Chọn thú cưng**: Staff mở PetListFragment, filter `status='available'`, chọn thú khách muốn mua
2. **Kiểm tra thú**: Hệ thống kiểm tra thú còn sống, còn trong cửa hàng, không có bệnh (HoSoSucKhoe gần nhất OK)
3. **Thỏa thuận giá**: Staff nhập giá bán (có thể giảm giá theo chính sách)
4. **Tạo đơn hàng**: OrderDao.insert() với `loai='pet_sale'`, ghi ActivityLog 'TAO_DON'
5. **Tạo hợp đồng**: HopDongMuaBan — tự sinh mã `HD-PET-YYYY-NNNNNN`, nhập `giaBan`, `dieuKhoan` (mặc định: bảo hành 30 ngày), ghi ActivityLog 'KY_HOP_DONG'
6. **Tạo bảo hành**: BaoHanhThuCung — `ngayBatDau=NOW`, `ngayKetThuc=NOW+30`, `soNgayBaoHanh=30`, `trangThai='active'`, ghi ActivityLog 'BAO_HANH'
7. **Thanh toán**: Ghi nhận giao dịch ThanhToan (soTien=giaBan, phuongThuc=tien_mat|chuyen_khoan), ghi ActivityLog 'THANH_TOAN'
8. **Cập nhật thú cưng**: ThuCung.status='sold', gán khachHangId, bỏ chuồng (chuongId=NULL)
9. **Bàn giao**: In hợp đồng (PDF), giao thú + hợp đồng cho khách

### Khiếu nại bảo hành (Staff):

- **Bước 10**: Khách mang thú đến khiếu nại trong thời gian bảo hành
- **Bước 11**: Staff kiểm tra thông tin bảo hành (còn hạn, chưa claimed)
- Cập nhật BaoHanhThuCung.trangThai='claimed', ghi chú lý do
- **Bước 12**: Lập HoSoSucKhoe để theo dõi tình trạng sức khỏe, lịch tái khám
- **Bước 13**: Trả kết quả cho khách (khám miễn phí, điều trị, ...)

### MVVM Flow:

```
WarrantyActivity / ContractActivity
    │
    ▼
PetWarrantyViewModel / PurchaseContractViewModel
    │
    ▼
PetWarrantyRepository / PurchaseContractRepository
    │
    ▼
PetWarrantyDao / PurchaseContractDao + PetDao + OrderDao + ActivityLogDao
    │
    ▼
Room Database (BaoHanhThuCung, HopDongMuaBan, ThuCung, DonHang)
```

### ActivityLog ghi nhận:

| Action | Khi nào | Ghi chú |
|--------|---------|---------|
| `TAO_DON` | Bước 4 | Khi tạo đơn bán thú |
| `KY_HOP_DONG` | Bước 5 | Ký hợp đồng mua bán |
| `BAO_HANH` | Bước 6 | Tạo bảo hành |
| `THANH_TOAN` | Bước 7 | Thanh toán |

---

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
