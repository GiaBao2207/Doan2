package com.example.quanlythucung.service.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.example.quanlythucung.customer.ui.CustomerHomeActivity;
import com.example.quanlythucung.pet.ui.MyPetsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

public class ServiceDiscoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_discovery);

        setupBottomNavigation();
        setupFilterChips();
        setupNotifications();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavCustomer);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_customer_shop);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_customer_shop) {
                    return true;
                } else if (itemId == R.id.nav_customer_home) {
                    startActivity(new Intent(this, CustomerHomeActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_customer_pets) {
                    startActivity(new Intent(this, MyPetsActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_customer_appointments) {
                    Toast.makeText(this, "Tính năng lịch hẹn đang được phát triển", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (itemId == R.id.nav_customer_account) {
                    Toast.makeText(this, "Tài khoản đang được phát triển", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return false;
            });
        }
    }

    private void setupFilterChips() {
        ChipGroup chipGroup = findViewById(R.id.chipGroupCategories);
        if (chipGroup != null) {
            chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
                // UI placeholder feedback for category filter
                if (!checkedIds.isEmpty()) {
                    int checkedId = checkedIds.get(0);
                    if (checkedId == R.id.chipAll) {
                        // All categories selected
                    } else if (checkedId == R.id.chipSpa) {
                        // Spa selected
                    } else if (checkedId == R.id.chipBoarding) {
                        // Boarding selected
                    } else if (checkedId == R.id.chipHealth) {
                        // Health selected
                    }
                }
            });
        }
    }

    private void setupNotifications() {
        android.widget.ImageView btnNotifications = findViewById(R.id.btnNotifications);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());
        }
    }
}
