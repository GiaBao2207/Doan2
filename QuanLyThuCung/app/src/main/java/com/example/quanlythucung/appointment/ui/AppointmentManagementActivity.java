package com.example.quanlythucung.appointment.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class AppointmentManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_management);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());

        TextInputEditText etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng tìm kiếm", Toast.LENGTH_SHORT).show());

        ChipGroup chipGroupDate = findViewById(R.id.chipGroupDate);
        chipGroupDate.setOnCheckedStateChangeListener((group, checkedIds) -> {
            // Placeholder date filter handling
        });

        ChipGroup chipGroupStatus = findViewById(R.id.chipGroupStatus);
        chipGroupStatus.setOnCheckedStateChangeListener((group, checkedIds) -> {
            // Placeholder status filter handling
        });
    }
}
