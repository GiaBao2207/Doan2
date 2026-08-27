package com.example.quanlythucung.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextInputEditText etIdentifier;
    private MaterialButton btnContinue;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etIdentifier = findViewById(R.id.etIdentifier);
        btnContinue = findViewById(R.id.btnContinue);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tvBackToLogin.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> {
            String identifier = etIdentifier.getText() != null ? etIdentifier.getText().toString().trim() : "";
            if (identifier.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập Email hoặc Tên đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
    }
}
