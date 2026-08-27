# PetStoreApp Design System (Terra & Sage)

Bộ tài liệu này định nghĩa hệ thống thiết kế (Design System) chuẩn hóa cho ứng dụng **PetStoreApp** dựa trên phong cách **Terra & Sage (Modern Organic)** từ Stitch. Hệ thống này được thiết kế để mang lại cảm giác thân thiện, ấm áp như gia đình, tối giản và hiện đại, tránh xa cảm giác lạnh lẽo của bệnh viện thú y.

---

## 1. Color Palette (Bảng màu)

Bảng màu lấy cảm hứng từ đất sét tự nhiên, cây cỏ xanh xô thơm và ánh nắng ấm áp.

### 1.1 Màu sắc chính thức (Semantic Colors)

| Token Name | Hex Value | Vai trò / Hướng dẫn sử dụng |
|---|---|---|
| `color_primary` | `#934524` | **Terracotta (Đất nung):** Màu thương hiệu chủ đạo, dùng cho các nút nhấn chính, tiêu đề quan trọng, trạng thái được chọn chính. |
| `color_primary_container` | `#B25D3A` | Nền phụ cho màu Primary hoặc trạng thái nhấn nhẹ. |
| `color_secondary` | `#4E6444` | **Sage Green (Xanh xô thơm):** Dùng cho các hành động phụ, nhãn thành công, phân biệt danh mục sức khỏe, dịch vụ y tế nhẹ. |
| `color_secondary_container` | `#D0EAC0` | Nền nhạt cho màu xanh Sage (phù hợp làm nền Chip unselected/success). |
| `color_accent` | `#805203` | **Amber (Hổ phách):** Màu nhấn tạo điểm nhấn cảm xúc vui tươi (quà tặng, điểm thưởng), cảnh báo nhẹ. |
| `color_background` | `#FCF9F4` | **Warm Off-White:** Màu nền chính của toàn màn hình. Tạo cảm giác dễ chịu cho mắt, giảm căng thẳng so với màu trắng tinh. |
| `color_surface` | `#FFFFFF` | **Surface (Bề mặt):** Màu nền của các thẻ (Card), ô nhập liệu (Input), hộp hội thoại nổi lên trên nền Background. |
| `color_surface_variant` | `#F0EDE9` | **Surface Variant:** Màu xám ấm phụ để làm nền Container, đường kẻ chia tách. |

### 1.2 Màu chữ (Typography Colors)

* **`text_primary` (`#2D2926` - Charcoal):** Chữ nội dung chính, tiêu đề. Đây là màu xám than tối, ấm áp, không dùng màu đen tuyền `#000000` để tránh tạo cảm giác quá tương phản gắt gao.
* **`text_secondary` (`#726A63` - Warm Gray):** Chữ phụ, caption, nhãn phụ, ngày tháng, placeholder.
* **`text_disabled` (`#A8A39F`):** Trạng thái chữ bị vô hiệu hóa.

### 1.3 Trạng thái đặc biệt (Status Colors)

* **`color_success` (`#4E6444` - Sage Green):** Trạng thái hoàn thành ca chăm sóc, thanh toán thành công, đặt lịch thành công.
* **`color_warning` (`#805203` - Amber):** Cảnh báo sắp hết hạn lô hàng, lịch hẹn chưa xác nhận quá hạn.
* **`color_error` (`#BA1A1A` - Muted Red):** Trạng thái lỗi điền form, hủy lịch hẹn, lỗi thanh toán.

---

## 2. Typography (Hệ thống chữ)

Toàn bộ ứng dụng sử dụng phông chữ **`Be Vietnam Pro`** (hoặc mặc định hệ thống `sans-serif` với tỉ lệ tương ứng):

| Style Name | Size | Line Height | Weight | Sử dụng |
|---|---|---|---|---|
| `Display` | 32sp | 40dp | Bold (700) | Tên thương hiệu, màn hình chào mừng lớn |
| `Title` | 22sp | 28dp | Semi-Bold (600) | Tiêu đề chính trên thanh công cụ/màn hình mobile |
| `SectionTitle` | 20sp | 28dp | Semi-Bold (600) | Tiêu đề các phân mục nội dung lớn trên trang |
| `Body` | 16sp | 24dp | Regular (400) | Văn bản dài, chi tiết dịch vụ, ghi chú |
| `BodySecondary` | 14sp | 20dp | Regular (400) | Văn bản thông thường, giá trị nhập liệu, mô tả phụ |
| `Button` | 14sp | 20dp | Semi-Bold (600) | Nhãn trên nút bấm chính/phụ, chữ trên thẻ Action |
| `Caption` | 12sp | 16dp | Medium (500) | Ghi chú siêu nhỏ, metadata, ngày giờ, số lượng lô |

---

## 3. Spacing System (Hệ thống khoảng cách)

Mọi khoảng cách (padding, margin) đều phải chia hết cho **`4dp`**:

* **`space_xs` (`4dp`):** Khoảng cách cực nhỏ giữa text và icon đi kèm, hoặc padding trong chip.
* **`space_sm` (`8dp`):** Khoảng cách giữa các phần tử nhỏ kề nhau (VD: Label và ô Input).
* **`space_md` (`16dp`):** Khoảng cách chuẩn giữa các thành phần trong Card, khoảng cách cột.
* **`space_lg` (`24dp`):** Khoảng cách phân chia khối lớn hoặc padding lớn của Card lớn.
* **`space_xl` (`32dp`):** Khoảng cách phân tách giữa các vùng chức năng lớn độc lập.
* **`screen_padding` (`20dp`):** Lề trái và phải bắt buộc của tất cả các màn hình di động.

---

## 4. Radius / Shape (Hệ thống bo góc)

Các góc bo tạo cảm giác thân thiện, mềm mại, không sắc nhọn:

* **`radius_small` (`4dp`):** Bo góc nhãn phụ nhỏ.
* **`radius_medium` (`8dp`):** Góc bo vừa.
* **`radius_input` (`12dp`):** Bo góc ô nhập liệu.
* **`radius_button` (`12dp`):** Bo góc nút bấm.
* **`radius_card` (`16dp`):** Bo góc tất cả các thẻ Card và Sheet hiển thị thông tin.
* **`radius_chip` (`100dp`):** Bo góc tròn tuyệt đối cho các nhãn trạng thái (Chips).

---

## 5. Component Guidelines (Thiết kế các thành phần)

### 5.1 Buttons (Nút bấm)
* **Primary Button:**
  * Màu nền: `color_primary` (`#934524`). Màu chữ: `color_surface` (`#FFFFFF`).
  * Chiều cao tối thiểu: `48dp` (đảm bảo diện tích chạm).
  * Bo góc: `radius_button` (`12dp`).
  * Trạng thái Disabled: nền xám nhẹ, chữ màu `text_disabled`.
* **Secondary Button:**
  * Màu nền: Nền xanh Sage ở mức độ mờ 10% (`#1A4E6444`).
  * Màu chữ: `color_secondary` (`#4E6444`).
  * Bo góc: `radius_button` (`12dp`).

### 5.2 Input Fields (Ô nhập liệu)
* Bề mặt: màu nền `color_surface` (`#FFFFFF`), viền màu `text_secondary` ở mức mờ 20% (`#33726A63`).
* Bo góc: `radius_input` (`12dp`).
* Khi focus: Viền chuyển sang màu `color_primary` (`#934524`).
* Chiều cao vùng nhập tối thiểu: `48dp`.

### 5.3 Cards (Thẻ thông tin)
* Màu nền: `color_surface` (`#FFFFFF`). Viền nhạt `#dbc1b8` mờ 20% hoặc đổ bóng cực mỏng (`Y: 2dp, Blur: 8dp`, màu `#2D2926` mờ 4%).
* Bo góc: `radius_card` (`16dp`).
* Padding bên trong thẻ mặc định: `space_md` (`16dp`).

### 5.4 Chips (Nhãn trạng thái)
* Nền unselected: `#D0EAC0` (Sage nhạt) mờ 12%, chữ màu xanh Sage `#4E6444`.
* Nền selected: màu xanh Sage `#4E6444` nguyên bản, chữ màu trắng `#FFFFFF`.
* Bo góc: `radius_chip` (`100dp` - tròn hoàn toàn).

---

## 6. Trạng thái UI nhất quán

* **Success State:** Dùng màu xanh Sage `#4E6444`.
* **Warning State:** Dùng màu vàng hổ phách `#805203`.
* **Error State:** Dùng màu đỏ nhạt `#BA1A1A`.
* **Empty State:** Dùng tranh minh họa nét phẳng hiện đại, tinh gọn với thông điệp rõ ràng, nền trắng ấm `#FCF9F4`.
