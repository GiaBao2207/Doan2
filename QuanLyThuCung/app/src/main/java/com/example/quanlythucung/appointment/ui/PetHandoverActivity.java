package com.example.quanlythucung.appointment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.example.quanlythucung.payment.ui.CounterPaymentActivity;
import com.google.android.material.button.MaterialButton;

public class PetHandoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_handover);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnBackFromHandover = findViewById(R.id.btnBackFromHandover);
        if (btnBackFromHandover != null) {
            btnBackFromHandover.setOnClickListener(v -> finish());
        }

        MaterialButton btnContactCustomerHandover = findViewById(R.id.btnContactCustomerHandover);
        if (btnContactCustomerHandover != null) {
            btnContactCustomerHandover.setOnClickListener(v ->
                    Toast.makeText(this, "Liên hệ khách hàng", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnProceedToPayment = findViewById(R.id.btnProceedToPayment);
        if (btnProceedToPayment != null) {
            btnProceedToPayment.setOnClickListener(v -> {
                Intent intent = new Intent(this, CounterPaymentActivity.class);
                startActivity(intent);
            });
        }
    }
}
