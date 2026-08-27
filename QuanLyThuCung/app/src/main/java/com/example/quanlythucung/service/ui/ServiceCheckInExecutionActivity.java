package com.example.quanlythucung.service.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ServiceCheckInExecutionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_check_in_execution);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageButton btnNotifications = findViewById(R.id.btnNotifications);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnStartService = findViewById(R.id.btnStartService);
        if (btnStartService != null) {
            btnStartService.setOnClickListener(v ->
                    Toast.makeText(this, "Bắt đầu dịch vụ", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnUpdateNotes = findViewById(R.id.btnUpdateNotes);
        if (btnUpdateNotes != null) {
            btnUpdateNotes.setOnClickListener(v ->
                    Toast.makeText(this, "Cập nhật ghi chú", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnBackToList = findViewById(R.id.btnBackToList);
        if (btnBackToList != null) {
            btnBackToList.setOnClickListener(v -> finish());
        }

        View btnContactCustomer = findViewById(R.id.btnContactCustomer);
        if (btnContactCustomer != null) {
            btnContactCustomer.setOnClickListener(v ->
                    Toast.makeText(this, "Liên hệ khách hàng", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnCancelAppointment = findViewById(R.id.btnCancelAppointment);
        if (btnCancelAppointment != null) {
            btnCancelAppointment.setOnClickListener(v -> showCancelAppointmentDialog());
        }
    }

    private void showCancelAppointmentDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_appointment, null);
        dialog.setContentView(dialogView);

        RadioGroup rgCancelReasons = dialogView.findViewById(R.id.rgCancelReasons);
        TextInputLayout tilOtherReasonNote = dialogView.findViewById(R.id.tilOtherReasonNote);

        if (rgCancelReasons != null && tilOtherReasonNote != null) {
            rgCancelReasons.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rbOtherReason) {
                    tilOtherReasonNote.setVisibility(View.VISIBLE);
                } else {
                    tilOtherReasonNote.setVisibility(View.GONE);
                }
            });
        }

        MaterialButton btnConfirmCancelAppointment = dialogView.findViewById(R.id.btnConfirmCancelAppointment);
        if (btnConfirmCancelAppointment != null) {
            btnConfirmCancelAppointment.setOnClickListener(v -> {
                dialog.dismiss();
                Toast.makeText(this, "Đã hủy lịch hẹn", Toast.LENGTH_SHORT).show();
            });
        }

        MaterialButton btnDismissCancelDialog = dialogView.findViewById(R.id.btnDismissCancelDialog);
        if (btnDismissCancelDialog != null) {
            btnDismissCancelDialog.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }
}
