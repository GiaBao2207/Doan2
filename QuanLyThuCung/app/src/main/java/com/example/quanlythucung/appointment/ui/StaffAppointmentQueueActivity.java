package com.example.quanlythucung.appointment.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class StaffAppointmentQueueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_appointment_queue);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageButton btnNotifications = findViewById(R.id.btnNotifications);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());
        }

        TextInputEditText etSearch = findViewById(R.id.etSearch);
        if (etSearch != null) {
            etSearch.setOnClickListener(v ->
                    Toast.makeText(this, "Chức năng tìm kiếm", Toast.LENGTH_SHORT).show());
        }

        ChipGroup chipGroupDate = findViewById(R.id.chipGroupDate);
        if (chipGroupDate != null) {
            chipGroupDate.setOnCheckedStateChangeListener((group, checkedIds) -> {
                // Placeholder date filter handling
            });
        }

        ChipGroup chipGroupStatus = findViewById(R.id.chipGroupStatus);
        if (chipGroupStatus != null) {
            chipGroupStatus.setOnCheckedStateChangeListener((group, checkedIds) -> {
                // Placeholder status filter handling
            });
        }
    }
}
