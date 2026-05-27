# Mô Tả Luồng Thực Tế Của App

> **Quy trình mua hàng tại cửa hàng thú cưng** – từ lúc khách vào đến lúc xuất hóa đơn.

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

## Các chức năng hỗ trợ (Điểm cộng)

| Chức năng | Mô tả | Áp dụng |
|---|---|---|
| **Phân quyền** | Admin: full quyền; Staff: chỉ bán hàng, không xóa | User.role |
| **Mã hóa mật khẩu** | Dùng SHA-256 + Salt khi lưu password | User.java |
| **Tìm kiếm nâng cao** | Lọc thú cưng theo loại, giá, trạng thái | PetViewModel |
| **Sao lưu** | Export database ra file .db vào thẻ nhớ | AppDatabase backup |
| **Mã vạch/QrCode** | Tạo mã QR cho sản phẩm (dùng thư viện ZXing) | Product |

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
