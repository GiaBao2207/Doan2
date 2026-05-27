# Thiết Kế Cơ Sở Dữ Liệu

## Mô hình ERD (Entity-Relationship Diagram)

```
┌─────────────┐     ┌──────────────────────────┐     ┌──────────────┐
│    User     │     │   Customer (có đăng nhập) │     │   Supplier   │
├─────────────┤     ├──────────────────────────┤     ├──────────────┤
│ userId (PK) │     │ customerId(PK)           │     │ supplierId   │
│ username    │     │ fullName                  │     │ name         │
│ password    │     │ phone (UNIQUE)            │     │ phone        │
│ fullName    │     │ email                     │     │ email        │
│ role        │     │ address                   │     │ address      │
│ phone       │     │ password (hashed)         │     │ createdAt    │
│ createdAt   │     │ emailVerified (bool)      │     └──────────────┘
└─────────────┘     │ loyaltyPoints (int)       │
                    │ membershipTier (string)  │
                    │ avatar                    │
                    │ createdAt                 │
                    └──────────┬────────────────┘
                            │                     │
┌──────────────────┐        │                     │
│  PetCategory     │        │                     │
├──────────────────┤        │                     │
│ petCategoryId(PK)│        │                     │
│ name             │        │                     │
│ description      │        │                     │
└────────┬─────────┘        │                     │
         │                  │                     │
         ▼                  ▼                     │
┌──────────────────────────────────┐              │
│             Pet                  │              │
├──────────────────────────────────┤              │
│ petId (PK)                       │              │
│ name                             │              │
│ breed                            │              │
│ age                              │              │
│ weight                           │              │
│ color                            │              │
│ price                            │              │
│ status (available/sold/grooming) │              │
│ petCategoryId (FK → PetCategory) │              │
│ customerId (FK → Customer)       │              │
│ createdAt                        │              │
└──────────────────────────────────┘              │
                                                  │
┌──────────────────────┐         ┌────────────────┴──────────────┐
│  ProductCategory     │         │          Product              │
├──────────────────────┤         ├───────────────────────────────┤
│ productCategoryId(PK)│         │ productId (PK)                │
│ name                 │         │ name                          │
│ description          │         │ productCategoryId (FK→Cat)    │
└──────────┬───────────┘         │ supplierId (FK→Supplier)      │
           │                     │ price                         │
           ▼                     │ costPrice                     │
┌──────────────────────────────────────┐                         │
│              Product                 │ quantity                  │
├──────────────────────────────────────┤ unit                       │
│ (Như trên)                           │ description               │
│ productId (PK)                       │ createdAt                 │
│ ...                                  └───────────────────────────┘
│ supplierId (FK→Supplier)
└──────────────────────────────────────┘

┌──────────────────────────┐     ┌──────────────────────────┐
│     Appointment          │     │      Service             │
├──────────────────────────┤     ├──────────────────────────┤
│ appointmentId (PK)       │     │ serviceId (PK)           │
│ petId (FK→Pet)           │     │ name                     │
│ customerId (FK→Customer) │     │ description              │
│ serviceId (FK→Service)   │     │ price                    │
│ date                     │     │ duration                 │
│ timeSlot                 │     │ createdAt                │
│ status (pending/done/…)  │     └──────────────────────────┘
│ notes                    │
│ createdAt                │
└──────────────────────────┘

┌──────────────────────────┐     ┌──────────────────────────┐
│        Order             │     │      OrderDetail         │
├──────────────────────────┤     ├──────────────────────────┤
│ orderId (PK)             │     │ orderDetailId (PK)       │
│ customerId (FK→Customer) │     │ orderId (FK→Order)       │
│ userId (FK→User)         │     │ productId (FK→Product)   │
│ orderDate                │     │ quantity                 │
│ totalAmount              │     │ unitPrice                │
│ discount                 │     │ subtotal                 │
│ paymentMethod            │     └──────────────────────────┘
│ status (pending/paid/…)  │
│ notes                    │
└──────────────────────────┘

┌──────────────────────────┐     ┌──────────────────────────┐
│      Inventory           │     │   InventoryDetail        │
├──────────────────────────┤     ├──────────────────────────┤
│ inventoryId (PK)         │     │ inventoryDetailId (PK)   │
│ supplierId (FK→Supplier) │     │ inventoryId (FK→Inv)     │
│ userId (FK→User)         │     │ productId (FK→Product)   │
│ entryDate                │     │ quantity                 │
│ totalCost                │     │ unitCost                 │
│ note                     │     │ subtotal                 │
└──────────────────────────┘     └──────────────────────────┘

┌──────────────────────────┐
│       Promotion          │
├──────────────────────────┤
│ promotionId (PK)         │
│ code (unique)            │
│ description              │
│ discountPercent          │
│ startDate                │
│ endDate                  │
│ minOrderValue            │
│ status (active/expired)  │
│ createdAt                │
└──────────────────────────┘

┌──────────────────────────┐     ┌──────────────────────────────┐
│        Review            │     │     PetHealthRecord          │
├──────────────────────────┤     ├──────────────────────────────┤
│ reviewId (PK)            │     │ recordId (PK)                │
│ customerId (FK→Customer) │     │ petId (FK→Pet)               │
│ serviceId (FK→Service)   │     │ recordDate                   │
│ orderId (FK→Order)       │     │ recordType (vaccine/checkup) │
│ rating (1-5)             │     │ description                  │
│ comment                   │     │ vetName                      │
│ createdAt                │     │ nextDueDate                  │
│ status (shown/hidden)    │     │ createdAt                    │
└──────────────────────────┘     └──────────────────────────────┘

┌──────────────────────────┐     ┌──────────────────────────┐
│     LoyaltyPoint         │     │   CustomerNotification   │
├──────────────────────────┤     ├──────────────────────────┤
│ pointId (PK)             │     │ notificationId (PK)      │
│ customerId (FK→Customer) │     │ customerId (FK→Customer) │
│ points                   │     │ title                    │
│ type (earn/burn)         │     │ message                  │
│ referenceType            │     │ type (reminder/promo/…)  │
│ referenceId              │     │ isRead (bool)            │
│ description              │     │ createdAt                │
│ createdAt                │     └──────────────────────────┘
└──────────────────────────┘

┌──────────────────────────┐     ┌──────────────────────────┐
│         Cart             │     │      FavoritePet         │
├──────────────────────────┤     ├──────────────────────────┤
│ cartId (PK)              │     │ favoriteId (PK)          │
│ customerId (FK→Customer) │     │ customerId (FK→Customer) │
│ productId (FK→Product)   │     │ petId (FK→Pet)           │
│ quantity                 │     │ createdAt                │
│ addedAt                  │     └──────────────────────────┘
└──────────────────────────┘
```

---

## Danh sách bảng (Tables)

| STT | Tên bảng | Mô tả | Ghi chú |
|---|---|---|---|---|
| 1 | **User** | Người dùng hệ thống (Admin, Staff) | Có phân quyền |
| 2 | **PetCategory** | Loại thú cưng (Chó, Mèo, Hamster,...) | Danh mục cha |
| 3 | **Pet** | Thú cưng | Có FK → PetCategory, Customer |
| 4 | **Customer** | Khách hàng (có đăng nhập) | Có password, membershipTier |
| 5 | **ProductCategory** | Loại sản phẩm (Thức ăn, Phụ kiện, Thuốc) | Danh mục cha |
| 6 | **Product** | Sản phẩm bán | FK → ProductCategory, Supplier |
| 7 | **Supplier** | Nhà cung cấp | |
| 8 | **Service** | Dịch vụ (Tắm, Cắt tỉa, Khám) | |
| 9 | **Appointment** | Lịch hẹn | FK → Pet, Customer, Service |
| 10 | **Order** | Đơn hàng / Hóa đơn bán | FK → Customer, User |
| 11 | **OrderDetail** | Chi tiết đơn hàng | FK → Order, Product |
| 12 | **Inventory** | Phiếu nhập kho | FK → Supplier, User |
| 13 | **InventoryDetail** | Chi tiết phiếu nhập | FK → Inventory, Product |
| 14 | **Promotion** | Khuyến mãi | |
| 15 | **Review** | Đánh giá dịch vụ | FK → Customer, Service |
| 16 | **PetHealthRecord** | Hồ sơ sức khỏe thú cưng | FK → Pet |
| 17 | **LoyaltyPoint** | Lịch sử tích điểm | FK → Customer |
| 18 | **CustomerNotification** | Thông báo cho khách | FK → Customer |
| 19 | **Cart** | Giỏ hàng online | FK → Customer, Product |
| 20 | **FavoritePet** | Thú cưng yêu thích | FK → Customer, Pet |

→ **Tổng: 20 bảng** ✅

---

## Ràng buộc toàn vẹn (Integrity Constraints)

### Khóa chính (Primary Keys)
- Mỗi bảng có cột `xxxId` là số tự tăng (AUTOINCREMENT).

### Khóa ngoại (Foreign Keys)
| Bảng | FK | Tham chiếu |
|---|---|---|
| Pet | petCategoryId | PetCategory(petCategoryId) |
| Pet | customerId | Customer(customerId) |
| Product | productCategoryId | ProductCategory(productCategoryId) |
| Product | supplierId | Supplier(supplierId) |
| Order | customerId | Customer(customerId) |
| Order | userId | User(userId) |
| OrderDetail | orderId | Order(orderId) |
| OrderDetail | productId | Product(productId) |
| Appointment | petId | Pet(petId) |
| Appointment | customerId | Customer(customerId) |
| Appointment | serviceId | Service(serviceId) |
| Inventory | supplierId | Supplier(supplierId) |
| Inventory | userId | User(userId) |
| InventoryDetail | inventoryId | Inventory(inventoryId) |
| InventoryDetail | productId | Product(productId) |
| Review | customerId | Customer(customerId) |
| Review | serviceId | Service(serviceId) |
| Review | orderId | Order(orderId) |
| PetHealthRecord | petId | Pet(petId) |
| LoyaltyPoint | customerId | Customer(customerId) |
| CustomerNotification | customerId | Customer(customerId) |
| Cart | customerId | Customer(customerId) |
| Cart | productId | Product(productId) |
| FavoritePet | customerId | Customer(customerId) |
| FavoritePet | petId | Pet(petId) |

### Ràng buộc NOT NULL
- Tất cả các cột `xxxId` (PK, FK)
- User: username, password, role
- Pet: name, status
- Customer: fullName, phone, password
- Product: name, price, quantity
- Order: orderDate, totalAmount
- OrderDetail: quantity, unitPrice
- Appointment: date, timeSlot
- Review: rating, comment
- PetHealthRecord: recordDate, recordType

### Ràng buộc UNIQUE
- User.username
- Customer.phone, Customer.email
- Promotion.code

### Ràng buộc DEFAULT & CHECK
- Pet.status DEFAULT 'available'
- Order.status DEFAULT 'pending'
- Order.paymentMethod DEFAULT 'cash'
- Product.quantity DEFAULT 0
- Promotion.status DEFAULT 'active'
- Customer.membershipTier DEFAULT 'Bronze'
- Review.rating CHECK (1-5)
- CustomerNotification.isRead DEFAULT false
- LoyaltyPoint.type IN ('earn', 'burn')
- PetHealthRecord.recordType IN ('vaccine', 'checkup', 'treatment')

---

## Mối quan hệ Master-Detail

### 1. Order → OrderDetail (Form chính - phụ)
```
Order (Master)                     OrderDetail (Detail)
┌────────────────────┐             ┌────────────────────┐
│ orderId (PK)       │◄────────────│ orderId (FK)       │
│ customerId (FK)    │    1 : N    │ productId (FK)     │
│ orderDate          │             │ quantity           │
│ totalAmount        │             │ unitPrice          │
│ ...                │             │ subtotal           │
└────────────────────┘             └────────────────────┘
```

### 2. Inventory → InventoryDetail (Form chính - phụ)
```
Inventory (Master)                 InventoryDetail (Detail)
┌────────────────────┐             ┌────────────────────┐
│ inventoryId (PK)   │◄────────────│ inventoryId (FK)   │
│ supplierId (FK)    │    1 : N    │ productId (FK)     │
│ entryDate          │             │ quantity           │
│ totalCost          │             │ unitCost           │
│ ...                │             │ subtotal           │
└────────────────────┘             └────────────────────┘
```
