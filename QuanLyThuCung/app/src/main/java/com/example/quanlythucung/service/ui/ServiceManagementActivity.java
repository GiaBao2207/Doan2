package com.example.quanlythucung.service.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;

public class ServiceManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        MaterialButton btnAdd = findViewById(R.id.btnAddService);
        btnAdd.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng Thêm dịch vụ", Toast.LENGTH_SHORT).show());
    }
}