# PetStoreApp Agent Context

## 1. Project Summary

- Android app, Java + XML.
- Development strategy: UI-first.
- Main roles: Admin, Staff, Customer.
- Stitch is the UI source of truth.
- Reuse the existing PetStoreApp Android Design System.
- Room/SQLite and business/data layers planned for later (after UI phase is sufficiently complete).

## 2. Target Architecture

Target: UI → ViewModel → Repository → DAO → Room Database.

Current implementation: UI Java/XML + Design System + package skeleton only.
Backend/data layers (Entity, DAO, Repository, ViewModel, Room, AppDatabase, API, real CRUD, authentication/session, appointment logic, payment logic) are NOT implemented.

## 3. Development Workflow

Order: Design System → UI screens → UI navigation → main business screens → Entity/DAO/Room → Repository/ViewModel → business logic → integration → testing/cleanup.

## 4. Main Business Modules

auth, user, customer, employee, pet, service, appointment, product, inventory, order, payment, promotion, loyalty, notification, review, report, core.

Most are skeleton/preparation packages unless real UI source exists.

## 5. UI / Design System Rules

- Keep the existing PetStoreApp palette from Stitch.
- Do NOT create a new palette.
- Create contrast via light/dark relationships inside the existing palette.
- Dark subject → lighter background. Light subject → darker supporting surface.
- Avoid screen/card/component backgrounds being too similar.
- Cards/surfaces: subtle contrast + light border + light elevation/shadow.
- TextInput border must differ visually from its outer card/container.
- Focused TextInput: primary terracotta.
- Standalone Paw Icon: circular container, inner fill #FFFFFF, centered, no inner square.
- Full Logo: no circular container.
- Branding vs Image Placeholder Rules:
  - Header / Top App Bar branding uses clean app branding (inline brand icon + "PetStoreApp" title). Never use the circular Paw placeholder container as header branding.
  - Circular Paw Logo placeholder is strictly reserved for future data-image fallbacks (Pet Detail, Service Detail, future product images, etc.).
  - Add/Edit Pet "Thêm ảnh" remains camera/upload action UI.
  - Real data images replace placeholders when data becomes available.

## 5a. Global Logo Consistency Rules (ENFORCED)

- **Wide branding areas** (dashboard header with space for full wordmark): use `petstore_logo` (Full Logo = paw symbol + "PetStoreApp" wordmark). Example: Admin Dashboard header.
- **Compact/inline branding areas** (auth screen top-of-form icon, inline header brand mark next to title text): use `petstore_paw_icon` in `bg_brand_paw_container` or inline (no tint override). Example: Login, Register, ForgotPassword, ResetPassword, ResetPasswordSuccess, CustomerHome/MyPets/ServiceDiscovery header icon.
- **Data image placeholder** (pet image, service image, empty-state "no pets" illustration): use `petstore_paw_icon` (no tint). Example: PetDetail hero, ServiceDetail hero, CustomerHome empty-state pet card, MyPets empty-state card.
- **Semantic UI icon** (info-row icon for pet type, quick-action stat card for "My Pets" feature): use `ic_pets` — this is a feature/category icon, not branding. Example: ServiceDetail info-row "Loại thú cưng phù hợp", CustomerHome quick-action "Thú cưng của tôi".
- **Add/Edit Pet upload area**: keep camera icon + "Thêm ảnh" — do NOT replace with paw icon.
- **Bottom nav "Thú cưng" tab**: uses `ic_pets` — correct (functional nav icon, not branding).

## 6. Implemented UI Status

- **Auth UI (COMPLETED, UI-only)**:
  - LoginActivity
  - RegisterActivity
  - ForgotPasswordActivity
  - ResetPasswordActivity
  - ResetPasswordSuccessActivity
- **Admin UI (COMPLETED, UI-only)**:
  - AdminDashboardActivity (core/ui)
  - EmployeeManagementActivity (employee/ui)
  - ServiceManagementActivity (service/ui)
  - ProductManagementActivity (product/ui)
  - InventoryManagementActivity (inventory/ui)
  - PromotionManagementActivity (promotion/ui)
  - AppointmentManagementActivity (appointment/ui)
  - ReportOverviewActivity (report/ui)
- **Staff UI (COMPLETED, UI-only)**:
  - StaffAppointmentQueueActivity (appointment/ui)
  - ServiceCheckInExecutionActivity (service/ui)
  - PetHandoverActivity (appointment/ui)
  - CounterPaymentActivity (payment/ui)
- **Customer UI (COMPLETED batch 1 & 2, UI-only)**:
  - CustomerHomeActivity (customer/ui)
  - MyPetsActivity (pet/ui)
  - ServiceDiscoveryActivity (service/ui)
  - AddEditPetActivity (pet/ui)
  - PetDetailActivity (pet/ui)
  - ServiceDetailActivity (service/ui)

All screens follow the UI-only placeholder rule: 0 summary totals, empty states for lists/charts, zero fake data, no Room/DB backend.

## 7. Current Task

Global logo consistency pass completed. Ready for Customer UI batch 3 planning.

## 8. In Progress

(none)

## 9. Known Issues

(none — build passes.)

## 10. Completed

- Auth UI screens (UI-only, basic validation/navigation).
- Global color consistency sync & palette refinement (Build PASS).
- Admin Dashboard UI refinement (Build PASS): 54dp logo, 0-stats, 5 bottom nav items with modal menu, 6 quick shortcuts.
- Admin Management screens batch 1 (Employee, Service, Product) (Build PASS).
- Service Management Add Service action refinement (Build PASS).
- Admin Management screens batch 2 (Inventory, Promotion) (Build PASS).
- Admin Operations screens batch 3 (Appointment, Report Overview) (Build PASS).
- Staff Operations batch 1 (Staff Appointment Queue, Service Check-in & Execution) (Build PASS).
- Staff Operations batch 2 (Pet Handover, Counter Payment) (Build PASS).
- Gradle & Theme Lint Build Blockers Fix (lintDebug PASS, build PASS).
- Customer UI batch 1 & 2 Header Branding Fix (lintDebug PASS, build PASS).
- Customer UI batch 2 (Add / Edit Pet, Pet Detail, Service Detail) (lintDebug PASS, build PASS).
- **Global Logo Consistency Pass (lintDebug PASS, build PASS)**:
  - Audited ALL implemented screens: Auth (5), Admin (8), Staff (4), Customer batch 1 & 2 (6).
  - Fixed activity_login.xml: replaced bare `petstore_logo` (64dp) with `petstore_paw_icon` in `bg_brand_paw_container` (now consistent with all other auth screens).
  - Fixed activity_customer_home.xml: header brand icon `ic_pets` → `petstore_paw_icon`; empty-state "no pets" `ic_pets` → `petstore_paw_icon`.
  - Fixed activity_my_pets.xml: header brand icon `ic_pets` → `petstore_paw_icon`; empty-state `ic_pets` → `petstore_paw_icon`.
  - Fixed activity_service_discovery.xml: header brand icon `ic_pets` → `petstore_paw_icon`.
  - Preserved (correct): Admin Dashboard `petstore_logo` (wide header), PetDetail/ServiceDetail `petstore_paw_icon` placeholders, ServiceDetail info-row `ic_pets` (semantic), CustomerHome quick-action stat `ic_pets` (semantic), Add/Edit Pet camera upload area (unchanged), bottom nav `ic_pets` tab (functional nav icon).

## 11. Next Action

Review and design Customer UI batch 3 in Stitch:
- Choose Pet / Booking setup
- Choose Date & Time
- Booking Confirmation

## 12. Not Started

- Customer UI batch 3 (Choose Pet / Booking setup, Choose Date & Time, Booking Confirmation, Orders, Profile, etc.).
- Backend / Data layer: Entity, DAO, Repository, ViewModel, Room, AppDatabase, API, real CRUD, real auth/session, real appointment & payment logic.

## 13. Agent Rules

1. New session: READ AGENT_CONTEXT.md FIRST.
2. Do NOT scan the whole repository by default.
3. Only open additional files directly required by the current task.
4. Before stopping after any dev task: UPDATE AGENT_CONTEXT.md.
5. Task completed: record under Completed, record verification/build status, update Next Action.
6. Task unfinished: keep under In Progress, record what was completed + remaining work + blockers/errors, update Next Action.
7. New issue/task discovered: record in Known Issues or Next Action.
8. Do NOT mark anything Completed unless verified.
9. Keep AGENT_CONTEXT.md short; replace outdated status, don't accumulate history.
10. Antigravity is the primary implementation agent. Codex is READ ONLY — audit/report only, no project file changes.
11. Backend/database work begins only after the UI phase is sufficiently complete.

## Token Efficiency

AGENT_CONTEXT.md is NOT a history log. Keep compact. Remove/replace outdated info.