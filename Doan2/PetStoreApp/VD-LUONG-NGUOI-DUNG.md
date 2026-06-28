# Ví Dụ Luồng Thực Tế Khi Người Dùng Sử Dụng App (Theo Rule)

> Mô tả chi tiết từng bước người dùng thao tác trên app cho mỗi **rule (vai trò)**:  
> **Admin** → **Staff (Nhân viên)** → **Customer (Khách hàng)**

---

## 1. RULE: ADMIN (Quản trị viên)

> Nhiệm vụ: Quản lý toàn bộ hệ thống, nhân viên, xem báo cáo.

### Luồng: Quản lý nhân viên

```
┌─────────────────────────────────────────────────────────────────────┐
│  ADMIN: Quản lý tài khoản nhân viên                                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) Mở app → Màn hình Đăng nhập                                    │
│      ├─ Nhập username: admin                                        │
│      ├─ Nhập password: ●●●●●●●●                                     │
│      └─ Bấm [Đăng nhập]                                             │
│                                                                      │
│  (2) Hệ thống xác thực:                                             │
│      ├─ UserDao.login("admin", "admin123")                          │
│      ├─ Kiểm tra role == "Admin" → true                             │
│      ├─ ActivityLogDao.insert('DANG_NHAP', 'Admin', adminId)        │
│      └─ Chuyển đến DashboardActivity (Admin view)                   │
│                                                                      │
│  (3) Dashboard Admin hiển thị:                                      │
│      ┌─────────────────────────────────────┐                        │
│      │  ☰ PETSTORE            ADMIN        │                        │
│      ├─────────────────────────────────────┤                        │
│      │  📊 Doanh thu hôm nay: 12,500,000đ  │                        │
│      │  👥 Nhân viên: 5          📦 Đơn: 23│                        │
│      │  ⚠️ Tồn kho thấp: 3 sản phẩm        │                        │
│      ├─────────────────────────────────────┤                        │
│      │  [Quản lý nhân viên]  [Báo cáo]     │                        │
│      │  [Quản lý kho]       [Cài đặt]     │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (4) Bấm [Quản lý nhân viên] → UserManagementActivity              │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Quản lý nhân viên       [+ Thêm] │                        │
│      ├─────────────────────────────────────┤                        │
│      │  👤 Nguyễn Văn A - Staff            │                        │
│      │  👤 Trần Thị B  - Staff            │                        │
│      │  👤 Lê Văn C    - Staff  🔴 Khóa   │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (5) Bấm [+ Thêm] → Dialog thêm nhân viên:                         │
│      ├─ Họ tên:    [Nguyễn Văn B]                                  │
│      ├─ Username:  [nguyenvanb]                                    │
│      ├─ Password:  [●●●●●●●●]                                     │
│      ├─ Số điện thoại: [0901234568]                                │
│      └─ Bấm [Lưu] → UserDao.insert()                                │
│                                                                      │
│  (6) Vào [Báo cáo] → StatisticActivity (Report)                    │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Báo cáo doanh thu                │                        │
│      ├─────────────────────────────────────┤                        │
│      │  [Hôm nay] [7 ngày] [Tháng] [Tùy chỉnh]│                    │
│      │                                     │                        │
│      │  Doanh thu tháng 5/2026: 98,500,000đ│                        │
│      │  ┌───┬───┬───┬───┬───┬───┐        │                        │
│      │  │███│███│███│███│███│███│        │                        │
│      │  └───┴───┴───┴───┴───┴───┘        │                        │
│      │   T5  T4  T3  T2  T1  T12        │                        │
│      │                                     │                        │
│      │  Top 5 nhân viên bán chạy:        │                        │
│      │  1. Nguyễn Văn A: 45,000,000đ     │                        │
│      │  2. Trần Thị B: 38,000,000đ       │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (7) [Xuất Excel] → Export report ra file .xlsx                      │
│                                                                      │
│  (8) Vào [Nhật ký hoạt động] → ActivityLogActivity                  │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Nhật ký hoạt động      [Lọc ▼]   │                        │
│      ├─────────────────────────────────────┤                        │
│      │  🔍 [Tìm kiếm...              ]    │                        │
│      │─────────────────────────────────────│                        │
│      │  📋 28/05 09:15 - Staff A          │                        │
│      │      TẠO_ĐƠN - ĐơnHàng #ORD125     │                        │
│      │      Chi tiết: KH Nguyễn Văn C     │                        │
│      ├─────────────────────────────────────┤                        │
│      │  📋 28/05 09:20 - Staff A          │                        │
│      │      THANH_TOÁN - ĐơnHàng #ORD125  │                        │
│      │      Tiền mặt: 801,000đ            │                        │
│      ├─────────────────────────────────────┤                        │
│      │  📋 27/05 14:00 - Admin            │                        │
│      │      THÊM_NV - NgườiDùng #5        │                        │
│      │      "Thêm nhân viên Trần Văn B"   │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 2. RULE: STAFF (Nhân viên bán hàng)

> Nhiệm vụ: Bán hàng tại quầy, quản lý thú cưng, khách hàng, lịch hẹn.

### Luồng 2a: Bán hàng tại quầy (Quy trình chính)

```
┌─────────────────────────────────────────────────────────────────────┐
│  STAFF: Bán hàng cho khách tại cửa hàng                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) Nhân viên đăng nhập:                                           │
│      ┌─────────────────────────────────┐                            │
│      │  PETSTORE STAFF                  │                            │
│      │  Username: [nhanvien_a       ]  │                            │
│      │  Password: [●●●●●●●●         ]  │                            │
│      │  [        ĐĂNG NHẬP         ]    │                            │
│      └─────────────────────────────────┘                            │
│      → UserDao.login(username, pass)                                │
│      → ActivityLogDao.insert('DANG_NHAP', 'Staff', userId)         │
│                                                                      │
│  (2) Dashboard Staff:                                               │
│      ┌─────────────────────────────────────┐                        │
│      │  ☰ PETSTORE          NHÂN VIÊN A    │                        │
│      ├─────────────────────────────────────┤                        │
│      │  [➕ Tạo đơn hàng]  [📋 DS đơn hàng]│                        │
│      │  [🐶 Quản lý thú]  [👥 Khách hàng] │                        │
│      │  [📅 Lịch hẹn]     [📦 Sản phẩm]  │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (3) Bấm [➕ Tạo đơn hàng] → OrderActivity                         │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Tạo đơn hàng          [Lưu]      │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Khách hàng:                         │                        │
│      │  [🔍 Tìm SĐT...               ]     │                        │
│      │  Kết quả:                           │                        │
│      │  ○ Nguyễn Văn A - 0901234567       │                        │
│      │  ○ Trần Thị B - 0907654321         │                        │
│      │  [+ Thêm khách mới]                │                        │
│      │─────────────────────────────────────│                        │
│      │  Sản phẩm trong đơn:                │                        │
│      │  ┌─────────────────────────┬──┬──┐  │                        │
│      │  │ Sản phẩm         SL  Đơn│  │  │  │                        │
│      │  ├─────────────────────────┼──┼──┤  │                        │
│      │  │ Hạt Royal Canin 15kg│ 2 │...│  │                        │
│      │  │ Pate Whiskas       │ 5 │...│  │                        │
│      │  │ Đồ chơi chuột      │ 1 │...│  │                        │
│      │  └─────────────────────────┴──┴──┘  │                        │
│      │  [+ Thêm sản phẩm]                  │                        │
│      │─────────────────────────────────────│                        │
│      │  Tạm tính: 890,000đ                │                        │
│      │  Mã KM: [PET10] → -10% (89,000đ)   │                        │
│      │  Tổng: 801,000đ                     │                        │
│      │─────────────────────────────────────│                        │
│      │  Thanh toán: ○ Tiền mặt ○ Chuyển khoản│                      │
│      │─────────────────────────────────────│                        │
│      │  [        LƯU ĐƠN HÀNG          ]    │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (4) Bấm [LƯU ĐƠN HÀNG] → Xử lý:                                   │
│      ├─ OrderViewModel.placeOrder(data)                             │
│      ├─ OrderRepository.placeOrder()                                │
│      │  ├─ OrderDao.insert(order) → orderId = 125                  │
│      │  ├─ OrderDetailDao.insertList(details)                      │
│      │  ├─ ProductDao.updateQuantity(prodId, newQty)               │
│      │  ├─ PaymentDao.insert(payment)                               │
│      │  │  (ghi giao dịch thanh toán)                              │
│      │  ├─ OrderDao.updateStatus(orderId, 'paid')                   │
│      │  └─ ActivityLogDao.insert('TAO_DON', ...)                    │
│      │     ActivityLogDao.insert('THANH_TOAN', ...)                 │
│      └─ Hiển thị: ✅ "Đã lưu đơn hàng #ORD125"                      │
│         [In hóa đơn] [Xem chi tiết] [Tiếp tục bán]                 │
│                                                                      │
│  (5) Bấm [In hóa đơn] → Xuất PDF / in qua Bluetooth printer        │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Luồng 2b: Quản lý lịch hẹn

```
┌─────────────────────────────────────────────────────────────────────┐
│  STAFF: Xem & xử lý lịch hẹn của khách                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) Bấm [📅 Lịch hẹn] → AppointmentFragment                       │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Lịch hẹn            [➕ Thêm]    │                        │
│      ├─────────────────────────────────────┤                        │
│      │  📅 Hôm nay (28/05/2026)            │                        │
│      │  ┌──────────────────────────────┐   │                        │
│      │  │ 🟢 09:00 Tắm - Cún Miu     ✅ │   │ ← (đã check-in)      │
│      │  │ 🟡 10:30 Khám - Mèo Mun    ⏳ │   │ ← (đang chờ)         │
│      │  │ 🔴 14:00 Cắt tỉa - Bông    ❌ │   │ ← (khách hủy)        │
│      │  │ 🟡 15:30 Tiêm - Milo       ⏳ │   │ ← (đang chờ)         │
│      │  └──────────────────────────────┘   │                        │
│      │─────────────────────────────────────│                        │
│      │  [Hôm nay] [Ngày mai] [Tuần này]   │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (2) Bấm vào lịch "10:30 Khám - Mèo Mun" → Chi tiết                │
│      ┌─────────────────────────────────────┐                        │
│      │  Chi tiết lịch hẹn                  │                        │
│      │  Khách: Nguyễn Thị B (0907654321)  │                        │
│      │  Thú cưng: 🐱 Mun - Mèo Anh lông   │                        │
│      │  Dịch vụ: 💉 Khám sức khỏe - 300k │                        │
│      │  Ghi chú: "Mèo bỏ ăn 2 ngày"      │                        │
│      │                                     │                        │
│      │  [✅ Check-in] [📞 Gọi khách]       │                        │
│      │  [🕐 Dời lịch] [❌ Hủy lịch]       │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (3) Bấm [✅ Check-in] → AppointmentDao.update(status='done')       │
│      → Tự động tạo hóa đơn dịch vụ nếu có phát sinh thêm            │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Luồng 2c: Thêm thú cưng cho khách

```
┌─────────────────────────────────────────────────────────────────────┐
│  STAFF: Thêm thú cưng mới cho khách                                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) Bấm [🐶 Quản lý thú] → PetListFragment                        │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Danh sách thú cưng     [+ Thêm]  │                        │
│      ├─────────────────────────────────────┤                        │
│      │  [🔍 Tìm tên/chủ...              ]│                        │
│      │                                     │                        │
│      │  🐶 Cún Miu - Poodle - Ng.Văn A   │                        │
│      │  🐱 Mun - Mèo Anh lông - Tr.B     │                        │
│      │  🐶 Bông - Golden - Lê Văn C      │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (2) Bấm [+ Thêm] → PetFormActivity                                │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Thêm thú cưng          [Lưu]     │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Tên thú cưng: [Milo            ]  │                        │
│      │  Loại: [🐶 Chó ▼]                   │                        │
│      │  Giống: [Golden Retriever       ]  │                        │
│      │  Tuổi: [2] tháng                    │                        │
│      │  Cân nặng: [12] kg                 │                        │
│      │  Màu sắc: [Vàng óng             ]  │                        │
│      │  Giá bán: [15,000,000           ]  │                        │
│      │  Trạng thái: [Còn bán ▼]           │                        │
│      │  Chủ: [Nguyễn Văn A - 090...  🔍] │                        │
│      │                                     │                        │
│      │  [        LƯU        ]              │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (3) Bấm [LƯU] → PetDao.insert(pet)                                │
│      → ✅ "Đã thêm thú cưng Milo"                                    │
│      → Quay lại danh sách, thấy Milo xuất hiện                      │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. RULE: CUSTOMER (Khách hàng)

> Nhiệm vụ: Đặt lịch, mua online, xem lịch sử, đánh giá.

### Luồng 3a: Đăng ký & Đăng nhập

```
┌─────────────────────────────────────────────────────────────────────┐
│  CUSTOMER: Đăng ký tài khoản mới                                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) Mở app Customer → Màn hình Đăng nhập                          │
│      ┌─────────────────────────────────┐                            │
│      │    🐾 PETSTORE                   │                            │
│      │    Chào mừng bạn!                │                            │
│      │                                   │                          │
│      │    Số điện thoại:                 │                          │
│      │    [0912345678              ]     │                          │
│      │    Mật khẩu:                     │                          │
│      │    [●●●●●●●●               ]     │                          │
│      │                                   │                          │
│      │    [       ĐĂNG NHẬP        ]    │                          │
│      │                                   │                          │
│      │    Chưa có tài khoản? [Đăng ký]  │                          │
│      └─────────────────────────────────┘                            │
│                                                                      │
│  (2) Bấm [Đăng ký] → Màn hình Đăng ký                              │
│      ┌─────────────────────────────────┐                            │
│      │  ← Đăng ký tài khoản            │                            │
│      ├─────────────────────────────────┤                            │
│      │  Họ tên: [Nguyễn Văn C      ]  │                            │
│      │  Số điện thoại: [0912345678  ]  │                            │
│      │  Email: [nguyenvanc@email   ]   │                            │
│      │  Mật khẩu: [●●●●●●●●       ]   │                            │
│      │  Nhập lại MK: [●●●●●●●●    ]   │                            │
│      │                                   │                          │
│      │  [      ĐĂNG KÝ            ]    │                          │
│      └─────────────────────────────────┘                            │
│                                                                      │
│  (3) Bấm [ĐĂNG KÝ] → CustomerDao.insert()                          │
│      → ✅ "Đăng ký thành công! Mời bạn đăng nhập"                   │
│      → Quay lại màn hình đăng nhập                                  │
│                                                                      │
│  (4) Nhập SĐT + MK → Bấm [ĐĂNG NHẬP]                               │
│      → CustomerLoginViewModel.login(phone, pass)                    │
│      → CustomerDao.login(phone, pass)                               │
│      → ActivityLogDao.insert('DANG_NHAP', 'Customer', customerId)  │
│      → ✅ Chuyển đến CustomerHomeActivity                           │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Luồng 3b: Trang chủ khách hàng & Đặt lịch

```
┌─────────────────────────────────────────────────────────────────────┐
│  CUSTOMER: Trang chủ và đặt lịch hẹn                                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) CustomerHomeActivity:                                          │
│      ┌─────────────────────────────────────┐                        │
│      │  🔔 🛒 👤                           │                        │
│      │  🐾 Xin chào, Nguyễn Văn C!        │                        │
│      │  ⭐ Hạng Bạc - 1,200 điểm           │                        │
│      ├─────────────────────────────────────┤                        │
│      │  [🎯 ĐẶT LỊCH NGAY]                 │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Dịch vụ nổi bật:                   │                        │
│      │  ┌────┐┌────┐┌────┐┌────┐         │                        │
│      │  │ Tắm ││Cắt ││Khám││Tiêm│         │                        │
│      │  │150k ││200k││300k││250k│         │                        │
│      │  └────┘└────┘└────┘└────┘         │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Lịch hẹn sắp tới:                  │                        │
│      │  📅 28/05 14:00 - Tắm cho Cún Miu  │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Thông báo:                         │                        │
│      │  🔔 Nhắc lịch: Ngày mai 14:00...   │                        │
│      │  🎉 KM: Giảm 20% thức ăn hạt       │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (2) Bấm [🎯 ĐẶT LỊCH NGAY] → BookingActivity                      │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Đặt lịch hẹn       [Tiếp theo▶] │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Bước 1: Chọn dịch vụ              │                        │
│      │  ┌──────────────────────────────┐  │                        │
│      │  │○ 🛁 Tắm                  150k│  │                        │
│      │  │● ✂️ Cắt tỉa + Tắm      300k│  │                        │
│      │  │○ 💉 Khám sức khỏe       300k│  │                        │
│      │  │○ 💊 Tiêm phòng          250k│  │                        │
│      │  └──────────────────────────────┘  │                        │
│      │─────────────────────────────────────│                        │
│      │  Bước 2: Chọn thú cưng             │                        │
│      │  ┌──────────────────────────────┐  │                        │
│      │  │🐶 Cún Miu - Poodle          │  │                        │
│      │  │🐱 Mun - Mèo Anh lông       │  │                        │
│      │  │🐹 Bông - Hamster           │  │                        │
│      │  └──────────────────────────────┘  │                        │
│      │─────────────────────────────────────│                        │
│      │  Bước 3: Chọn ngày & giờ          │                        │
│      │  [29/05/2026] [09:00 ▼]           │                        │
│      │─────────────────────────────────────│                        │
│      │  Ghi chú:                          │                        │
│      │  [Cắt tỉa ngắn, để lại 2cm...]   │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (3) Bấm [Tiếp theo▶] → Xác nhận                                   │
│      ┌─────────────────────────────────────┐                        │
│      │  Xác nhận đặt lịch                  │                        │
│      │                                     │                        │
│      │  Dịch vụ: ✂️ Cắt tỉa + Tắm        │                        │
│      │  Thú cưng: 🐶 Cún Miu              │                        │
│      │  Ngày: 29/05/2026                  │                        │
│      │  Giờ: 09:00                        │                        │
│      │  Giá: 300,000đ                     │                        │
│      │                                     │                        │
│      │  [  ✅ XÁC NHẬN ĐẶT LỊCH  ]       │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (4) Hệ thống kiểm tra xung đột:                                    │
│      ├── AppointmentDao.checkConflict(thuCungId, date, timeSlot)    │
│      ├── Nếu trùng → "Thú cưng đã có lịch khung giờ này!"          │
│      └── Nếu không trùng → tiếp tục ↓                               │
│                                                                      │
│  (5) Bấm [XÁC NHẬN] → AppointmentDao.insert()                      │
│      → ActivityLogDao.insert('TAO_LICH', ...)                       │
│      → ✅ "Đặt lịch thành công!"                                    │
│      → Trở về trang chủ, thấy lịch mới trong "Lịch hẹn sắp tới"   │
│      → Nhận thông báo nhắc lịch trước 1 ngày                        │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Luồng 3c: Xem lịch sử & Thống kê cá nhân

```
┌─────────────────────────────────────────────────────────────────────┐
│  CUSTOMER: Xem lịch sử dịch vụ và thống kê cá nhân                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) Bottom Navigation: Bấm [📋 Lịch sử]                            │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Lịch sử                           │                        │
│      ├─────────────────────────────────────┤                        │
│      │  [📄 Đơn hàng] [📅 Lịch hẹn]        │                        │
│      ├─────────────────────────────────────┤                        │
│      │  📄 ĐƠN HÀNG ĐÃ MUA                 │                        │
│      │  ┌──────────────────────────────┐   │                        │
│      │  │ #ORD125 - 25/05              │   │                        │
│      │  │ Hạt Royal Canin x2 + Pate   │   │                        │
│      │  │ 💰 801,000đ ✓ Đã nhận       │   │                        │
│      │  ├──────────────────────────────┤   │                        │
│      │  │ #ORD120 - 20/05              │   │                        │
│      │  │ Dịch vụ Tắm + Cắt tỉa      │   │                        │
│      │  │ 💰 350,000đ ✓ Đã hoàn thành │   │                        │
│      │  └──────────────────────────────┘   │                        │
│      │─────────────────────────────────────│                        │
│      │  Bấm vào đơn → OrderDetailActivity  │                        │
│      │  ┌──────────────────────────────┐   │                        │
│      │  │ Chi tiết đơn #ORD125        │   │                        │
│      │  │ Ngày: 25/05/2026 14:30      │   │                        │
│      │  │ Hạt Royal Canin 15kg x 2   │   │                        │
│      │  │         Đơn giá: 350,000   │   │                        │
│      │  │ Pate Whiskas x 5            │   │                        │
│      │  │         Đơn giá: 20,000    │   │                        │
│      │  │─────────────────────────    │   │                        │
│      │  │ Tạm tính:       800,000đ   │   │                        │
│      │  │ Giảm giá(PET10): -80,000đ │   │                        │
│      │  │ Tổng cộng:     720,000đ   │   │                        │
│      │  │ Hình thức: Tiền mặt        │   │                        │
│      │  └──────────────────────────────┘   │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (2) Bottom Navigation: Bấm [📊 Thống kê]                           │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Thống kê của tôi                 │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Nguyễn Văn C                       │                        │
│      │  ⭐ Hạng: Bạc                       │                        │
│      │  🎯 1,200 / 3,000 điểm → Vàng      │                        │
│      │  [████████░░░░░░░░░░] 40%           │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Chi tiêu 6 tháng gần nhất:        │                        │
│      │  ┌───┬───┬───┬───┬───┬───┐       │                        │
│      │  │███│██ │███│ █ │ █ │███│       │                        │
│      │  │███│██ │███│ █ │ █ │███│       │                        │
│      │  └───┴───┴───┴───┴───┴───┘       │                        │
│      │   T5  T4  T3  T2  T1  T12        │                        │
│      │  2.1 1.5 1.8 0.5 0.3 2.5 (triệu) │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Dịch vụ đã sử dụng:               │                        │
│      │  🛁 Tắm               8 lần    40% │                        │
│      │  ✂️ Cắt tỉa           4 lần    20% │                        │
│      │  💉 Khám              2 lần    10% │                        │
│      │  💊 Tiêm              1 lần     5% │                        │
│      │  🛒 Mua sản phẩm      5 lần    25% │                        │
│      │                                     │                        │
│      │  💰 Tổng chi tiêu: 8,700,000đ      │                        │
│      │  📅 Lần cuối: 25/05/2026           │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (3) === CÁCH TÍNH TOÁN TRONG CODE ===                              │
│                                                                      │
│  // CustomerStatisticViewModel.java                                  │
│  public LiveData<CustomerStatistic> loadStatistic(int customerId) { │
│      return repository.getStatistic(customerId);                     │
│  }                                                                   │
│                                                                      │
│  // CustomerRepository.java                                          │
│  public LiveData<CustomerStatistic> getStatistic(int customerId) {  │
│      MutableLiveData<CustomerStatistic> result = new MutableLiveData<>(); │
│                                                                      │
│      // 1. Tính tổng chi tiêu theo tháng                            │
│      List<MonthlySpending> monthly = orderDao                       │
│          .getMonthlySpending(customerId);                           │
│                                                                      │
│      // 2. Đếm số lần sử dụng từng dịch vụ                         │
│      List<ServiceUsageCount> services = appointmentDao              │
│          .countServiceUsage(customerId);                            │
│                                                                      │
│      // 3. Lấy tổng điểm tích lũy                                   │
│      int totalPoints = loyaltyPointDao                              │
│          .getTotalPoints(customerId);                               │
│                                                                      │
│      result.setValue(new CustomerStatistic(monthly, services, points)); │
│      return result;                                                  │
│  }                                                                   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Luồng 3d: Đánh giá dịch vụ

```
┌─────────────────────────────────────────────────────────────────────┐
│  CUSTOMER: Đánh giá dịch vụ sau khi sử dụng                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  (1) Bottom Navigation: Bấm [⭐ Đánh giá]                            │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Đánh giá dịch vụ                 │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Dịch vụ đã hoàn thành:             │                        │
│      │  ┌──────────────────────────────┐   │                        │
│      │  │ 25/05 - Tắm Cún Miu         │   │                        │
│      │  │ [⭐⭐⭐⭐⭐] Rất hài lòng!    │   │                        │
│      │  ├──────────────────────────────┤   │                        │
│      │  │ 20/05 - Cắt tỉa Cún Miu    │   │                        │
│      │  │ [         Đánh giá ngay   ]│   │                        │
│      │  ├──────────────────────────────┤   │                        │
│      │  │ 15/05 - Khám Mèo Mun       │   │                        │
│      │  │ [⭐⭐⭐] Tạm ổn              │   │                        │
│      │  └──────────────────────────────┘   │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (2) Bấm [Đánh giá ngay] → ReviewActivity                          │
│      ┌─────────────────────────────────────┐                        │
│      │  ← Đánh giá dịch vụ       [Gửi]    │                        │
│      ├─────────────────────────────────────┤                        │
│      │  Dịch vụ: 🛁 Tắm cho thú cưng      │                        │
│      │  Thú cưng: 🐶 Cún Miu              │                        │
│      │  Ngày: 20/05/2026                  │                        │
│      │  Nhân viên phục vụ: Nguyễn Văn A  │                        │
│      │                                     │                        │
│      │  Bạn có hài lòng không?            │                        │
│      │  ★ ★ ★ ★ ★                         │                        │
│      │  (Chạm để đánh giá sao)            │                        │
│      │                                     │                        │
│      │  Nhận xét của bạn:                 │                        │
│      │  ┌──────────────────────────────┐  │                        │
│      │  │ Nhân viên phục vụ rất nhiệt  │  │                        │
│      │  │ tình, Cún Miu được tắm sạch, │  │                        │
│      │  │ thơm lâu. Sẽ quay lại!       │  │                        │
│      │  └──────────────────────────────┘  │                        │
│      │                                     │                        │
│      │  [[Hình ảnh đính kèm]] 📸         │                        │
│      │                                     │                        │
│      │  💡 Đánh giá nhận +10 điểm thưởng │                        │
│      └─────────────────────────────────────┘                        │
│                                                                      │
│  (3) Bấm [Gửi] → ReviewDao.insert(review)                          │
│      → ✅ "Cảm ơn bạn đã đánh giá!"                                │
│      → LoyaltyPointDao.insert(customerId, +10, "review")          │
│      → Cập nhật tổng điểm Customer                                 │
│      → Quay lại danh sách, thấy trạng thái đã đánh giá              │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 4. RULE: CROSS-CUTTING — Quên mật khẩu (Forgot Password)

### Luồng: Khách hàng quên mật khẩu

```
CUSTOMER                          HỆ THỐNG
     │                                   │
     │ (1) Bấm [Quên mật khẩu]           │
     │──────────────────────────────────►│
     │◄──────────────────────────────────│
     │  Màn hình nhập SĐT:               │
     │  ┌──────────────────────────┐     │
     │  │ Số điện thoại:           │     │
     │  │ [0912345678       ]     │     │
     │  │                         │     │
     │  │ [  GỬI MÃ XÁC THỰC  ]  │     │
     │  └──────────────────────────┘     │
     │                                   │
     │ (2) Nhập SĐT → Bấm [Gửi]         │
     │──────────────────────────────────►│── CustomerDao.findByPhone(phone)
     │                                  │   Nếu không tìm thấy → "SĐT chưa đăng ký"
     │◄──────────────────────────────────│
     │  ✅ Mã OTP đã gửi đến SĐT của bạn│
     │                                   │
     │ (3) Nhập mã OTP:                  │
     │  ┌──────────────────────────┐     │
     │  │ Mã xác thực:             │     │
     │  │ [ _ _ _ _ _ _ ]          │     │
     │  │                         │     │
     │  │ [  XÁC NHẬN  ]          │     │
     │  └──────────────────────────┘     │
     │                                   │
     │ (4) Nhập mật khẩu mới:            │
     │  ┌──────────────────────────┐     │
     │  │ Mật khẩu mới:            │     │
     │  │ [●●●●●●●●       ]      │     │
     │  │ Nhập lại:                │     │
     │  │ [●●●●●●●●       ]      │     │
     │  │                         │     │
     │  │ [  ĐẶT LẠI MẬT KHẨU  ]│     │
     │  └──────────────────────────┘     │
     │                                   │
     │ (5) Bấm [Đặt lại]                 │
     │──────────────────────────────────►│── CustomerDao.updatePassword(phone, newPass)
     │                                   ├── PasswordUtils.hash(newPass)  // SHA-256 + Salt
     │                                   ├── ActivityLogDao.insert('DOI_MAT_KHAU', ...)
     │◄──────────────────────────────────│
     │  ✅ Đặt lại mật khẩu thành công!  │
     │  Mời bạn đăng nhập lại            │
```

---

## 5. RULE: CUSTOMER — Hủy lịch hẹn (Cancel Appointment)

```
CUSTOMER                          HỆ THỐNG
     │                                   │
     │ (1) Vào "Lịch sử" → Tab Lịch hẹn│
     │──────────────────────────────────►│── AppointmentDao.getByCustomer()
     │◄──────────────────────────────────│
     │                                   │
     │ (2) Chọn lịch hẹn sắp tới         │
     │  ┌──────────────────────────┐     │
     │  │ 📅 28/05 14:00 - Tắm    │     │ (sắp tới)
     │  │   Cún Miu               │     │
     │  │   [Hủy lịch] [Dời lịch]│     │
     │  └──────────────────────────┘     │
     │                                   │
     │ (3) Bấm [Hủy lịch]                │
     │──────────────────────────────────►│
     │  Màn hình xác nhận:              │
     │  ┌──────────────────────────┐     │
     │  │ Xác nhận hủy lịch hẹn?  │     │
     │  │ Dịch vụ: Tắm cho Cún Miu│     │
     │  │ Thời gian: 28/05 14:00  │     │
     │  │                         │     │
     │  │ ○ Hủy trước 2h → Miễn   │     │
     │  │ ○ Hủy sau 2h → Mất phí  │     │
     │  │                         │     │
     │  │ Lý do hủy:              │     │
     │  │ [Bận đột xuất     ]    │     │
     │  │                         │     │
     │  │ [  XÁC NHẬN HỦY  ]     │     │
     │  └──────────────────────────┘     │
     │                                   │
     │ (4) Bấm [Xác nhận hủy]            │
     │──────────────────────────────────►│
     │                                   ├── AppointmentDao.updateStatus(id, 'cancelled')
     │                                   ├── Nếu đã gán nhân viên:
     │                                   │   └── AppointmentStaffDao.updateStatus(id, 'huy')
     │                                   ├── ActivityLogDao.insert('HUY_LICH', ...)
     │◄──────────────────────────────────│
     │  ✅ Đã hủy lịch hẹn               │
     │  Lịch hẹn đã chuyển sang "Đã hủy"│
```

---

## Tổng hợp luồng theo Rule

```
┌────────────────────────────────────────────────────────────────────┐
│                    SƠ ĐỒ TỔNG THỂ                                    │
│                                                                      │
│  ┌──────────────────────────────────────────────────────┐          │
│  │              MÀN HÌNH ĐĂNG NHẬP                        │          │
│  │  ┌──────┐  ┌───────────┐  ┌───────────────────┐    │          │
│  │  │admin │  │staff_user │  │  0912345678       │    │          │
│  │  └──────┘  └───────────┘  └───────────────────┘    │          │
│  └────────────────────┬───────────────────────────────┘          │
│                        │                                           │
│         ┌──────────────┼──────────────┐                           │
│         ▼              ▼              ▼                            │
│  ┌──────────┐  ┌────────────┐  ┌──────────────┐                  │
│  │ ADMIN    │  │  STAFF     │  │  CUSTOMER    │                  │
│  │          │  │            │  │              │                  │
│  │• Quản lý │  │• Tạo đơn  │  │• Đặt lịch    │                  │
│  │  nhân    │  │• QL thú   │  │• Lịch sử     │                  │
│  │  viên    │  │• QL khách │  │• Thống kê    │                  │
│  │• Báo cáo │  │• Lịch hẹn │  │  cá nhân     │                  │
│  │• Cài đặt │  │• Nhập kho │  │• Đánh giá    │                  │
│  │• Xóa dữ  │  │• In hóa   │  │• Giỏ hàng    │                  │
│  │  liệu    │  │  đơn      │  │• Hồ sơ sức   │                  │
│  │• Xem     │  │• Thanh    │  │  khỏe thú    │                  │
│  │  nhật ký │  │  toán     │  │  cưng        │                  │
│  │  hoạt    │  │• Gán NV   │  │• Đánh giá    │                  │
│  │  động    │  │  phục vụ  │  │  dịch vụ     │                  │
│  │          │  │• Nhập kho │  │• Thanh toán  │                  │
│  │          │  │  + log    │  │• Thông báo   │                  │
│  └──────────┘  └────────────┘  └──────────────┘                  │
│        │              │               │                            │
│        └──────────────┼───────────────┘                            │
│                       ▼                                             │
│           ┌──────────────────────┐                                 │
│           │  DATABASE (23 bảng)  │                                 │
│           │  Room / SQLite        │                                 │
│           └──────────────────────┘                                 │
└────────────────────────────────────────────────────────────────────┘
```
