package com.example.quanlythucung.inventory.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class InventoryManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_management);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());

        MaterialButton btnAddStock = findViewById(R.id.btnAddStock);
        btnAddStock.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng Nhập kho", Toast.LENGTH_SHORT).show());

        TextInputEditText etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng tìm kiếm", Toast.LENGTH_SHORT).show());

        ChipGroup chipGroup = findViewById(R.id.chipGroupStockFilter);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            // Placeholder filter handling
        });
    }
}
