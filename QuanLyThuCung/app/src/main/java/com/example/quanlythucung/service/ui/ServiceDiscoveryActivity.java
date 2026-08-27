package com.example.quanlythucung.service.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

public class ServiceDiscoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_discovery);

        setupTopBar();
        setupFilterChips();
        setupActions();
    }

    private void setupTopBar() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView btnNotifications = findViewById(R.id.btnNotifications);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());
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

    private void setupActions() {
        MaterialButton btnBackHome = findViewById(R.id.btnBackHome);
        if (btnBackHome != null) {
            btnBackHome.setOnClickListener(v -> finish());
        }
    }
}
