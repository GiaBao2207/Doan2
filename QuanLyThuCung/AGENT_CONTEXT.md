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
- Image Placeholder Rules:
  - Add/Edit Pet "Thêm ảnh" remains camera/upload action UI.
  - Standardized Paw Logo placeholder for all future data-image areas when no real image data exists (Pet Detail, Service Detail, future products, etc.).
  - Paw Logo placeholder is circular, centered, inner fill #FFFFFF, subtle stroke, no inner square.
  - Real data images replace placeholders when data becomes available.

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

Customer UI batch 2 completed (Add/Edit Pet, Pet Detail, Service Detail). Ready for Customer UI batch 3 planning.

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
- Customer UI batch 1 (Customer Home, My Pets, Service Discovery) (lintDebug PASS, build PASS).
- Customer UI batch 2 (Add / Edit Pet, Pet Detail, Service Detail) (lintDebug PASS, build PASS):
  - Visual correction & Stitch synchronization completed:
    - AddEditPetActivity (pet/ui) + activity_add_edit_pet.xml: full-width resilient pill buttons for species (Chó, Mèo, Khác) and gender (Đực, Cái, Không rõ) with sage green selection, conditional other species input, photo upload action area (camera icon), date picker, weight with kg unit, notes multiline input, primary Save CTA, edit mode CTA + delete support, NO bottom nav.
    - PetDetailActivity (pet/ui) + activity_pet_detail.xml: Top App Bar with brand title and more options, pet profile hero with standardized Paw Logo placeholder (#FFFFFF fill, centered paw icon), info card with neutral placeholders, notes empty state, service history empty state, primary Booking CTA, secondary Edit CTA, NO bottom nav.
    - ServiceDetailActivity (service/ui) + activity_service_detail.xml: Top App Bar matching Stitch (Service Detail), service hero with standardized Paw Logo placeholder (#FFFFFF fill, centered paw icon), info rows, neutral description, suitable-pet chips, neutral notes/notice, primary Booking CTA, NO bottom nav.
    - Vector drawables added: ic_add_a_photo, ic_edit_note, ic_history, sl_chip_background_species, sl_chip_text_species, sl_chip_stroke_species.
    - MyPetsActivity wired to AddEditPetActivity; PetDetailActivity wired to AddEditPetActivity in edit mode.
    - AndroidManifest.xml declared; launcher unchanged (LoginActivity).
    - Zero fake business data; UI-only navigation and feedback.

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