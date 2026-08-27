package com.example.quanlythucung.auth.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quanlythucung.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextInputEditText etFullName;
    private TextInputEditText etIdentifier;
    private TextInputEditText etPhone;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialCheckBox cbTerms;
    private TextView tvTerms;
    private MaterialButton btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupTermsText();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etFullName = findViewById(R.id.etFullName);
        etIdentifier = findViewById(R.id.etIdentifier);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        tvTerms = findViewById(R.id.tvTerms);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupTermsText() {
        String primaryHex = String.format("#%06X", 0xFFFFFF & ContextCompat.getColor(this, R.color.color_primary));
        String termsHtml = "Tôi đồng ý với <font color='" + primaryHex + "'><b>Điều khoản dịch vụ</b></font> và <font color='" + primaryHex + "'><b>Chính sách bảo mật</b></font>";
        Spanned styledText = Html.fromHtml(termsHtml, Html.FROM_HTML_MODE_LEGACY);
        tvTerms.setText(styledText);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tvLogin.setOnClickListener(v -> finish());

        tvTerms.setOnClickListener(v -> cbTerms.setChecked(!cbTerms.isChecked()));

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
            String identifier = etIdentifier.getText() != null ? etIdentifier.getText().toString().trim() : "";
            String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : "";

            if (fullName.isEmpty() || identifier.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!cbTerms.isChecked()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng đồng ý với Điều khoản dịch vụ và Chính sách bảo mật", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(RegisterActivity.this, "Nút đăng ký được nhấn (UI Only)", Toast.LENGTH_SHORT).show();
        });
    }
}
