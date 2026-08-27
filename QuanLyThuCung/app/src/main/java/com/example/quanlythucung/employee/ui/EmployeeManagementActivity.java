package com.example.quanlythucung.employee.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EmployeeManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Menu")
                        .setItems(new String[]{"Khuyến mãi", "Thông báo", "Hồ sơ"}, (d, w) -> {
                        })
                        .show());

        MaterialButton btnAdd = findViewById(R.id.btnAddEmployee);
        btnAdd.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng Thêm nhân viên", Toast.LENGTH_SHORT).show());

        TextInputEditText etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng tìm kiếm", Toast.LENGTH_SHORT).show());

        MaterialButton btnStatus = findViewById(R.id.btnFilterStatus);
        btnStatus.setOnClickListener(v -> showFilter("Trạng thái"));

        MaterialButton btnSort = findViewById(R.id.btnFilterSort);
        btnSort.setOnClickListener(v -> showFilter("Sắp xếp"));
    }

    private void showFilter(String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(new String[]{"Tất cả", "Đang hoạt động", "Tạm khóa"}, (d, w) -> {
                })
                .show();
    }
}