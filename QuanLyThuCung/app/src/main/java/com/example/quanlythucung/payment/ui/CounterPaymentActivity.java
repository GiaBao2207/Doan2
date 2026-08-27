package com.example.quanlythucung.payment.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class CounterPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_payment);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnPayCash = findViewById(R.id.btnPayCash);
        MaterialButton btnPayTransfer = findViewById(R.id.btnPayTransfer);
        MaterialButton btnPayCard = findViewById(R.id.btnPayCard);

        if (btnPayCash != null) {
            btnPayCash.setOnClickListener(v ->
                    Toast.makeText(this, "Tiền mặt được chọn", Toast.LENGTH_SHORT).show());
        }
        if (btnPayTransfer != null) {
            btnPayTransfer.setOnClickListener(v ->
                    Toast.makeText(this, "Chuyển khoản được chọn", Toast.LENGTH_SHORT).show());
        }
        if (btnPayCard != null) {
            btnPayCard.setOnClickListener(v ->
                    Toast.makeText(this, "Thẻ được chọn", Toast.LENGTH_SHORT).show());
        }

        MaterialButton btnBackToHandover = findViewById(R.id.btnBackToHandover);
        if (btnBackToHandover != null) {
            btnBackToHandover.setOnClickListener(v -> finish());
        }

        MaterialButton btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        if (btnConfirmPayment != null) {
            btnConfirmPayment.setOnClickListener(v ->
                    Toast.makeText(this, "Chức năng sẽ được kết nối ở bước xử lý nghiệp vụ", Toast.LENGTH_SHORT).show());
        }

        TextInputLayout tilPromoCode = findViewById(R.id.etPromoCode) == null ? null :
                (TextInputLayout) findViewById(R.id.etPromoCode).getParent().getParent();
        if (tilPromoCode != null) {
            tilPromoCode.setEndIconOnClickListener(v ->
                    Toast.makeText(this, "Áp dụng mã khuyến mãi", Toast.LENGTH_SHORT).show());
        }
    }
}
