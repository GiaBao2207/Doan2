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

- **Customer UI (COMPLETED batch 1, UI-only)**:
  - CustomerHomeActivity (customer/ui)
  - MyPetsActivity (pet/ui)
  - ServiceDiscoveryActivity (service/ui)

All screens follow the UI-only placeholder rule: 0 summary totals, empty states for lists/charts, zero fake data, no Room/DB backend.

## 7. Current Task

Customer UI batch 1 completed. Ready for batch 2 planning.

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
- Customer UI batch 1 (Customer Home, My Pets, Service Discovery) (lintDebug PASS, build PASS):
  - CustomerHomeActivity (customer/ui) + activity_customer_home.xml (Header, 4 quick actions, upcoming appointment empty state, my pets preview empty state, 3 featured service cards, Customer bottom navigation).
  - MyPetsActivity (pet/ui) + activity_my_pets.xml (Header, title/subtitle, Add Pet CTA, pet empty state, Customer bottom navigation).
  - ServiceDiscoveryActivity (service/ui) + activity_service_discovery.xml (Top App Bar + Back, search field, category chips, danh mục dịch vụ, danh sách dịch vụ empty state, NO bottom nav).
  - Vector drawables added: ic_pets, ic_storefront, ic_content_cut, ic_add_circle.
  - Menu created: menu_customer_bottom_nav.xml.
  - AndroidManifest.xml updated; launcher unchanged (LoginActivity).
  - Zero fake business data; UI-only navigation and feedback.

## 11. Next Action

Review and design the next Customer UI batch in Stitch:
- Add/Edit Pet
- Pet Detail
- Service Detail / Booking entry

## 12. Not Started

- Customer UI batch 2 (Add/Edit Pet, Pet Detail, Service Detail / Booking entry, Orders, Profile, etc.).
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