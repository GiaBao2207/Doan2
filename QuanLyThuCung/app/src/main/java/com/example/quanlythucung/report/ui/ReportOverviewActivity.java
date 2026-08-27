package com.example.quanlythucung.report.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

public class ReportOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_overview);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());

        ChipGroup chipGroupPeriod = findViewById(R.id.chipGroupPeriod);
        chipGroupPeriod.setOnCheckedStateChangeListener((group, checkedIds) -> {
            // Placeholder period filter handling
        });

        TextView btnDetailRevenue = findViewById(R.id.btnDetailRevenue);
        btnDetailRevenue.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo doanh thu chi tiết", Toast.LENGTH_SHORT).show());

        TextView btnDetailAppointment = findViewById(R.id.btnDetailAppointment);
        btnDetailAppointment.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo lịch hẹn chi tiết", Toast.LENGTH_SHORT).show());

        TextView btnDetailInventory = findViewById(R.id.btnDetailInventory);
        btnDetailInventory.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo kho chi tiết", Toast.LENGTH_SHORT).show());

        TextView btnDetailService = findViewById(R.id.btnDetailService);
        btnDetailService.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo dịch vụ chi tiết", Toast.LENGTH_SHORT).show());

        MaterialButton btnQuickRevenue = findViewById(R.id.btnQuickRevenue);
        btnQuickRevenue.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo doanh thu", Toast.LENGTH_SHORT).show());

        MaterialButton btnQuickOrders = findViewById(R.id.btnQuickOrders);
        btnQuickOrders.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo đơn hàng", Toast.LENGTH_SHORT).show());

        MaterialButton btnQuickAppointments = findViewById(R.id.btnQuickAppointments);
        btnQuickAppointments.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo lịch hẹn", Toast.LENGTH_SHORT).show());

        MaterialButton btnQuickInventory = findViewById(R.id.btnQuickInventory);
        btnQuickInventory.setOnClickListener(v ->
                Toast.makeText(this, "Báo cáo tồn kho", Toast.LENGTH_SHORT).show());
    }
}
