# Implementation Notes & Review

> Phân tích và nhận xét về thiết kế PetStoreApp dựa trên các tài liệu hiện có.

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

### 3. Thiết kế database khá toàn diện
- 20 bảng, đầy đủ khóa ngoại, ràng buộc NOT NULL, UNIQUE, DEFAULT, CHECK.
- Quan hệ Master-Detail cho Order và Inventory.

### 4. Quy trình nghiệp vụ được mô tả chi tiết
- Các file `LUONG-THUC-TE.md` và `VD-LUONG-NGUOI-DUNG.md` mô tả từng bước người dùng thao tác, rất tốt cho việc code sau này.

---

## ⚠️ Những Điểm Cần Cải Thiện / Bổ Sung

### 1. **THIẾU CODE NGUỒN** (Quan trọng nhất)
- Toàn bộ dự án mới dừng ở thiết kế, chưa có file Java, XML layout, Gradle build.
- **Cần triển khai code để đánh giá đúng chất lượng.**

### 2. Vấn đề về Database

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Thiếu bảng Employee** | `User` dùng chung cho Admin & Staff nhưng không có trường lương, ca làm, ngày vào làm | Nên tách `Employee` riêng hoặc thêm field: `salary`, `shift`, `hireDate` |
| **Không có bảng lịch sử giá** | Khi Product thay đổi giá, mất trace | Thêm `ProductPriceHistory` |
| **Pet gắn cứng customerId** | Pet chỉ thuộc 1 chủ, không hỗ trợ đồng sở hữu | Thêm bảng `PetOwner` (petId, customerId, isPrimary) |
| **Thiếu bảng Payment** | PaymentMethod là string trong Order, không trace được giao dịch | Nên tách `Payment` riêng: paymentId, orderId, method, amount, status, transactionId, paidAt |
| **Thiếu chỉ mục (Index)** | Không có index cho các cột hay truy vấn (phone, email, orderDate, status) | Thêm index cho performance |
| **Product.quantity mặc định 0** | Không phân biệt tồn kho thực tế và tồn kho khả dụng | Có thể thêm `availableQuantity`, `reservedQuantity` |

### 3. Vấn đề về Chức năng Nghiệp vụ

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Quên mật khẩu** | Được đề cập trong gợi ý nhưng không có trong thiết kế chính | Cần có luồng "Quên mật khẩu" với OTP |
| **Không có log hoạt động (Audit Trail)** | Không ghi lại ai đã làm gì, lúc nào | Thêm bảng `ActivityLog` để trace (VD: ai tạo đơn, ai sửa giá, ai xóa pet) |
| **Xuất Excel / PDF** | Chỉ nhắc đến ở Admin report nhưng chưa có thiết kế chi tiết | Cần spec rõ luồng export |
| **Hủy đơn hàng** | Chưa có quy trình hủy đơn (hoàn tiền, cập nhật tồn kho) | Cần state machine rõ cho Order.status |
| **Xung đột lịch hẹn** | 2 khách có thể đặt cùng giờ, cùng nhân viên | Cần kiểm tra trùng lịch, gán nhân viên phục vụ |
| **Giao hàng** | Nếu là Pet Store thực tế, có thể cần giao hàng tận nơi | Thêm `ShippingAddress`, `DeliveryStatus` |

### 4. Vấn đề về Kỹ thuật / Kiến trúc

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Staff & Customer chung DB trên cùng máy?** | Tài liệu nói "cả 2 app cùng truy cập 1 DB" → trên Android mỗi app có sandbox riêng, không thể share DB trực tiếp | Cần backend server (REST API) hoặc dùng ContentProvider. Với đồ án thì có thể coi như 2 module trong 1 project APK |
| **Không có lớp Service / UseCase** | ViewModel gọi thẳng Repository, thiếu tầng xử lý nghiệp vụ phức tạp | Nên thêm `UseCase` / `Interactor` giữa ViewModel và Repository |
| **Threading** | Room yêu cầu không chạy trên main thread, cần xử lý bất đồng bộ | Cần dùng AsyncTask/Coroutines/RxJava + LiveData |
| **Không đề cập Dependency Injection** | Không dùng Hilt/Dagger/Koin → code khó maintain | Nên dùng Hilt (Android khuyến nghị) |
| **Không đề cập Navigation** | Không có Component/Graph cho luồng màn hình | Nên dùng Android Navigation Component |
| **Không có Unit Test / UI Test** | Không có đề cập đến testing | Nên có ít nhất Unit Test cho ViewModel + Repository |

### 5. Vấn đề về UX / UI

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Chưa có thiết kế offline-first** | Mất mạng là không dùng được | Cơ chế cache + sync khi có network |
| **Ngôn ngữ giao diện** | Chỉ có tiếng Việt? | Nên hỗ trợ i18n (ít nhất VN + EN) |
| **Loading / Error state** | Thiết kế chưa đề cập xử lý loading, empty, error state | Cần thêm vào mọi màn hình |
| **Pagination** | Danh sách dài (sản phẩm, đơn hàng) không có phân trang | Nên thêm limit/offset hoặc Paging 3 |

### 6. Vấn đề về Bảo mật

| Vấn đề | Mô tả | Đề xuất |
|--------|-------|---------|
| **Mật khẩu** | Nói dùng SHA-256 + Salt nhưng Room DB local lưu pass là không an toàn nếu có nhiều user | Pass local có thể chấp nhận với đồ án, nhưng thực tế cần backend + JWT |
| **Phân quyền thô** | Chỉ dùng role string, dễ sai | Nên dùng enum + Permission-based access control |
| **SQL Injection** | Room an toàn về mặt này, nhưng cần lưu ý nếu có raw query | Sử dụng parameterized queries |

---

## 🧮 So Sánh Với Thực Tế

### Cửa hàng thú cưng thực tế cần thêm:
- **Quản lý giống / nhân giống** (breeding management)
- **Quản lý chuồng / lồng** (cage management)
- **Quản lý lịch tiêm chủng định kỳ + gửi thông báo**
- **Bảo hành thú cưng** (sức khỏe 7-30 ngày sau khi mua)
- **Hợp đồng mua bán thú cưng** (giấy tờ pháp lý)
- **Quản lý thuốc / vật tư y tế** (expiry date, lot number)
- **Dashboard realtime** (số khách trong cửa hàng, công việc đang chờ)

### Phù hợp với đồ án không?
- **Rất phù hợp** với yêu cầu đồ án môn học (≥10 ĐT, ≥2 QT, ≥10 form, master-detail, report).
- Các tính năng hiện tại đã đáp ứng vượt yêu cầu đồ án.
- Cần tập trung **code sạch, đúng MVVM, chạy được, ít bug** hơn là thêm quá nhiều tính năng.

---

## Khuyến Nghị

### Priority 1 (Bắt buộc để chạy được)
1. Viết code Entities + DAOs + Database
2. Viết Repositories
3. Viết ViewModels cho các màn hình chính (Login, Dashboard, Order, Pet, Customer)
4. UI cơ bản (XML layout) cho các form chính
5. Đảm bảo CRUD hoạt động đúng

### Priority 2 (Nên có)
6. Form Master-Detail: Order + OrderDetail
7. Form Master-Detail: Inventory + InventoryDetail
8. Statistic / Report (biểu đồ cột đơn giản)
9. Tích điểm + Phân hạng thành viên
10. Customer App: Đặt lịch, lịch sử, đánh giá

### Priority 3 (Điểm cộng)
11. Phân quyền chi tiết
12. Tìm kiếm nâng cao + Lọc
13. Sao lưu database
14. Mã hóa mật khẩu
15. Thông báo push (FCM)

---

## Kết Luận

| Tiêu chí | Đánh giá |
|----------|----------|
| Phân tích nghiệp vụ | ✅ **Tốt** - bao phủ rộng, chi tiết |
| Thiết kế database | ✅ **Khá tốt** - 20 bảng, FK, constraints |
| Kiến trúc | ✅ **Phù hợp MVVM** - nhưng thiếu UseCase layer |
| Code nguồn | ❌ **Chưa có** - cần triển khai |
| Bảo mật | ⚠️ **Cơ bản** - cần cải thiện nếu production |
| UX / UI | ⚠️ **Cần bổ sung** loading/error/empty state |
| Khả năng mở rộng | ⚠️ **Có thể tốt hơn** - DI, Navigation, testing |
| Phù hợp đồ án | ✅ **Vượt yêu cầu** - đủ điểm tối đa nếu code tốt |

> **Tổng thể:** Thiết kế rất tốt và phù hợp cho đồ án. Điểm yếu duy nhất là chưa có code nguồn. Cần tập trung code đúng thiết kế, xử lý tốt các state (loading/success/error) và đảm bảo CRUD + quy trình bán hàng + nhập kho chạy trơn tru.
