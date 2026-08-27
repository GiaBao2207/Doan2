package com.example.quanlythucung.product.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProductManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());

        MaterialButton btnAdd = findViewById(R.id.btnAddProduct);
        btnAdd.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng Thêm sản phẩm", Toast.LENGTH_SHORT).show());

        TextInputEditText etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng tìm kiếm", Toast.LENGTH_SHORT).show());

        MaterialButton btnCategory = findViewById(R.id.btnFilterCategory);
        btnCategory.setOnClickListener(v -> showFilter("Danh mục"));

        MaterialButton btnStatus = findViewById(R.id.btnFilterStatus);
        btnStatus.setOnClickListener(v -> showFilter("Trạng thái"));
    }

    private void showFilter(String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(new String[]{"Tất cả", "Đang bán", "Sắp hết hàng", "Bản nháp"}, (d, w) -> {
                })
                .show();
    }
}