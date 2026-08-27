package com.example.quanlythucung.core.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.example.quanlythucung.appointment.ui.AppointmentManagementActivity;
import com.example.quanlythucung.employee.ui.EmployeeManagementActivity;
import com.example.quanlythucung.inventory.ui.InventoryManagementActivity;
import com.example.quanlythucung.product.ui.ProductManagementActivity;
import com.example.quanlythucung.promotion.ui.PromotionManagementActivity;
import com.example.quanlythucung.report.ui.ReportOverviewActivity;
import com.example.quanlythucung.service.ui.ServiceManagementActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_appointments) {
                startActivity(new Intent(this, AppointmentManagementActivity.class));
                return true;
            } else if (itemId == R.id.nav_inventory) {
                startActivity(new Intent(this, InventoryManagementActivity.class));
                return true;
            } else if (itemId == R.id.nav_reports) {
                startActivity(new Intent(this, ReportOverviewActivity.class));
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreMenu();
                return true;
            }
            return false;
        });

        MaterialCardView btnEmployee = findViewById(R.id.btnShortcutEmployee);
        btnEmployee.setOnClickListener(v ->
                startActivity(new Intent(this, EmployeeManagementActivity.class)));

        MaterialCardView btnService = findViewById(R.id.btnShortcutService);
        btnService.setOnClickListener(v ->
                startActivity(new Intent(this, ServiceManagementActivity.class)));

        MaterialCardView btnProduct = findViewById(R.id.btnShortcutProduct);
        btnProduct.setOnClickListener(v ->
                startActivity(new Intent(this, ProductManagementActivity.class)));

        MaterialCardView btnInventory = findViewById(R.id.btnShortcutInventory);
        if (btnInventory != null) {
            btnInventory.setOnClickListener(v ->
                    startActivity(new Intent(this, InventoryManagementActivity.class)));
        }

        MaterialCardView btnPromotion = findViewById(R.id.btnShortcutPromotion);
        if (btnPromotion != null) {
            btnPromotion.setOnClickListener(v ->
                    startActivity(new Intent(this, PromotionManagementActivity.class)));
        }

        MaterialCardView btnReport = findViewById(R.id.btnShortcutReport);
        if (btnReport != null) {
            btnReport.setOnClickListener(v ->
                    startActivity(new Intent(this, ReportOverviewActivity.class)));
        }
    }

    private void showMoreMenu() {
        new AlertDialog.Builder(this)
                .setTitle("Menu")
                .setItems(new String[]{
                        "Quản lý nhân viên",
                        "Quản lý dịch vụ",
                        "Quản lý sản phẩm",
                        "Khuyến mãi",
                        "Thông báo",
                        "Hồ sơ",
                        "Đăng xuất"
                }, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            startActivity(new Intent(this, EmployeeManagementActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(this, ServiceManagementActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(this, ProductManagementActivity.class));
                            break;
                        case 3:
                            startActivity(new Intent(this, PromotionManagementActivity.class));
                            break;
                        default:
                            break;
                    }
                })
                .show();
    }
}