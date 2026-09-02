package com.example.quanlythucung.service.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;

public class ServiceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        setupTopBar();
        setupActions();
    }

    private void setupTopBar() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void setupActions() {
        MaterialButton btnBookService = findViewById(R.id.btnBookService);
        if (btnBookService != null) {
            btnBookService.setOnClickListener(v ->
                    Toast.makeText(this, "Tính năng đặt lịch dịch vụ đang được phát triển", Toast.LENGTH_SHORT).show());
        }
    }
}
