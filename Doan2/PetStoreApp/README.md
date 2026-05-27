# PetStoreApp - Quản Lý Cửa Hàng Thú Cưng

## Giới thiệu
Ứng dụng Android quản lý cửa hàng thú cưng (Pet Store Management).  
Xây dựng trên nền tảng **Android Studio**, ngôn ngữ **Java**, kiến trúc **MVVM**.

## Công nghệ sử dụng
| Công nghệ | Mục đích |
|---|---|
| Java | Ngôn ngữ lập trình chính |
| Android SDK | Xây dựng ứng dụng Android |
| MVVM Architecture | Phân tách rõ ràng Model - View - ViewModel |
| Room Database | Lưu trữ dữ liệu offline (SQLite) |
| LiveData / Observable | Cập nhật UI theo dữ liệu thời gian thực |
| RecyclerView | Hiển thị danh sách dữ liệu |
| Material Design | Giao diện người dùng |

## Cấu trúc thư mục (Package)

```
com.petstore.app/
├── model/              # Định nghĩa các đối tượng dữ liệu (POJO / Entity)
├── data/               # Tầng dữ liệu
│   ├── database/       # Room Database, DAOs
│   ├── repository/     # Repository pattern (trung gian dữ liệu)
│   └── preference/     # SharedPreferences (lưu phiên đăng nhập,...)
├── ui/                 # Tầng giao diện (Activity, Fragment, Adapter)
│   ├── login/          # Màn hình đăng nhập
│   ├── dashboard/      # Màn hình tổng quan
│   ├── pet/            # Quản lý thú cưng
│   ├── customer/       # Quản lý khách hàng
│   ├── product/        # Quản lý sản phẩm
│   ├── order/          # Quản lý đơn hàng
│   └── appointment/    # Quản lý lịch hẹn
└── viewmodel/          # Tầng ViewModel (trung gian UI - Data)
```

## Hướng dẫn cài đặt
1. Clone project về máy
2. Mở bằng Android Studio
3. Gradle Sync để tải dependencies
4. Chạy trên emulator hoặc thiết bị thật (Android 7.0+)

## Tài khoản mặc định
- Admin: `admin` / `admin123`
- Nhân viên: `staff` / `staff123`

## Yêu cầu hệ thống
- Android Studio Hedgehog (2023.1.1) trở lên
- Gradle 8.0+
- Min SDK 26, Target SDK 34
