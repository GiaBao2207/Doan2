# Thiết Kế Cơ Sở Dữ Liệu

## Mô hình ERD (Entity-Relationship Diagram)

```
┌─────────────┐     ┌────────────────┐     ┌──────────────┐
│    User     │     │   Customer     │     │   Supplier   │
├─────────────┤     ├────────────────┤     ├──────────────┤
│ userId (PK) │     │ customerId(PK) │     │ supplierId   │
│ username    │     │ fullName       │     │ name         │
│ password    │     │ phone          │     │ phone        │
│ fullName    │     │ email          │     │ email        │
│ role        │     │ address        │     │ address      │
│ phone       │     │ createdAt      │     │ createdAt    │
│ createdAt   │     └───────┬────────┘     └──────┬───────┘
└─────────────┘             │                     │
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
```

---

## Danh sách bảng (Tables)

| STT | Tên bảng | Mô tả | Ghi chú |
|---|---|---|---|
| 1 | **User** | Người dùng hệ thống (Admin, Staff) | Có phân quyền |
| 2 | **PetCategory** | Loại thú cưng (Chó, Mèo, Hamster,...) | Danh mục cha |
| 3 | **Pet** | Thú cưng | Có FK → PetCategory, Customer |
| 4 | **Customer** | Khách hàng | |
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

→ **Tổng: 14 bảng** ✅

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

### Ràng buộc NOT NULL
- Tất cả các cột `xxxId` (PK, FK)
- User: username, password, role
- Pet: name, status
- Customer: fullName, phone
- Product: name, price, quantity
- Order: orderDate, totalAmount
- OrderDetail: quantity, unitPrice
- Appointment: date, timeSlot

### Ràng buộc UNIQUE
- User.username
- Customer.phone
- Promotion.code

### Ràng buộc DEFAULT & CHECK
- Pet.status DEFAULT 'available'
- Order.status DEFAULT 'pending'
- Order.paymentMethod DEFAULT 'cash'
- Product.quantity DEFAULT 0
- Promotion.status DEFAULT 'active'

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
