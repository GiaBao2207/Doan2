# PetStoreApp Agent Context

## 1. Project Summary

- Android app, Java + XML.
- Development strategy: UI-first.
- Main roles: Admin, Staff, Customer.
- Stitch is the UI source of truth.
- Reuse the existing PetStoreApp Android Design System.
- Room/SQLite and business/data layers planned for later.

## 2. Target Architecture

Target: UI → ViewModel → Repository → DAO → Room Database.

Current implementation: UI Java/XML + Design System + package skeleton only.
Backend/data layers are NOT implemented.

## 3. Development Workflow

Order: Design System → UI screens → UI navigation → main business screens → Entity/DAO/Room → Repository/ViewModel → business logic → integration → testing/cleanup.

## 4. Main Business Modules

auth, user, customer, employee, pet, service, appointment, product, inventory, order, payment, promotion, loyalty, notification, review, report, core.

Most are skeleton/preparation packages unless real source exists.

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

## 6. Implemented UI

- Auth UI: LoginActivity, RegisterActivity, ForgotPasswordActivity, ResetPasswordActivity, ResetPasswordSuccessActivity (UI-only, basic validation/navigation).
- Admin Core UI:
  - AdminDashboardActivity (core/ui)
  - EmployeeManagementActivity (employee/ui)
  - ServiceManagementActivity (service/ui)
  - ProductManagementActivity (product/ui)
  - InventoryManagementActivity (inventory/ui)
  - PromotionManagementActivity (promotion/ui)
  - AppointmentManagementActivity (appointment/ui)
  - ReportOverviewActivity (report/ui)

All screens follow the UI-only placeholder rule: 0 summary totals, empty states for lists/charts, zero fake data, no Room/DB backend.

## 7. Admin Core Status

Stitch Admin Core designed & matched:
- Admin Dashboard
- Employee Management
- Service Management
- Product Management
- Inventory Management
- Promotion Management
- Appointment Management
- Report Overview

All declared in AndroidManifest.xml and wired into AdminDashboardActivity navigation.

## 8. Current Task

Phase: Staff Operations Android UI Batch 2 — Pet Handover + Counter Payment.
Focus: (complete) Both screens implemented and Manifest declared. Build PASS.

## 9. In Progress

(none)

## 10. Known Issues

(none — build passes.)

## 11. Completed

- Auth UI screens (UI-only).
- Global color consistency sync & palette refinement (Build PASS).
- Admin Dashboard UI refinement (Build PASS): 54dp logo, 0-stats, 5 bottom nav items with modal menu, 6 quick shortcuts.
- Admin Management screens batch 1 (Employee, Service, Product) (Build PASS).
- Service Management Add Service action refinement (Build PASS).
- Admin Management screens batch 2 (Inventory, Promotion) (Build PASS).
- Admin Operations screens batch 3 (Appointment, Report Overview) (Build PASS).
- Staff Operations batch 1 (Staff Appointment Queue, Service Check-in & Execution) (Build PASS):
  - StaffAppointmentQueueActivity (appointment/ui): 2x2 stat cards, date chips, search, status filter chips, empty state.
  - ServiceCheckInExecutionActivity (service/ui): 6-step timeline, warning card, check-in inputs, service execution card, THAO TÁC KHÁC section (Cập nhật ghi chú, Quay lại danh sách, Liên hệ khách hàng, Hủy lịch hẹn + BottomSheet cancellation dialog), fixed bottom "Bắt đầu dịch vụ" CTA.
- Staff Operations batch 2 (Pet Handover, Counter Payment) (Build PASS):
  - PetHandoverActivity (appointment/ui) + activity_pet_handover.xml: Top App Bar + Back, "Bàn giao thú cưng" title + subtitle, 2-column appointment/pet info card (Mã lịch hẹn, Khách hàng, Thú cưng, Loại thú cưng, Dịch vụ, Nhân viên phụ trách, Giờ tiếp nhận, Giờ hoàn thành), "DỊCH VỤ ĐÃ THỰC HIỆN" section (placeholder rows + total "--"), "KIỂM TRA TRƯỚC BÀN GIAO" checklist (5 checkboxes matching Stitch), "GHI CHÚ BÀN GIAO" note input, secondary actions (Quay lại, Liên hệ khách), fixed bottom "Tiến hành thanh toán" CTA → navigates to CounterPaymentActivity.
  - CounterPaymentActivity (payment/ui) + activity_counter_payment.xml: Top App Bar + Back, "Thanh toán tại quầy" title + subtitle, customer/transaction summary card (2x2 grid: Khách hàng, Thú cưng, Mã lịch hẹn, Mã đơn hàng), "CHI TIẾT THANH TOÁN" (service+product rows, 0 ₫ placeholders), promo code input, "TỔNG THANH TOÁN" (Tạm tính/Giảm giá/Phụ phí/Tổng cộng all 0 ₫), "PHƯƠNG THỨC THANH TOÁN" chips (Tiền mặt/Chuyển khoản/Thẻ), "Quay lại bàn giao" secondary, fixed bottom "Xác nhận thanh toán" CTA.
  - Vector icons added: ic_account_balance, ic_credit_card.
  - Manifest: both activities declared; launcher unchanged (LoginActivity).
  - Staff workflow navigation: ServiceCheckInExecutionActivity → PetHandoverActivity → CounterPaymentActivity wired (UI only).
  - Zero fake data; all monetary placeholders use "0 ₫"; no backend/database.

## 12. Next Action

Staff Operations UI batch 2 complete. Proceed to Customer UI screens or plan Room/database architecture layer.

## 13. Not Started

Remaining Staff UI (POS / Payment / Service Completion if designed), Customer UI.
Backend/data: Room, Entity, DAO, Repository, ViewModel, business logic.

## 14. Agent Rules

1. New session: READ AGENT_CONTEXT.md FIRST.
2. Do NOT scan the whole repository by default.
3. Only open additional files directly required by the current task.
4. Before stopping after any dev task: UPDATE AGENT_CONTEXT.md.
5. Task completed: record under Completed, record verification/build status, update Next Action.
6. Task unfinished: keep under In Progress, record what was completed + remaining work + blockers/errors, update Next Action.
7. New issue/task discovered: record in Known Issues or Next Action.
8. Do NOT mark anything Completed unless verified.
9. Keep AGENT_CONTEXT.md short; replace outdated status, don't accumulate history.
10. Codex is READ ONLY in this workflow — audit/report only, no project file changes.
11. Actual modifications performed by Antigravity or explicitly approved OpenCode tasks.

## Token Efficiency

AGENT_CONTEXT.md is NOT a history log. Keep compact. Remove/replace outdated info.