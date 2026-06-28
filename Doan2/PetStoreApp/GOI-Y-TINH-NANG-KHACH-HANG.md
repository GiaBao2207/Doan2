# Gợi Ý Tính Năng Thực Tế Cho Khách Hàng

> Các tính năng bổ sung giúp app khách hàng thực tế và chuyên nghiệp hơn.
> Phân loại theo mức độ ưu tiên khi làm đồ án.

---

## 🟢 NHÓM 1: Cần thiết (ưu tiên cao)

| # | Tính năng | Mô tả ngắn | Liên quan |
|---|---|---|---|
| 1 | **Đăng ký tài khoản** | Đăng ký bằng SĐT, nhận mã OTP xác thực | Customer |
| 2 | **Quên mật khẩu** | Gửi mã OTP qua SMS/email để đặt lại pass | Customer |
| 3 | **Quản lý nhiều thú cưng** | Thêm/sửa/xóa thú cưng của mình (tên, giống, tuổi, ảnh) | Pet |
| 4 | **Xem chi tiết dịch vụ** | Mô tả, giá, thời gian thực hiện, hình ảnh minh họa | Service |
| 5 | **Xem chi tiết đơn hàng** | Danh sách sản phẩm, thành tiền, trạng thái, ngày mua | OrderDetail |
| 6 | **Hủy lịch hẹn** | Hủy trước 2h không mất phí | Appointment |
| 7 | **Thông báo nhắc lịch** | Push notification trước 1 ngày và 1h | Notification |

## 🟡 NHÓM 2: Nâng cao trải nghiệm

| # | Tính năng | Mô tả ngắn | Liên quan |
|---|---|---|---|
| 8 | **🛒 Mua hàng online** | Chọn sản phẩm → thêm vào giỏ → đặt hàng → chờ xác nhận | Cart → Order |
| 9 | **🔍 Tìm kiếm sản phẩm/dịch vụ** | Tìm theo tên, danh mục, khoảng giá | Product, Service |
| 10 | **📸 Ảnh thú cưng** | Upload ảnh đại diện cho từng thú cưng | Pet.avatar |
| 11 | **⭐ Yêu thích (Wishlist)** | Lưu dịch vụ/sản phẩm yêu thích để đặt sau | FavoritePet |
| 12 | **💳 Lịch sử tích điểm** | Xem số điểm hiện tại, lịch sử tích/dùng điểm | LoyaltyPoint |
| 13 | **🎁 Đổi quà từ điểm** | Dùng điểm để đổi dịch vụ/sản phẩm miễn phí | LoyaltyPoint.burn |
| 14 | **📊 Lịch sử sức khỏe** | Xem lịch tiêm, tái khám, ghi chú bác sĩ | PetHealthRecord |
| 15 | **🔔 Nhắc tiêm chủng** | Tự động gửi thông báo khi gần đến hạn tiêm | PetHealthRecord + Notif |
| 16 | **🎂 Sinh nhật thú cưng** | Gửi lời chúc + mã giảm giá vào ngày sinh nhật pet | Notification + Promo |

## 🟠 NHÓM 3: Cao cấp (điểm cộng)

| # | Tính năng | Mô tả ngắn | Liên quan |
|---|---|---|---|
| 17 | **📱 Thanh toán online** | Tích hợp VNPay, Momo, ZaloPay | Payment (bảng ThanhToán) |
| 18 | **💬 Chat với cửa hàng** | Gửi tin nhắn hỏi về dịch vụ, sản phẩm, lịch hẹn | Chat (table mới) |
| 19 | **📍 Bản đồ cửa hàng** | Xem vị trí cửa hàng, chỉ đường (Google Maps) | Google Maps API |
| 20 | **📦 Theo dõi đơn hàng** | Real-time: đã xác nhận → đang giao → đã nhận | Order.status |
| 21 | **📅 Đặt lịch định kỳ** | Đặt lịch tắm/grooming hàng tuần/tháng tự động | Appointment.recurring |
| 22 | **📝 Phiếu khám sức khỏe PDF** | Xuất phiếu khám dạng PDF gửi qua email | PetHealthRecord + Export |
| 23 | **🎯 Gợi ý thông minh** | Gợi ý dịch vụ dựa trên giống thú cưng, lịch sử | Recommendation engine |
| 24 | **🏆 Minigame / Check-in** | Check-in hàng ngày nhận điểm thưởng | LoyaltyPoint |
| 25 | **👨‍👩‍👧 Chia sẻ thú cưng** | Cho phép nhiều người trong gia đình cùng quản lý 1 pet | Pet.sharedWith |

---

## So sánh với thang điểm

| Nhóm tính năng | Thang điểm tương ứng |
|---|---|
| Nhóm 1 (1-7) | Đáp ứng đủ các chức năng đối tượng + quy trình cơ bản |
| Nhóm 2 (8-16) | Nâng cao chất lượng, có report/thống kê + form master-detail |
| Nhóm 3 (17-25) | **Điểm cộng**: chức năng hỗ trợ thêm (mã hóa, sao lưu, tích hợp…) |

> **Khuyến nghị cho đồ án:** Làm Nhóm 1 (bắt buộc) + chọn 3-5 tính năng từ Nhóm 2 + 1-2 tính năng từ Nhóm 3.

---

## Ví dụ: Luồng Mua hàng online (tính năng #8)

```
KHÁCH                    HỆ THỐNG
  │                          │
  │ (1) Xem sản phẩm         │
  │─────────────────────────►│── ProductDao.getAll()
  │◄─────────────────────────│
  │                          │
  │ (2) Thêm vào giỏ hàng   │
  │─────────────────────────►│── CartDao.insert()
  │                          │   (kiểm tra trùng, tăng qty)
  │◄─────────────────────────│
  │                          │
  │ (3) Vào giỏ hàng         │
  │─────────────────────────►│── CartDao.getByCustomer()
  │◄─────────────────────────│
  │  Hiển thị:               │
  │  • Hạt Royal Canin x 2   │
  │  • Đồ chơi mèo x 1       │
  │  • Tổng: 560,000đ        │
  │                          │
  │ (4) Nhập mã giảm giá     │
  │ "PET10"                  │
  │─────────────────────────►│── PromotionDao.check()
  │◄─────────────────────────│
  │  Giảm 10%: -56,000đ     │
  │  Tạm tính: 504,000đ    │
  │                          │
  │ (5) Đặt hàng             │
  │─────────────────────────►│── OrderRepository.placeOrder()
  │                          │   (tạo Order, OrderDetail, xóa Cart)
  │◄─────────────────────────│
  │  ✅ Đặt hàng thành công  │
  │  Mã đơn: #ORD-2026-0527 │
  │                          │
  │ (6) Nhận thông báo       │
  │◄─────────────────────────│
  │  "Đơn hàng #ORD-2026-   │
  │   0527 đang được xử lý"  │
```

---

## Ví dụ: Hồ sơ sức khỏe + Nhắc tiêm (tính năng #14, #15)

```
┌─────────────────────────────────┐
│  ← Hồ sơ sức khỏe: Cún Miu   │
├─────────────────────────────────┤
│  🐶 Cún Miu - Poodle           │
│  Ngày sinh: 15/03/2024 (2 tuổi)│
│                                 │
│  [Lịch tiêm]                    │
│  ┌──────────────────────────┐  │
│  │✅ Dại (15/03/2025)       │  │
│  │✅ 7 bệnh (15/01/2025)    │  │
│  │⬜ Cúm (🔔 Quá hạn 2 ngày)│  │ ⬅ auto-notify
│  └──────────────────────────┘  │
│                                 │
│  [Lịch sử khám]                │
│  ┌──────────────────────────┐  │
│  │20/05 Viêm da - 350k      │  │
│  │10/03 Khám tổng quát - 0đ│  │
│  └──────────────────────────┘  │
│                                 │
│  [Lịch hẹn sắp tới]            │
│  🔔 28/05 - Tái khám da liễu  │
│                                 │
│  [Xuất phiếu sức khỏe PDF]    │
└─────────────────────────────────┘
```
