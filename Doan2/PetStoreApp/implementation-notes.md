# Implementation Notes & Review

> Phân tích và nhận xét về thiết kế PetStoreApp.  
> **Lưu ý:** File này được cập nhật sau mỗi lần thay đổi thiết kế.

## Tổng Quan

Dự án là một ứng dụng Android quản lý cửa hàng thú cưng với **2 app riêng** (Staff + Customer) dùng chung database, kiến trúc MVVM, ngôn ngữ Java, Room database.

**Trạng thái hiện tại:** Mới chỉ có tài liệu thiết kế (`.md`), chưa có code nguồn (Java/XML).

---

## ✅ Những Điểm Làm Tốt

### 1. Phân tích nghiệp vụ đầy đủ
- Bao phủ hầu hết nghiệp vụ của cửa hàng thú cưng thực tế: bán hàng, nhập kho, lịch hẹn, dịch vụ, khuyến mãi, tích điểm, đánh giá.
- Phân vai rõ ràng: Admin / Staff / Customer với quyền hạn khác nhau.
- Có cả **bán tại quầy** (Staff app) và **kênh online** (Customer app).

### 2. Kiến trúc MVVM hợp lý
- Phân tách rõ View → ViewModel → Repository → DAO → Database.
- Sử dụng LiveData để cập nhật UI tự động.

### 3. Thiết kế database toàn diện (đã cải thiện)
- Từ **20 bảng** nâng lên **23 bảng** — thêm `ThanhToán`, `NhậtKýHoạtĐộng`, `NhânViênPhụcVụ`.
- Đầy đủ khóa ngoại, ràng buộc NOT NULL, UNIQUE, DEFAULT, CHECK.
- 4 quan hệ Master-Detail (Order, Inventory, ThanhToán, NhânViênPhụcVụ).
- Toàn bộ tên bảng đã đổi sang tiếng Việt.

### 4. Biểu đồ Use Case đầy đủ
- Đã bổ sung **32 use case** có ma trận phân theo 3 role.
- Mô tả chi tiết 3 use case quan trọng nhất: Tạo đơn hàng, Đặt lịch hẹn, Nhập kho.

### 5. Quy trình nghiệp vụ được mô tả chi tiết
- Các file `LUONG-THUC-TE.md` và `VD-LUONG-NGUOI-DUNG.md` mô tả từng bước người dùng thao tác, rất tốt cho việc code sau này.

---

## ⚠️ Những Điểm Cần Cải Thiện / Bổ Sung

### 1. **THIẾU CODE NGUỒN** (Quan trọng nhất)
- Toàn bộ dự án mới dừng ở thiết kế, chưa có file Java, XML layout, Gradle build.
- **Cần triển khai code để đánh giá đúng chất lượng.**

### 2. Vấn đề về Database (đã xử lý một phần)

| Vấn đề | Trạng thái |
|--------|-----------|
| **Thiếu bảng Employee** | ⚠️ Vẫn thiếu — `User` chưa có salary, shift, hireDate |
| **Không có bảng lịch sử giá** | ❌ Vẫn thiếu — chưa thêm `ProductPriceHistory` |
| **Pet gắn cứng customerId** | ❌ Vẫn chưa hỗ trợ đồng sở hữu |
| **Thiếu bảng Payment** | ✅ **Đã thêm** — `ThanhToán` với đầy đủ fields |
| **Thiếu Audit Log** | ✅ **Đã thêm** — `NhậtKýHoạtĐộng` append-only |
| **Thiếu gán nhân viên lịch hẹn** | ✅ **Đã thêm** — `NhânViênPhụcVụ` |
| **Thiếu chỉ mục (Index)** | ❌ Vẫn chưa có index |
| **Product.quantity** | ⚠️ Chưa phân biệt tồn khả dụng / tồn thực tế |

### 3. Vấn đề về Chức năng Nghiệp vụ

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Quên mật khẩu** | Được đề cập trong gợi ý nhưng chưa có thiết kế chi tiết | Cần có luồng "Quên mật khẩu" với OTP |
| **Xuất Excel / PDF** | Chỉ nhắc đến ở Admin report | Cần spec rõ luồng export |
| **Hủy đơn hàng** | Chưa có state machine cho Order.status | Cần quy trình hủy + hoàn tiền |
| **Xung đột lịch hẹn** | Đã có NhânViênPhụcVụ nhưng chưa có cơ chế kiểm tra trùng | Cần validate ở code |
| **Giao hàng** | Đã thêm shippingAddress, deliveryStatus vào ĐơnHàng | ⚠️ Chưa có bảng riêng cho địa chỉ |

### 4. Vấn đề về Kỹ thuật / Kiến trúc

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Staff & Customer chung DB trên cùng máy?** | Tài liệu nói "cả 2 app cùng truy cập 1 DB" → trên Android mỗi app có sandbox riêng | Cần backend REST API hoặc 2 module trong 1 APK |
| **Không có lớp Service / UseCase** | ViewModel gọi thẳng Repository | Nên thêm `UseCase` giữa ViewModel và Repository |
| **Threading** | Room yêu cầu không chạy trên main thread | Cần AsyncTask/Coroutines/RxJava + LiveData |
| **Không có Dependency Injection** | Code khó maintain | Nên dùng Hilt |
| **Không có Navigation Component** | Không có graph cho luồng màn hình | Nên dùng Android Navigation Component |
| **Không có Unit Test / UI Test** | Chưa đề cập testing | Nên có ít nhất Unit Test cho ViewModel + Repository |

### 5. Vấn đề về UX / UI

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Offline-first** | Mất mạng là không dùng được | Cơ chế cache + sync |
| **Ngôn ngữ** | Chỉ có tiếng Việt | Nên hỗ trợ i18n (VN + EN) |
| **Loading / Error / Empty state** | Chưa được thiết kế | Cần thêm vào mọi màn hình |
| **Pagination** | Danh sách dài không có phân trang | Nên thêm Paging 3 |

### 6. Vấn đề về Bảo mật

| Vấn đề | Đề xuất |
|--------|---------|
| **Mật khẩu SHA-256 + Salt** | Chấp nhận được với đồ án; thực tế cần backend + JWT |
| **Phân quyền** | Nên dùng enum + Permission-based thay vì role string |
| **SQL Injection** | Room đã an toàn, lưu ý khi dùng raw query |

---

## Nhật ký thay đổi thiết kế

| Ngày | Thay đổi | File ảnh hưởng |
|------|---------|----------------|
| 28/06/2026 | Tạo implementation-notes.md ban đầu | implementation-notes.md |
| 28/06/2026 | Đánh giá: thiếu 8 bảng, cần bổ sung | implementation-notes.md |
| 28/06/2026 | Thêm 3 bảng: ThanhToán, NhậtKýHoạtĐộng, NhânViênPhụcVụ | DATABASE.md |
| 28/06/2026 | Đổi tên toàn bộ 23 bảng sang tiếng Việt | DATABASE.md |
| 28/06/2026 | Sửa ERD đầy đủ 23 bảng (3 phần) | DATABASE.md |
| 28/06/2026 | Thêm biểu đồ Use Case + 32 use case + mô tả chi tiết 3 UC chính | DATABASE.md |
| 28/06/2026 | Tạo file nhiệm-vụ.md với 5 giai đoạn, ~100 tasks | nhiệm-vụ.md |
| 28/06/2026 | Cập nhật implementation-notes.md sau các thay đổi | implementation-notes.md |
| 28/06/2026 | Cập nhật CHUC-NANG.md: 20→23 entities, DAOs, Repos, ViewModels, UI, summary | CHUC-NANG.md |
| 28/06/2026 | Cập nhật README.md: 20→23 bảng | README.md |
| 28/06/2026 | Cập nhật LUONG-THUC-TE.md: thêm Payment, ActivityLog vào các luồng | LUONG-THUC-TE.md |
| 28/06/2026 | Cập nhật VD-LUONG-NGUOI-DUNG.md: thêm Admin xem log, Staff ghi payment+log | VD-LUONG-NGUOI-DUNG.md |
| 28/06/2026 | Cập nhật GOI-Y-TINH-NANG-KHACH-HANG.md: reference đến bảng ThanhToán | GOI-Y-TINH-NANG-KHACH-HANG.md |
| 28/06/2026 | Viết lại ERD đầy đủ 23 bảng: mỗi bảng hiển thị tất cả thuộc tính + PK/FK rõ ràng | DATABASE.md |
| 28/06/2026 | Chuyển toàn bộ ERD sang SQL CREATE TABLE (sẵn code để chạy), thêm index, dọn duplicate | DATABASE.md |

---

## 🧮 So Sánh Với Thực Tế

### Cửa hàng thú cưng thực tế cần thêm:
- **Quản lý giống / nhân giống** (breeding management)
- **Quản lý chuồng / lồng** (cage management)
- **Bảo hành thú cưng** (sức khỏe 7-30 ngày sau khi mua)
- **Hợp đồng mua bán thú cưng** (giấy tờ pháp lý)
- **Dashboard realtime** (số khách trong cửa hàng, công việc đang chờ)

### Phù hợp với đồ án không?
- **Rất phù hợp** với yêu cầu đồ án môn học (≥10 ĐT, ≥2 QT, ≥10 form, master-detail, report).
- Các tính năng hiện tại đã đáp ứng vượt yêu cầu đồ án.
- Cần tập trung **code sạch, đúng MVVM, chạy được, ít bug** hơn là thêm quá nhiều tính năng.

---

## Khuyến Nghị

### Priority 1 (Bắt buộc để chạy được)
1. Viết code Entities + DAOs + Database (23 bảng)
2. Viết Repositories
3. Viết ViewModels cho các màn hình chính (Login, Dashboard, Order, Pet, Customer)
4. UI cơ bản (XML layout) cho các form chính
5. Đảm bảo CRUD hoạt động đúng

### Priority 2 (Nên có)
6. Form Master-Detail: Order + OrderDetail + ThanhToán
7. Form Master-Detail: Inventory + InventoryDetail
8. Form Master-Detail: LịchHẹn + NhânViênPhụcVụ
9. Statistic / Report (biểu đồ cột đơn giản)
10. Tích điểm + Phân hạng thành viên
11. Customer App: Đặt lịch, lịch sử, đánh giá
12. Ghi NhậtKýHoạtĐộng tự động

### Priority 3 (Điểm cộng)
13. Phân quyền chi tiết
14. Tìm kiếm nâng cao + Lọc
15. Sao lưu database
16. Mã hóa mật khẩu
17. Thông báo push (FCM)
18. Thanh toán online (VNPay / Momo)

---

## Kết Luận

| Tiêu chí | Đánh giá |
|----------|----------|
| Phân tích nghiệp vụ | ✅ **Tốt** - bao phủ rộng, chi tiết |
| Thiết kế database | ✅ **Rất tốt** - 23 bảng, đã bổ sung 3 bảng còn thiếu |
| Use Case | ✅ **Đầy đủ** - 32 use case, ma trận role, mô tả chi tiết |
| Biểu đồ / ERD | ✅ **Đã cập nhật** - ERD 23 bảng + Use Case diagram |
| Task tracking | ✅ **Có** - nhiệm-vụ.md với 5 giai đoạn |
| Kiến trúc | ✅ **Phù hợp MVVM** - nhưng thiếu UseCase layer |
| Code nguồn | ❌ **Chưa có** - cần triển khai |
| Bảo mật | ⚠️ **Cơ bản** - cần cải thiện nếu production |
| UX / UI | ⚠️ **Cần bổ sung** loading/error/empty state |
| Khả năng mở rộng | ⚠️ **Có thể tốt hơn** - DI, Navigation, testing |
| Phù hợp đồ án | ✅ **Vượt yêu cầu** - đủ điểm tối đa nếu code tốt |

> **Tổng thể:** Thiết kế đã được cải thiện đáng kể — từ 20 → 23 bảng, thêm Use Case, ERD, task list. Điểm yếu duy nhất là chưa có code nguồn. Cần tập trung code đúng thiết kế, xử lý tốt các state (loading/success/error) và đảm bảo CRUD + quy trình bán hàng + nhập kho chạy trơn tru.
