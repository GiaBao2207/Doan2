# Quy định Thiết kế Giao diện (UI Rules) - PetStoreApp

Bộ quy định này nhằm đảm bảo tính nhất quán (Consistency) về mặt thị giác trên toàn bộ các màn hình của ứng dụng **PetStoreApp**. Tất cả lập trình viên khi phát triển giao diện Android (bằng XML hoặc Jetpack Compose) phải tuân thủ nghiêm ngặt các nguyên tắc sau:

---

## 1. Không hard-code các giá trị thị giác (No Hard-coding Visual Values)

* **Không viết mã màu HEX trực tiếp trong layout:**
  * *Sai:* `android:background="#934524"`
  * *Đúng:* `android:background="@color/color_primary"`
* **Không dùng kích thước/khoảng cách tự do:**
  * *Sai:* `android:layout_margin="15dp"`, `android:padding="13dp"`
  * *Đúng:* `android:layout_margin="@dimen/space_md"`, `android:padding="@dimen/space_md"`
* **Không hard-code giá trị bo góc (Corner Radius):**
  * *Sai:* `app:cardCornerRadius="14dp"`
  * *Đúng:* `app:cardCornerRadius="@dimen/radius_card"`
* **Không khai báo size chữ trực tiếp:**
  * *Sai:* `android:textSize="18sp"`
  * *Đúng:* `style="@style/TextAppearance.PetStore.SectionTitle"` (hoặc áp dụng qua theme).

---

## 2. Quản lý tài nguyên tập trung

* **Không tự tiện tạo màu mới cho từng màn hình:** Chỉ được phép sử dụng các mã màu có trong `colors.xml`. Nếu có yêu cầu màu đặc thù, phải xin ý kiến Lead UI/UX để cập nhật vào Design System trước.
* **Quy tắc đặt tên tài nguyên:**
  * Màu sắc phải theo chuẩn ngữ nghĩa (semantic tokens), ví dụ: `color_primary`, `text_primary`, `color_background`, không đặt tên kiểu `login_red`, `button_color_1`.
  * Khoảng cách phải theo lưới spacing hệ `space_xs`, `space_sm`, `space_md`, v.v.

---

## 3. Quy tắc ánh xạ màn hình từ Stitch

Khi chuyển đổi thiết kế của một màn hình từ Stitch (ví dụ màn hình đăng nhập) sang code Android:
1. **Layout / Bố cục:** Tuân thủ cấu trúc phân bố các phần tử (logo, tranh minh họa, input, button) từ bản vẽ Stitch.
2. **Màu sắc & Typography:** Bắt buộc sử dụng hệ thống màu và typography chuẩn trong Android Design System thay vì copy chính xác thuộc tính CSS nội bộ (Inline style) từ Stitch (ví dụ: nếu card trên Stitch dùng góc bo 24px nhưng quy chuẩn card là 16dp, ta phải dùng `@dimen/radius_card` - 16dp).
3. **Diện tích chạm (Touch Target Size):** Mọi nút bấm, trường nhập liệu, icon tương tác được phải có kích thước chạm tối thiểu là **`48dp x 48dp`** (`@dimen/touch_target_min`) để đảm bảo dễ bấm trên thực tế, kể cả khi trên Stitch trông nhỏ hơn.

---

## 4. Quản lý trạng thái UI nhất quán

* Mọi thông báo lỗi (Error) phải hiển thị bằng màu `@color/color_error`.
* Mọi thông báo thành công (Success) phải dùng màu `@color/color_success`.
* Mọi nút bấm ở trạng thái bị khóa phải tự động mờ đi và dùng màu `@color/text_disabled`.
